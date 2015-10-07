package fr.cyann.jinyparser.grammartree;/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import static fr.cyann.jinyparser.grammartree.GrammarFactory.*;

/**
 * The SeparatorsManager class. Grammar element that help to manage the separator characters between two real grammar.
 */
public class SeparatorsManager extends GrammarDecorator {

	/**
	 * A default separator grammar.<br>
	 * <i>' ' | '\t' | '\0' | '\n'(new line)</i>
	 */
	private static final GrammarElement DEFAULT_SEPARATOR =
			optional(choice("sep").addAll(lexerCharIn(" \t\0"), lineIncrementer(lexerCharIn("\n"))));

	private final GrammarElement separator;

	/**
	 * The default and mandatory constructor.
	 * @param decorated the object to decorate.
	 * @param separator the separator peace of grammar that will be checked (if exists) at the beginning of decorated grammar.
	 */
	private SeparatorsManager(GrammarElement decorated, GrammarElement separator) {
		super(decorated);
		this.separator = separator;
	}

	/** {@inheritDoc} */
	SeparatorsManager(GrammarElement decorated) {
		this(decorated, DEFAULT_SEPARATOR);
	}

	/** {@inheritDoc} */
	@Override
	protected boolean lookahead(GrammarContext context) {
		if (!separator.lookahead(context)) return false;
		return decorated.lookahead(context);
	}

	/** {@inheritDoc} */
	@Override
	public boolean parse(GrammarContext context) {
		if (!separator.parse(context)) return false;
		return decorated.parse(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void toEBNFAbstract(BuildEBNFContext context, StringBuilder buffer) {
		if (!separator.equals(DEFAULT_SEPARATOR)) {
			separator.buildBNF(context, buffer);
			buffer.append(' ');
		}
		decorated.buildBNF(context, buffer);
	}

}
