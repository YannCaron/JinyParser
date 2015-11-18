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
public class ArrayTree<E> extends AbstractTree<E, ArrayTree<E>> {

	protected final List<ArrayTree<E>> leafs;

	public ArrayTree(E head) {
		super(head);
		leafs = new ArrayList<ArrayTree<E>>();
	}

	@Override
	public ArrayTree<E> getThis() {
		return this;
	}

	@Override
	protected Collection<ArrayTree<E>> getCollection() {
		return leafs;
	}

	@Override
	public void addLeaf(E leaf) {
		ArrayTree<E> tree = new ArrayTree<E>(leaf);
		super.addLeaf(tree);
	}

	public ArrayTree<E> get(int index) {
		return leafs.get(index);
	}

}
