package fr.cyann.jinyparser.grammartree;/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The Optional class. Used to indicate an optional part of the grammar. The part if evaluated if necessary but parsing is always successful even if grammar does not exists.
 */
public class Optional extends GrammarDecorator {

	/** {@inheritDoc} */
	Optional(GrammarElement decorated) {
		super(decorated);
	}

	/** {@inheritDoc} */
	@Override
	protected boolean lookahead(GrammarContext context) {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public boolean parse(GrammarContext context) {
		decorated.parse(context);
		return true;
	}

	/**
	 * Give the BNF representation of the grammar expression.
	 *
	 * @return the BNF representation.
	 */
	@Override
	public String toString() {
		String deco = decorated.toString();
		if ((deco.charAt(0) == '(' && deco.charAt(deco.length() - 1) == ')')) {
			deco = deco.substring(1, deco.length() - 2);
		}
		return "[" + deco + "]";
	}
}
