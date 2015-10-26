package fr.cyann.jinyparser.parsetree;/**
 * Copyright (C) 28/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.exceptions.JinyException;
import fr.cyann.jinyparser.lexem.Lexem;
import fr.cyann.jinyparser.utils.MultilingualMessage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * The NonTerminal definition.
 */
public abstract class NonTerminal extends ParsemElement {

    private Lexem lexemEnd;

    public NonTerminal(Lexem lexem) {
        super(lexem);
    }

    public Lexem getLexemEnd() {
        return lexemEnd;
    }


	@Override
	public void injectVisitor(VisitorInjector injector) {
		setVisitor(injector.getVisitorFor(this));

		try {
			for (Field field : this.getClass().getDeclaredFields()) {
				field.setAccessible(true);

				if (ParsemElement.class.isAssignableFrom(field.getType())) {
					ParsemElement element = (ParsemElement) field.get(this);
					element.injectVisitor(injector);
				} else if (List.class.isAssignableFrom(field.getType())) {
					List<ParsemElement> list = ((List<ParsemElement>) field.get(this));

					for (ParsemElement child : list) {
						child.injectVisitor(injector);
					}
				}
			}
		} catch (Exception e) {
			throw new JinyException(MultilingualMessage.create(e.toString()));
		}
	}

	@Override
	public void aggregate(String fieldName, ParsemElement element) {
        boolean found = false;
        List<String> names = new ArrayList<String>();

        try {
            for (Field field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                AggregateField annotation = field.getAnnotation(AggregateField.class);

                String name = field.getName();
                if (annotation != null && !annotation.identity().equals(AggregateField.DEFAULT_IDENTITY)) {
                    name = annotation.identity();
                    names.add(name);
                }

                if (annotation != null && (name.equals(fieldName))) {

                    if (List.class.isAssignableFrom(field.getType())) {
                        ((List<ParsemElement>) field.get(this)).add(0, element);
                        found = true;
                    } else {
                        field.set(this, element);
                        found = true;
                    }
                }



            }
        } catch (Exception e) {
            throw new JinyException(MultilingualMessage.create(e.toString()));
        }

        if (!found)
            throw new JinyException(MultilingualMessage
                    .create("Field [%s] not found in class [%s].\nPerhaps you forgot the @AggregateField annotation or made a mistake with the name.\nAvailable fields are: %s.")
                    .setArgs(fieldName, this.getClass().toString(), names));

    }

}
