package fr.cyann.jinyparser.utils;/**
 * Copyright (C) 08/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import java.util.Stack;

/**
 * The StringLookaheadIterator class definition.
 */
public class StringLookaheadIterator implements LookaheadIterator<Character> {

	public static final char EOS = Character.MIN_VALUE;
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
		if (index < string.length()) {
			return string.charAt(index);
		} else {
			return EOS;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void next() {
/*		if (!hasNext())
			throw new IndexOutOfBoundsException(
					MultilingualMessage.create("String bound reached [%d] !")
							.translate(Locale.FRENCH, "La fin de la chaîne de caractère à été atteinte [%d] !")
							.setArgs(string.length()).toString());*/
		if (hasNext()) {
			index++;
		} else {
			index = string.length();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void mark() {
		indexes.push(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rollback() {
		index = indexes.pop();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resume() {
		indexes.pop();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("StringLookaheadIterator:\n\t");
		sb.append(string);
		sb.append("\n\t");
		for (int i = 0; i < index; i++) sb.append('-');
		sb.append("^\n");
		sb.append("current: ");
		sb.append(current());
		return sb.toString();
	}
}
