package fr.cyann.jinyparser.grammartree;/**
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
 * The TokenProducer class. Each time the decorated grammar element is parsed, it produce a Token and store it into the context.<br>
 * That represent the lexer producer function.
 */
public class TokenProducer extends GrammarDecorator {

	private final TokenType tokenType;

	/**
	 * The default and mandatory constructor.
	 * @param tokenType the token type to produce.
	 * @param decorated the decorated object.
	 */
	TokenProducer(TokenType tokenType, GrammarElement decorated) {
		super(decorated);
		this.tokenType = tokenType;
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
			Token token = new Token(context.getTerm(), tokenType, context.getPos(), context.getLine(), context.getColumn());
			context.setCurrentToken(token);
			System.out.println(token);
		}

		return res;
	}
}
