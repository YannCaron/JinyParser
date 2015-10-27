package fr.cyann.jinyparser.utils;/**
 * Copyright (C) 08/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The LookaheadIterator interface definition.
 * Give the ability to iterate on elements and during that, storing some positions to backtrack to them if parsing failed.
 */
@SuppressWarnings("WeakerAccess")
public interface LookaheadIterator<T> {

	/**
	 * Get the current element.
	 *
	 * @return the current element.
	 */
	T current();

	/**
	 * Get the current position.
	 *
	 * @return the current position.
	 */
	int getCurrentPosition();

	/**
	 * Jump to the next element.
	 */
	void next();

	/**
	 * Tell if the next element exists.
	 *
	 * @return true if next element exists.
	 */
	boolean hasNext();

	/**
	 * Store (put) the actual position.
	 */
	void mark();

	/**
	 * Restore (pop) the stored position to the actual one.
	 */
	void rollback();

	/**
	 * Garbage (pop) the stored position.
	 */
	void resume();

}
