package fr.cyann.jinyparser.grammartree;
/**
 * Copyright (C) 14/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.exceptions.JinyException;
import fr.cyann.jinyparser.lexem.Lexem;
import fr.cyann.jinyparser.parsetree.*;
import fr.cyann.jinyparser.utils.MultilingualMessage;

import java.lang.reflect.Constructor;

/**
 * The ParsemCreator class definition.<br>
 */
public class ParsemCreator<P extends ParsemElement> extends GrammarDecorator {

    private final Class<P> clazz;
    private ParsemVisitor<P, ? extends VisitorContext> visitor;

    /**
     * Default constructor.
     *
     * @param decorated the decorated grammar element.
     * @param clazz the grammar element class to create.
     */
    public ParsemCreator(GrammarElement decorated, Class<P> clazz) {
        super(decorated);
        this.clazz = clazz;
    }

	/**
	 * The parsem visitor accessor.<br>
	 * Define the behaviour of the parsem element when it will be traversed.
	 *
	 * @param visitor the visitor to delegate to the parsem.
	 * @return this.
	 */
    public GrammarElement setVisitor(ParsemVisitor<P, ? extends VisitorContext> visitor) {
        this.visitor = visitor;
		return this;
	}

	/**
	 * {@inheritDoc}
     */
    @Override
    protected boolean lookahead(GrammarContext context) {
        return decorated.lookahead(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean parse(GrammarContext context) {
        context.resetTerm();

        boolean res = decorated.parse(context);

        if (res) {

            try {

                Constructor<P> constructor = clazz.getConstructor(Lexem.class);
                constructor.setAccessible(true);
                ParsemElement parsem = constructor.newInstance(context.getCurrentLexem());
	            parsem.setVisitor(visitor);
	            context.pushParsem(parsem);

            } catch (Exception e) {
                throw new JinyException(MultilingualMessage.create(e.toString()));
            }

        }

        return res;
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	void buildBnf(BnfContext context) {
		if (Terminal.class.isAssignableFrom(clazz) && clazz != DefaultTerminal.class) {
			context.newProduction(clazz.getSimpleName(), decorated);
		} else {
			super.buildBnf(context);
		}
	}
}
