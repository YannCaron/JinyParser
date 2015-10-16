package fr.cyann.jinyparser.parsetree;/**
 * Copyright (C) 28/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.lexem.Lexem;

/**
 * The ParsemElement definition.
 */
@SuppressWarnings("WeakerAccess")
public abstract class ParsemElement {

	private final Lexem lexem;

	public ParsemElement(Lexem lexem) {
		this.lexem = lexem;
	}

	public Lexem getLexem() {
		return lexem;
	}

	public abstract void aggregate(String fieldName, ParsemElement element);

}
