package fr.cyann.jinyparser.grammar;/**
 * Copyright (C) 07/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.visitor.Context;

/**
 * The Term definition.
 */
public class Term extends GrammarLeaf {

	final String term;

	public Term(String term) {
		this.term = term;
	}

	@Override
	public boolean lookahead(Context context) {
		return parse(context);
	}

	@Override
	public boolean parse(Context context) {
		return false;
	}
}
