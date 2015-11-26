package fr.cyann.jinyparser.grammartree;
/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.lexem.Lexem;
import fr.cyann.jinyparser.lexem.LexemType;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.charIn;
import static fr.cyann.jinyparser.grammartree.GrammarFactory.zeroOrMore;

/**
 * The LexemCreatorCore class. Each time the decorated grammar element is parsed, it produce a Lexem and store it into the context.<br>
 * That represent the lexer recursive function.
 */
public class LexemCreator extends GrammarDecorator {

	/**
	 * A default separator grammar.<br>
	 * <i>' ' | '\t' | '\n'</i>
	 */
	public static final GrammarElement DEFAULT_SEPARATOR = zeroOrMore(charIn(" \t\r\n"));

	private final LexemType lexemType;
	private final GrammarElement separator;

    /**
     * The default and mandatory constructor.
     *  @param lexemType the token type to produce.
     * @param separator the separator peace of grammar that will be checked (if exists) at the beginning of decorated grammar.
     * @param decorated the decorated object.
     */
    LexemCreator(LexemType lexemType, GrammarElement separator, GrammarElement decorated) {
        super(decorated);
		this.lexemType = lexemType;
	    this.separator = separator;
	}

	/**
	 * The default and mandatory constructor.
	 *
	 * @param lexemType the token type to produce.
	 * @param decorated the decorated object.
	 */
	LexemCreator(LexemType lexemType, GrammarElement decorated) {
		this(lexemType, DEFAULT_SEPARATOR, decorated);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean lookahead(GrammarContext context) {
		return separator.lookahead(context) && decorated.lookahead(context) && separator.lookahead(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean parse(GrammarContext context) {

		separator.parse(context);

		context.resetTerm();

		boolean res = decorated.parse(context);

		if (res) {
			Lexem lexem = new Lexem(context.getTerm(), lexemType, context.getPositionManager(), context.getLine(), context.getColumn());
			context.appendLexem(lexem);

			separator.parse(context);

		}

		return res;
	}

}
