package fr.cyann.jinyparser.grammar;/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The Choice class. A compound grammar node that choosing among its children nodes to determine the appropriate grammar.<br>
 * Run as an <b>or</b> operator (BNF:+ sign); check if this or this or this is the appropriate grammar.<br>
 * One of the most important node in the parser tree.<br>
 */
public class Choice extends GrammarNode {

	/** {@inheritDoc} */
	@Override
	public boolean lookahead(GrammarContext context) {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean parse(GrammarContext context) {
		for (GrammarElement child : this) {
			if (child.parse(context)) {
				return true;
			}
		}

		return false;
	}
}
