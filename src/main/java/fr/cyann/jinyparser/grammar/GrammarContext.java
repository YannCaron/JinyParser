package fr.cyann.jinyparser.grammar;/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.token.SourcePositioner;
import fr.cyann.jinyparser.token.Token;
import fr.cyann.jinyparser.utils.StringLookaheadIterator;

/**
 * The GrammarContext definition.
 */
public class GrammarContext {

	private final StringLookaheadIterator it;
	private final SourcePositioner pos;
	private final StringBuilder term;
	private Token token;

	public GrammarContext(String source) {
		it = new StringLookaheadIterator(source);
		pos = new SourcePositioner(1, 1);
		term = new StringBuilder();
	}

	//region Char Iterator

	public void markChar() {
		it.mark();
	}

	public void rollbackChar() {
		it.rollback();
	}

	public Character currentChar() {
		return it.current();
	}

	public void nextChar() {
		term.append(it.current());
		it.next();
	}

	//endregion

	// region Token

	public void resetTerm() {
		term.delete(0, term.length());
	}

	public String getTerm() {
		return term.toString();
	}

	public void newLine() {
		pos.newLine();
	}

	public int getPos() {
		return pos.getPos();
	}

	public int getLine() {
		return pos.getLine();
	}

	public int getColumn() {
		return pos.getColumn();
	}
	// endregion

	// region Lexer

	public Token getCurrentToken() {
		return token;
	}

	public void setCurrentToken(Token token) {
		this.token = token;
	}

	// endregion

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "ParseContext:\n" + it.toString() + /*"\nAST: " + getAst().toString()*/ +'\n';
	}
}
