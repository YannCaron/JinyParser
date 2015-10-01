package fr.cyann.jinyparser.grammar;/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The SeparatorsManager definition.
 */
public class SeparatorsManager extends GrammarDecorator {

	public static final GrammarElement DEFAULT_SEPARATOR =
			new Choice()
					.add(new LexerCharIn(" \t\0"))
					.add(new LineIncrementer(new LexerCharIn("\n")));

	private final GrammarElement separator;

	public SeparatorsManager(GrammarElement decorated, GrammarElement separator) {
		super(decorated);
		this.separator = separator;
	}

	/**
	 * {@inheritDoc}
	 */
	public SeparatorsManager(GrammarElement decorated) {
		this(decorated, DEFAULT_SEPARATOR);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean lookahead(GrammarContext context) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean parse(GrammarContext context) {
		separator.parse(context);
		return decorated.parse(context);
	}
}
