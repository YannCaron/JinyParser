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
 * The StringLookaheadIterator class definition.<br>
 * Useful to truncate a string into sequence of character. Give the backtracking ability to standard iterator.
 */
public class StringLookaheadIterator implements LookaheadIterator<Character> {

	private static final char EOS = Character.MIN_VALUE;
	private final String string;
	private final Stack<Integer> positions;
	private int position;

	/**
	 * Default constructor.
	 * @param string the source code to parse.
	 */
	public StringLookaheadIterator(String string) {
		this.string = string;
		position = 0;
		positions = new Stack<Integer>();
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasNext() {
		return position + 1 < string.length();
	}

	/**
	 * Tell if iteration is terminated.
	 *
	 * @return true if iteration is terminated.
	 */
	public boolean isTerminated() {
		return position == string.length();
	}

	/** {@inheritDoc} */
	@Override
	public Character current() {
		if (position < string.length()) {
			return string.charAt(position);
		} else {
			return EOS;
		}
	}

	/** {@inheritDoc} */
	@Override
	public void next() {
		if (hasNext()) {
			position++;
		} else {
			position = string.length();
		}
	}

	/** {@inheritDoc} */
	@Override
	public void mark() {
		positions.push(position);
	}

	/** {@inheritDoc} */
	@Override
	public void rollback() {
		position = positions.pop();
	}

	/** {@inheritDoc} */
	@Override
	public void resume() {
		positions.pop();
	}

	/**
	 * Give the string representation of the object.
	 * @return the string representation.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("StringLookaheadIterator:\n\t");
		sb.append(string);
		sb.append("\n\t");
		for (int i = 0; i < position; i++) sb.append('-');
		sb.append("^\n");
		sb.append("current: [");
		sb.append(current());
		sb.append(']');
		return sb.toString();
	}
}
