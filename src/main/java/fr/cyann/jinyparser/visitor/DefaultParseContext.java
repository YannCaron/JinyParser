package fr.cyann.jinyparser.visitor;/**
 * Copyright (C) 09/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.ast.Ast;
import fr.cyann.jinyparser.ast.AstStack;
import fr.cyann.jinyparser.token.Token;
import fr.cyann.jinyparser.utils.LookaheadIterator;
import fr.cyann.jinyparser.utils.StringLookaheadIterator;

import java.util.Stack;

/**
 * The DefaultParseContext class definition.
 */
class DefaultParseContext extends Context implements LookaheadIterator<Character>, AstStack {

	private final StringLookaheadIterator it;
	private final Stack<Token> tokens;
	private final Stack<Ast> asts;

	/**
	 * Default constructor.
	 *
	 * @param source The source to parse.
	 */
	public DefaultParseContext(@SuppressWarnings("SameParameterValue") String source) {
		super(source);
		it = new StringLookaheadIterator(source);
		tokens = new Stack<Token>();
		asts = new Stack<Ast>();
	}

	//region Iterator

	public boolean hasNext() {
		return it.hasNext();
	}

	public Character current() {
		return it.current();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void mark() {
		it.mark();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rollback() {
		it.rollback();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resume() {
		it.resume();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void next() {
		if (hasNext()) it.next();
	}
	//endregion

	//region token stack

	/**
	 * {@inheritDoc}
	 */
	public Token pushToken(Token item) {
		return tokens.push(item);
	}

	/**
	 * {@inheritDoc}
	 */
	public Token popToken() {
		return tokens.pop();
	}

	//endregion

	//region Ast Stack

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void pushAst(Ast ast) {
		asts.push(ast);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Ast popAst() {
		return asts.pop();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Ast getAst() {
		return asts.firstElement();
	}
	//endregion

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "ParseContext:\n" + it.toString() + "\nAST: " + getAst().toString() + '\n';
	}
}
