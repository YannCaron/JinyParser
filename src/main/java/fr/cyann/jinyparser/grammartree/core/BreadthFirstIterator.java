package fr.cyann.jinyparser.grammartree.core;/**
 * Copyright (C) 09/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import java.util.*;

/**
 * The DepthFirstIterator definition.
 */
public class BreadthFirstIterator<E extends TreeIterable<E>> implements Iterator<E> {

	private final Queue<E> queue;
	private final Set<E> iterated;

	/**
	 * Default constructor.
	 *
	 * @param root the element of the tree to start with.
	 */
	public BreadthFirstIterator(E root) {
		queue = new ArrayDeque<E>();
		iterated = new HashSet<E>();
		queue.add(root);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return !queue.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E next() {
		E current = queue.poll();

		if (!iterated.contains(current)) {
			iterated.add(current);

			current.breadthFirstAdd(queue);
		}

		return current;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() {

	}
}
