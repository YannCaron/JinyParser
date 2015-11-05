package fr.cyann.jinyparser.grammartree;/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The CharIn definition.
 */
public class CharNotIn extends CharIn {

	/**
	 * {@inheritDoc}
	 */
	public CharNotIn(String characters) {
		super(characters);
	}

	/**
	 * {@inheritDoc}
	 */
	public CharNotIn(char start, char end) {
		super(start, end);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isTerm(GrammarContext context) {
		char current = context.currentChar();
		return characters.toString().indexOf(current) == -1 && !context.isTerminated();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void buildBnf(BnfContext context) {
		context.append("[^");
		context.append(bnf.toString());
		context.append("]");
	}

	@Override
	public String toString() {
		return "CharNotIn {" +
				"bnf='" + bnf +
				"'}";
	}
}
