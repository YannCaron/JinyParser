package fr.cyann.jinyparser.grammartree.core;/**
 * Copyright (C) 09/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import java.util.Queue;
import java.util.Stack;

/**
 * The TreeIterable definition.
 */
public interface TreeIterable<E> {

	/**
	 * Get the parent node of the tree element.
	 *
	 * @return the parent node.
	 */
	E getParent();

	/**
	 * Abstract method to implement for tree iteration in depth first order.<br>
	 * Push the elements (in inverse order) to the stack.
	 *
	 * @param stack the stack to push the next grammar element on.
	 */
	void depthFirstPush(Stack<E> stack);

	/**
	 * Abstract method to implement for tree iteration in breadth first order.<br>
	 * Add the element to the queue.
	 *
	 * @param queue the queue to add the next grammar element on.
	 */
	void breadthFirstAdd(Queue<E> queue);

	/**
	 * Get the ascending order iterable of the grammar element.
	 *
	 * @return
	 */
	Iterable<E> ascendingSearch();

	/**
	 * Get the depth first order iterable of the grammar element.
	 *
	 * @return the iterable.
	 */
	Iterable<E> depthFirstSearch();

	/**
	 * Get the breadth first order iterable of the rammar element.
	 *
	 * @return the iterable.
	 */
	Iterable<E> breadthFirstSearch();

}
