package fr.cyann.jinyparser.grammartree;/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The Word definition.
 */
public class Word extends GrammarLeaf {


	private final String word;

	/**
	 * Default constructor.
	 *
	 * @param word the entire word to test.
	 */
	public Word(String word) {
		this.word = word;
	}

	private boolean isTerm(GrammarContext context, boolean build) {
		for (int i = 0; i < word.length(); i++) {
			if (context.isTerminated()) return false;
			char chr = word.charAt(i);
			if (chr != context.currentChar()) return false;
			if (build) {
				context.nextCharParser();
			} else {
				context.nextCharLookahead();
			}
		}

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean lookahead(GrammarContext context) {
		return isTerm(context, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean parse(GrammarContext context) {
		return isTerm(context, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void buildBnf(BnfContext context) {
	}

}
