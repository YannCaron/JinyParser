package fr.cyann.jinyparser.grammartree;/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.lexem.LexemType;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.*;

/**
 * The LexemCreator class. Grammar element that help to manage the separator characters between two real grammar.
 */
@SuppressWarnings("WeakerAccess")
public class LexemCreator extends LexemCreatorCore {

	/**
	 * A default separator grammar.<br>
	 * <i>' ' | '\ProcessedGrammar' | '\0' | '\n'(new line)</i>
	 */
	private static final GrammarElement DEFAULT_SEPARATOR =
			zeroOrOne(choice(charIn(" \t\0"), lineIncrementer(charIn("\n"))));

	private final GrammarElement separator;

	/**
	 * The default constructor with all parameters.
	 * @param decorated the object to decorate.
	 * @param lexemType the lexem type to produce.
	 * @param separator the separator peace of grammar that will be checked (if exists) at the beginning of decorated grammar.
	 */
	public LexemCreator(GrammarElement decorated, LexemType lexemType, GrammarElement separator) {
		super(decorated, lexemType);
		this.separator = separator;
	}

	/**
	 * Optional constructor.
	 *
	 * @param decorated the object to decorate.
	 * @param lexemType the lexem type to produce.
	 */
	public LexemCreator(GrammarElement decorated, LexemType lexemType) {
		this(decorated, lexemType, DEFAULT_SEPARATOR);
	}

	/** {@inheritDoc} */
	@Override
	protected boolean lookahead(GrammarContext context) {
		return separator.lookahead(context) && super.lookahead(context) && separator.lookahead(context);
	}

	/** {@inheritDoc} */
	@Override
	protected boolean parse(GrammarContext context) {
		return separator.parse(context) && super.parse(context) && separator.parse(context);
	}

}
