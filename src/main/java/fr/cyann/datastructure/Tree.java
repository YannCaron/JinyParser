/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.cyann.datastructure;

/**
 * Copyright (C) 18/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/
public interface Tree<E, T extends Tree<E, T>> extends Iterable<T> {

	/**
	 * Get the element.
	 *
	 * @return the element.
	 */
	E getHead();

	/**
	 * Return if tree node has parent. If not, it is the root of the tree.
	 *
	 * @return true if tree node has parent.
	 */
	boolean hasParent();

	/**
	 * Get the tree node element.
	 *
	 * @return the tree node element.
	 */
	T getParent();

	/**
	 * Set the tree node element.
	 *
	 * @param parent the tree node element.
	 */
	void setParent(T parent);

	/**
	 * Get the this object with the right type (avoid cast on subclass).
	 *
	 * @return the object with the right type.
	 */
	T getThis();

	/**
	 * Get the root element by bubbling the tree.
	 *
	 * @return the root element.
	 */
	T getRoot();

	/**
	 * Get the number of sub-tree contained by the tree.
	 *
	 * @return the number of sub-tree.
	 */
	int size();

	/**
	 * Add sub-element to the tree node.
	 *
	 * @param leaf the element to add.
	 */
	void addLeaf(E leaf);

	/**
	 * Replace the sub-element by another.
	 *
	 * @param that the element to be replaced.
	 * @param by the element to use to replace.
	 */
	void replace(T that, T by);

	/**
	 * Make a pretty print of the tree.
	 *
	 * @param increment the current incrementation factor.
	 * @return the pretty print string representation of the tree.
	 */
	String printTree(int increment);

}
