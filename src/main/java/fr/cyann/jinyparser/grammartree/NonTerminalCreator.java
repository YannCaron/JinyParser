package fr.cyann.jinyparser.grammartree;
/**
 * Copyright (C) 14/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The TerminalProduction class definition.<br>
 */
public class NonTerminalCreator extends NonTerminalAggregator {

	/**
	 * {@inheritDoc}
	 */
	public NonTerminalCreator(String fieldName, GrammarElement decorated) {
		super(fieldName, decorated);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean lookahead(GrammarContext context) {
		return decorated.lookahead(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean parse(GrammarContext context) {
		boolean res = super.parse(context);

		if (res) {
			context.incorporateLastPending();
		}

		return res;
	}

}
