package fr.cyann.jinyparser.tree;/**
 * Copyright (C) 09/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import java.util.Iterator;
import java.util.Stack;

/**
 * The DepthFirstIterator definition.
 */
public class DepthFirstIterator<E extends TreeIterable<E>> implements Iterator<E> {

	private final PruningStrategy<E> pruningStrategy;
	private final Stack<E> stack;

	/**
	 * Default constructor.
	 *
	 * @param root the element of the tree to start with.
	 * @param pruningStrategy
	 */

	public DepthFirstIterator(E root, PruningStrategy<E> pruningStrategy) {
		this.pruningStrategy = pruningStrategy;
		stack = new Stack<E>();
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

		if (pruningStrategy.shouldContinue(current)) {
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
