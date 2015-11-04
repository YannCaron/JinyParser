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
public class CharIn extends GrammarLeaf {

	protected final StringBuilder characters;
	protected final StringBuilder bnf;

	/**
	 * Default constructor.
	 */
	private CharIn() {
		characters = new StringBuilder();
		bnf = new StringBuilder();
	}

	/**
	 * Default constructor.
	 *
	 * @param characters the list of character to test.
	 */
    public CharIn(String characters) {
	    this();
	    add(characters);
    }

	/**
	 * Constructor with character range e.g. [0-1], [a-z], [A-Z].<br>
	 * Follow the ascii table.
	 *
	 * @param start the first character of the range.
	 * @param end   the last character (included) of the range.
	 */
	public CharIn(char start, char end) {
		this();
		add(start, end);
	}

	/**
	 * Append characters to the existing character set.
	 *
	 * @param characters the characters to add.
	 * @return this.
	 */
	public CharIn add(String characters) {
		this.characters.append(characters);
		bnf.append(characters);

		return this;
	}

	/**
	 * Append character range to the existing character set.
	 *
	 * @param start the first character of the range.
	 * @param end   the last character (included) of the range.
	 * @return this.
	 */
	public CharIn add(char start, char end) {
		for (char c = start; c <= end; c++) {
			this.characters.append(c);
		}
		bnf.append(start);
		bnf.append("-");
		bnf.append(end);

		return this;
	}

	/**
	 * Check if character feet the criteria.
	 *
	 * @param context the grammar context
	 * @return true if the character feet the criteria.
	 */
	protected boolean isTerm(GrammarContext context) {
		char current = context.currentChar();
		return characters.toString().indexOf(current) != -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean lookahead(GrammarContext context) {
		boolean result = isTerm(context);
        if (result) context.nextCharLookahead();
        return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean parse(GrammarContext context) {
		boolean result = isTerm(context);
        if (result) context.nextCharParser();
        return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void buildBnf(BnfContext context) {
		if (characters.length() <= 1) {
			context.append("'");
			context.append(bnf.toString());
			context.append("'");
		} else {
			context.append("[");
			for (char chr : bnf.toString().toCharArray()) {
				context.append(String.valueOf(chr));
			}
			context.append("]");
		}
	}

}
