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
	public Repeat(GrammarElement decorated) {
		super(decorated);
	}

	/** {@inheritDoc} */
	@Override
	protected boolean lookahead(GrammarContext context) {
        boolean result = decorated.lookahead(context);

        while (decorated.lookahead(context)) ;

        return result;
    }

	/** {@inheritDoc} */
	@Override
	protected boolean parse(GrammarContext context) {

		boolean lookaheadResult = launchLookahead(context, decorated);

		if (!lookaheadResult) return false;

		while (lookaheadResult) {
			decorated.parse(context);
			lookaheadResult = launchLookahead(context, decorated);
		}

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void buildBnf(BnfContext context) {
		context.append("(");
		decorated.buildBnf(context);
		context.append(")+");
	}

}
