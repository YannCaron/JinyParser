package fr.cyann.jinyparser.grammartree;
/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.token.Lexem;
import fr.cyann.jinyparser.token.LexemType;

/**
 * The LexemProducer class. Each time the decorated grammar element is parsed, it produce a Lexem and store it into the context.<br>
 * That represent the lexer production function.
 */
public class LexemProducer extends GrammarDecorator {

	private final LexemType lexemType;

	/**
	 * The default and mandatory constructor.
	 * @param lexemType the token type to produce.
	 * @param decorated the decorated object.
	 */
	LexemProducer(LexemType lexemType, GrammarElement decorated) {
		super(decorated);
		this.lexemType = lexemType;
	}

	/** {@inheritDoc} */
	@Override
	protected boolean lookahead(GrammarContext context) {
		return decorated.lookahead(context);
	}

	/** {@inheritDoc} */
	@Override
	public boolean parse(GrammarContext context) {

		context.resetTerm();

		boolean res = decorated.parse(context);

		if (res) {
			Lexem lexem = new Lexem(context.getTerm(), lexemType, context.getPos(), context.getLine(), context.getColumn());
			context.appendLexem(lexem);
		}

		return res;
	}

	/**
	 * Give the BNF representation of the grammar expression.
	 *
	 * @return the BNF representation.
	 */
	@Override
	public String toString() {
		return lexemType + "(" + decorated.toString() + ")";
	}
}
