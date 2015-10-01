package fr.cyann.jinyparser.grammar;/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.token.Token;
import fr.cyann.jinyparser.token.TokenType;

/**
 * The TokenProducer definition.
 */
public class TokenProducer extends GrammarDecorator {

	private final TokenType tokenType;

	public TokenProducer(TokenType tokenType, GrammarElement decored) {
		super(decored);
		this.tokenType = tokenType;
	}

	/**
	 * The backtracking method. Use a lookahead to find if following term / grammar is valid.
	 *
	 * @param context the parsing context that contains all necessary resources to the parsing (iterators, flags and so on).
	 * @return true if lookahead succeed, false otherwise.
	 */
	@Override
	public boolean lookahead(GrammarContext context) {
		return decored.lookahead(context);
	}

	/**
	 * The parsing method.
	 *
	 * @param context the parsing context that contains all necessary resources to the parsing (iterators, flags and so on).
	 * @return true if parsing succeed, false otherwise.
	 */
	@Override
	public boolean parse(GrammarContext context) {

		context.resetTerm();

		boolean res = decored.parse(context);

		if (res) {
			Token token = new Token(context.getTerm(), tokenType, context.getPos(), context.getLine(), context.getColumn());
			context.setCurrentToken(token);
			System.out.println(token);
		}

		return res;
	}
}
