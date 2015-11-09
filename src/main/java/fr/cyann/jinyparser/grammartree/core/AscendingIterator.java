package fr.cyann.jinyparser.grammartree.core;/**
 * Copyright (C) 09/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import java.util.Iterator;

/**
 * The AscendingIterator definition.
 */
public class AscendingIterator<E extends TreeIterable<E>> implements Iterator<E> {

	private E current;

	/**
	 * Default constructor.
	 *
	 * @param root the element of the tree to start with.
	 */

	public AscendingIterator(E root) {
		this.current = root;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return current != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E next() {
		E element = current;
		current = current.getParent();
		return element;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() {

	}
}
