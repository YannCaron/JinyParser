/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.cyann.datastructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Copyright (C) 18/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/
public class ArrayTree<E> extends AbstractTree<E> {

	private final List<Tree<E>> leafs;

	/**
	 * Default constructor.
	 *
	 * @param head the element.
	 */
	public ArrayTree(E head) {
		super(head);
		leafs = new ArrayList<Tree<E>>();
	}

	/** {@inheritDoc} */
	@Override
	protected Collection<Tree<E>> getCollection() {
		return leafs;
	}

	/** {@inheritDoc} */
	@Override
	public ArrayTree<E> addLeaf(E leaf) {
		ArrayTree<E> tree = new ArrayTree<E>(leaf);
		super.addLeaf(tree);
		return tree;
	}

	/**
	 * Get the element in order of its insertion.
	 *
	 * @param index the index to search.
	 * @return the element.
	 */
	public ArrayTree<E> get(int index) {
		return (ArrayTree<E>) leafs.get(index);
	}

}
