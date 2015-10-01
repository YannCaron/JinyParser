package fr.cyann.jinyparser.grammar;/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The Repeat definition.
 */
public class Repeat extends GrammarDecorator {

	public Repeat(GrammarElement decored) {
		super(decored);
	}

	/**
	 * The backtracking method. Use a lookahead to find if following term / grammar is valid.
	 *
	 * @param context the parsing context that contains all necessary resources to the parsing (iterators, flags and so on).
	 * @return true if lookahead succeed, false otherwise.
	 */
	@Override
	public boolean lookahead(GrammarContext context) {
		return decorated.lookahead(context);
	}

	/**
	 * The parsing method.
	 *
	 * @param context the parsing context that contains all necessary resources to the parsing (iterators, flags and so on).
	 * @return true if parsing succeed, false otherwise.
	 */
	@Override
	public boolean parse(GrammarContext context) {

		if (!decorated.parse(context)) {
			return false;
		}

		while (decorated.parse(context)) {
		}

		return true;
	}
}
