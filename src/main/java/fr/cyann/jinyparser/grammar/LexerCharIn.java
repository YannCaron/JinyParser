package fr.cyann.jinyparser.grammar;/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The LexerCharIn definition.
 */
public class LexerCharIn extends GrammarLeaf {


	private final String characters;

	/**
	 * Default constructor.
	 * @param characters
	 */
	LexerCharIn(String characters) {
		this.characters = characters;
	}

	/**  {@inheritDoc} */
	@Override
	protected boolean isTerm(GrammarContext context) {
		char current = context.currentChar().charValue();
		return characters.indexOf(current) != -1;
	}

	/**  {@inheritDoc} */
	@Override
	protected void action(GrammarContext context) {
		context.nextChar();
	}
}
