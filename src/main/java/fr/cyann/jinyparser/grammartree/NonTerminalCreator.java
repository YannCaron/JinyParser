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
import fr.cyann.jinyparser.lexem.LexemType;
import fr.cyann.jinyparser.parsetree.NonTerminal;
import fr.cyann.jinyparser.parsetree.ParsemAccumulator;
import fr.cyann.jinyparser.parsetree.ParsemVisitor;
import fr.cyann.jinyparser.parsetree.VisitorContext;
import fr.cyann.jinyparser.utils.MultilingualMessage;

import java.lang.reflect.Constructor;

/**
 * The TerminalCreator class definition.<br>
 */
public class NonTerminalCreator<P extends NonTerminal> extends GrammarDecorator {

    private final String name;
    private final Class<P> clazz;
	private ParsemVisitor<P, ? extends VisitorContext> visitor;

	/**
	 * Default constructor.
	 * @param clazz     the grammar element class to createTerminal.
	 * @param decorated the decorated grammar element.
	 */
	public NonTerminalCreator(String name, Class<P> clazz, GrammarElement decorated) {
		super(decorated);
		this.name = name;
        this.clazz = clazz;
	}

    /**
     * Get the recursive name.
     *
     * @return the recursive name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the parsem class to produce.
	 *
	 * @return the parsem class to produce.
	 */
	public Class<P> getParsemClass() {
		return clazz;
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

	private void createParsem(GrammarContext context) {

		try {

			// createTerminal non terminal
			Constructor<P> constructor = clazz.getConstructor(Lexem.class);
			constructor.setAccessible(true);
			// TODO: LEXEM ????
			NonTerminal parsem = constructor.newInstance(new Lexem("TODO", LexemType.NONE));

			// createTerminal accumulator
			ParsemAccumulator accumulator = new ParsemAccumulator(null, parsem);
			accumulator.setVisitor(visitor);
			context.pushParsem(accumulator);

		} catch (Exception e) {
			throw new JinyException(MultilingualMessage.create(e.toString()));
		}
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

		createParsem(context);

		boolean ret = decorated.parse(context);

		((ParsemAccumulator) context.popParsem()).build(context);

		return ret;
	}

}
