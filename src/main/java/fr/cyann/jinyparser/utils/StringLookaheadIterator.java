package fr.cyann.jinyparser.utils;/**
 * Copyright (C) 08/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import java.util.Locale;
import java.util.Stack;

/**
 * The StringLookaheadIterator class definition.
 */
public class StringLookaheadIterator implements LookaheadIterator<Character> {

	private final String string;
	private final Stack<Integer> indexes;
	private int index;

	public StringLookaheadIterator(String string) {
		this.string = string;
		index = 0;
		indexes = new Stack<Integer>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return index + 1 < string.length();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Character current() {
		return string.charAt(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void next() {
		if (!hasNext())
			throw new IndexOutOfBoundsException(
					MultilingualMessage.create("String bound reached [%d] !")
							.translate(Locale.FRENCH, "La fin de la chaîne de caractère à été atteinte [%d] !")
							.setArgs(string.length()).toString());
		index++;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void store() {
		indexes.push(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void restore() {
		index = indexes.pop();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dump() {
		indexes.pop();
	}
}
