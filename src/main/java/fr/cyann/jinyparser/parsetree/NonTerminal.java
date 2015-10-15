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
    public void aggregate(String fieldName, ParsemElement element) {
        /*if (code.equals(ELSEIF_STMT)) {
            elseifs.add(element);
		} else if (code.equals(ELSE_STMT)) {
			else_ = element;
		}*/

        try {
            for (Field field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                AggregateField annotation = field.getAnnotation(AggregateField.class);

                if (annotation != null && annotation.value().equals(fieldName)) {

                    if (List.class.isAssignableFrom(field.getType())) {
                        ((List) field.get(this)).add(element);
                    } else {
                        field.set(this, element);
                    }

                }


            }
        } catch (Exception e) {
            throw new JinyException(MultilingualMessage.create(e.toString()));
        }
    }

}
