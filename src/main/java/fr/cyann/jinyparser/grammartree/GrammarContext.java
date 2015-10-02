package fr.cyann.jinyparser.grammartree;/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.token.SourcePosition;
import fr.cyann.jinyparser.token.Token;
import fr.cyann.jinyparser.utils.StringLookaheadIterator;

/**
 * The GrammarContext class. A context pass through parser tree that contains all intermediates states of lexing / parsing.<br>
 * All the parsing resources.
 */
public class GrammarContext {

	private final StringLookaheadIterator it;
	private final SourcePosition pos;
	private final StringBuilder term;
	private Token token;

	/**
	 * Default constructor.
	 * @param source the source code to parse.
	 */
	public GrammarContext(String source) {
		it = new StringLookaheadIterator(source);
		pos = new SourcePosition(1, 1);
		term = new StringBuilder();
	}

	//region Char Iterator

	/**
	 * Mark current character to give ability to backtrack to this position.
	 */
	public void markChar() {
		it.mark();
	}

	/**
	 * Backtrack to the last marked position.
	 */
	public void rollbackChar() {
		it.rollback();
	}

	/**
	 * Return the current character of source.
	 * @return the current character.
	 */
	public Character currentChar() {
		return it.current();
	}

	/**
	 * Jump to next character in the source code.
	 */
	public void nextChar() {
		term.append(it.current());
		it.next();
	}

	//endregion

	// region Token

	/**
	 * Empty the term buffer.
	 */
	public void resetTerm() {
		term.delete(0, term.length());
	}

	/**
	 * Get the term from the buffer.
	 * @return the constructed term.
	 */
	public String getTerm() {
		return term.toString();
	}

	/**
	 * Jump to next line.
	 */
	public void newLine() {
		pos.newLine();
	}

	/**
	 * Get the current position.
	 * @return the current position.
	 */
	public int getPos() {
		return pos.getPos();
	}

	/**
	 * Get the current line number.
	 * @return the current line number.
	 */
	public int getLine() {
		return pos.getLine();
	}

	/**
	 * Get the current column number.
	 * @return the current column number.
	 */
	public int getColumn() {
		return pos.getColumn();
	}
	// endregion

	// region Lexer

	/**
	 * Get the current constructed token.
	 * @return the current token.
	 */
	public Token getCurrentToken() {
		return token;
	}

	/**
	 * Set the current token.
	 * @param token the current token to store.
	 */
	public void setCurrentToken(Token token) {
		this.token = token;
	}

	// endregion

	/**
	 * Get the string representation of the object. Useful for debugging.
	 * @return
	 */
	@Override
	public String toString() {
		return "ParseContext:\n" + it.toString() + /*"\nAST: " + getAst().toString()*/ +'\n';
	}
}
