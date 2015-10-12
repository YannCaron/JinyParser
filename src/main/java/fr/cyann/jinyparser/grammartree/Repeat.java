package fr.cyann.jinyparser.grammartree;/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The Repeat class. Repeat the decorated grammar until it does not parse anymore.
 */
public class Repeat extends GrammarDecorator {

	/** {@inheritDoc} */
	Repeat(GrammarElement decorated) {
		super(decorated);
	}

	/** {@inheritDoc} */
	@Override
	protected boolean lookahead(GrammarContext context) {
		return decorated.lookahead(context);
	}

	/** {@inheritDoc} */
	@Override
	protected boolean parse(GrammarContext context) {

		boolean result = decorated.parse(context);

		if (!result) return false;

		while (result) {
			result = decorated.parse(context);
		}

		return true;
	}

}
