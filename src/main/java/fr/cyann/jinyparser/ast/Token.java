package fr.cyann.jinyparser.ast;/**
 * Copyright (C) 30/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The Token object definition.
 * An object that represent the program symbol.
 */
public class Token {

	private final String symbol;
	private final int pos, line, column;

	public Token(String symbol) {
		this.symbol = symbol;
		this.pos = -1;
		this.line = -1;
		this.column = -1;
	}

	public Token(String symbol, int pos, int line, int column) {
		this.symbol = symbol;
		this.pos = pos;
		this.line = line;
		this.column = column;
	}

	public String getSymbol() {
		return symbol;
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
}
