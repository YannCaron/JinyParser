package fr.cyann.jinyparser.token;/**
 * Copyright (C) 30/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The Token object definition.<br>
 * An object that represent the program lexeme with type and position in the source.
 */
public class Token {

	private final String lexeme;
	private final TokenType type;
	private final int pos, line, column;

	/**
	 * Constructor with default parameters.
	 * @param lexeme The string lexeme.
	 * @param type the token type (useful for syntax coloring).
	 */
	public Token(String lexeme, TokenType type) {
		this.lexeme = lexeme;
		this.type = type;
		this.pos = -1;
		this.line = -1;
		this.column = -1;
	}

	/**
	 * Cefault constructor.
	 * @param lexeme The string lexeme.
	 * @param type the token type (useful for syntax coloring).
	 * @param pos the token position.
	 * @param line the token line number.
	 * @param column the token column number.
	 */
	public Token(String lexeme, TokenType type, int pos, int line, int column) {
		this.lexeme = lexeme;
		this.type = type;
		this.pos = pos;
		this.line = line;
		this.column = column;
	}

	/**
	 * Get the token lexeme.
	 * @return token lexeme.
	 */
	public String getLexeme() {
		return lexeme;
	}

	/**
	 * Get the token position in the source code.
	 * @return the token position.
	 */
	public int getPos() {
		return pos;
	}

	/**
	 * Get the token line number in the source code.
	 * @return the token line number.
	 */
	public int getLine() {
		return line;
	}

	/**
	 * Get the token column position in the source code.
	 * @return the token column position.
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * Get the lexeme length.
	 * @return lexeme length.
	 */
	public int getLength() {
		return lexeme.length();
	}

	/**
	 * Give the string representation of the object.
	 * @return the string representation.
	 */
	@Override
	public String toString() {
		return "Token{" +
				"lexeme='" + lexeme + '\'' +
				", type=" + type +
				", pos=" + pos +
				", line=" + line +
				", column=" + column +
				'}';
	}
}
