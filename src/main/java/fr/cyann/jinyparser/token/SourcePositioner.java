package fr.cyann.jinyparser.token;/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The SourcePositioner definition.
 */
public class SourcePositioner {

	public final int firstLine;
	public final int firstColumn;

	private int pos;
	private int line;
	private int column;

	public SourcePositioner(int firstLine, int firstColumn) {
		this.pos = 0;
		this.firstLine = firstLine;
		this.firstColumn = firstColumn;
		this.line = this.firstLine;
		this.column = this.firstColumn;
	}

	public int getPos() {
		return pos;
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}

	public void increment() {
		pos++;
		column++;
	}

	public void newLine() {
		line++;
		column = firstColumn;
	}
}
