package fr.cyann.jinyparser.tree;/**
 * Copyright (C) 09/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

/**
 * The DepthFirstIterator definition.
 */
public class DepthFirstIterator<E extends TreeIterable<E>> implements Iterator<E> {

	private final Stack<E> stack;
	private final Set<E> iterated;

	/**
	 * Default constructor.
	 *
	 * @param root the element of the tree to start with.
	 */

	public DepthFirstIterator(E root) {
		stack = new Stack<E>();
		iterated = new HashSet<E>();
		stack.push(root);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return !stack.empty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E next() {
		E current = stack.pop();

		if (!iterated.contains(current)) {
			iterated.add(current);

			current.depthFirstPush(stack);
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
