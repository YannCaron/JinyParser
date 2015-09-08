package fr.cyann.jinyparser.utils;/**
 * Copyright (C) 07/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The StringBacktrackingIterator definition.
 */
public class StringBacktrackingIterator implements BacktrackingIterator<Character> {

	private final String source;
	private int index;

	public StringBacktrackingIterator(String source) {
		this.source = source;
		index = 0;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		StringBacktrackingIterator that = (StringBacktrackingIterator) o;

		return (index != that.index) && !(source != null ? !source.equals(that.source) : that.source != null);

	}

	@Override
	public Character current() {
		return source.charAt(index);
	}

	@Override
	public boolean hasNext() {
		return index + 1 < source.length();
	}

	@Override
	public Character next() {
		if (!hasNext()) return DONE;
		index++;
		return source.charAt(index);
	}

	@Override
	public boolean hasPrevious() {
		return index > 0;
	}

	@Override
	public Character previous() {
		if (!hasPrevious()) return DONE;
		index--;
		return source.charAt(index);
	}

	@Override
	public void remove() {
		// do nothing
	}
}
