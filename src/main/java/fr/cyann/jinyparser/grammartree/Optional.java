package fr.cyann.jinyparser.grammartree;/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The Optional class. Used to indicate an zeroOrOne part of the grammar. The part if evaluated if necessary but parsing is always successful even if grammar does not exists.
 */
public class Optional extends GrammarDecorator {

	/** {@inheritDoc} */
	public Optional(GrammarElement decorated) {
		super(decorated);
	}

	/** {@inheritDoc} */
	@Override
	protected boolean lookahead(GrammarContext context) {
		decorated.lookahead(context);
		return true;
	}

	/** {@inheritDoc} */
	@Override
	protected boolean parse(GrammarContext context) {
		boolean lookaheadResult = launchLookahead(context, decorated);

		if (lookaheadResult) {
			decorated.parse(context);
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void buildBnf(BnfContext context) {
		if (decorated instanceof Repeat) {
			context.append("(");
			((Repeat) decorated).decorated.buildBnf(context);
			context.append(")*");
		} else {
			context.append("(");
			decorated.buildBnf(context);
			context.append(")?");
		}
	}

}
