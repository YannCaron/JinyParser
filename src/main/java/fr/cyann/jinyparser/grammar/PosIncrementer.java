package fr.cyann.jinyparser.grammar;/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The PosIncrementer class. Each time the decorated grammar is parsed successfully, the current source position in the context is incremented.
 */
public class PosIncrementer extends GrammarDecorator {

	/** {@inheritDoc} */
	public PosIncrementer(GrammarElement decorated) {
		super(decorated);
	}

	/** {@inheritDoc} */
	@Override
	public boolean lookahead(GrammarContext context) {
		return decorated.lookahead(context);
	}

	/** {@inheritDoc} */
	@Override
	public boolean parse(GrammarContext context) {
		boolean result = decorated.parse(context);
		if (result) {
			context.newLine();
		}
		return result;
	}
}
