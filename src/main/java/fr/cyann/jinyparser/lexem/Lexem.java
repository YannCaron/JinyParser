package fr.cyann.jinyparser.lexem;/**
 * Copyright (C) 30/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The Lexem object definition.<br>
 * An object that represent the program term with type and position in the source.
 */
public class Lexem {

	private final String term;
	private final LexemType type;
	private final int pos, line, column;

	/**
	 * Constructor with default parameters.
	 * @param term The string term.
	 * @param type the token type (useful for syntax coloring).
	 */
	public Lexem(String term, LexemType type) {
		this.term = term;
		this.type = type;
		this.pos = -1;
		this.line = -1;
		this.column = -1;
	}

	/**
	 * Default constructor.
	 * @param term The string term.
	 * @param type the token type (useful for syntax coloring).
	 * @param pos the token position.
	 * @param line the token line number.
	 * @param column the token column number.
	 */
	public Lexem(String term, LexemType type, int pos, int line, int column) {
		this.term = term;
		this.type = type;
		this.pos = pos;
		this.line = line;
		this.column = column;
	}

	/**
	 * Get the token term.
	 * @return token term.
	 */
	public String getTerm() {
		return term;
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
	 * Get the term length.
	 * @return term length.
	 */
	public int getLength() {
		return term.length();
	}

	/**
	 * Give the string representation of the object.
	 * @return the string representation.
	 */
	@Override
	public String toString() {
		return "Lexem{" +
				"term='" + term + '\'' +
				", type=" + type +
				", pos=" + pos +
				", line=" + line +
				", column=" + column +
				'}';
	}
}
