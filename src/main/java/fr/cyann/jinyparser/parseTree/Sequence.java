package fr.cyann.jinyparser.parseTree;

import fr.cyann.jinyparser.visitor.Context;

/**
 * Copyright (C) 07/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/
public class Sequence extends GrammarNode {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean lookahead(Context context) {
		return false;
	}

	/**
	 * The sequence parsing method
	 *
	 * @param context the parsing context that contains all necessary resources to the parsing (iterators, flags and so on).
	 * @return true if all the children of the sequence succeed. Otherwise, if any failed, return false.
	 */
	@Override
	public boolean parse(Context context) {
		for (GrammarElement child : this) {
			if (!child.parse(context)) {
				return false;
			}
		}

		return true;
	}
}
