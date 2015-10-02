package fr.cyann.jinyparser.token;
/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The Source position manager definition.<br>
 * Manage the position in the source code.
 */
public class SourcePosition {

	private final int firstLine;
	private final int firstColumn;

	private int pos;
	private int line;
	private int column;

	/**
	 * Default constructor.
	 * @param firstLine what is the first number of line.
	 * @param firstColumn what is the first number of column.
	 */
	public SourcePosition(int firstLine, int firstColumn) {
		this.pos = 0;
		this.firstLine = firstLine;
		this.firstColumn = firstColumn;
		this.line = this.firstLine;
		this.column = this.firstColumn;
	}

	/**
	 * Get the current position.
	 * @return current position.
	 */
	public int getPos() {
		return pos;
	}

	/**
	 * Get the current line position.
	 * @return current line position.
	 */
	public int getLine() {
		return line;
	}

	/**
	 * Get the current column position.
	 * @return current column position.
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * Move position to next character.
	 */
	public void increment() {
		pos++;
		column++;
	}

	/**
	 * Move position to next line.
	 */
	public void newLine() {
		pos++;
		line++;
		column = firstColumn;
	}
}
