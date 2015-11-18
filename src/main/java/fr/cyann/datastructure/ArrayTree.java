/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.cyann.datastructure;

import java.util.ArrayList;

/**
 * Copyright (C) 18/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/
public class ArrayTree<T> extends AbstractTree<T> {

	public ArrayTree(T head) {
		super(head, new ArrayList<Tree<T>>());
	}

	@Override
	public ArrayTree<T> addLeaf(T leaf) {
		ArrayTree<T> tree = new ArrayTree<T>(leaf);
		super.addLeaf(tree);
		return tree;
	}

	public ArrayTree<T> get(int index) {
		return (ArrayTree<T>) ((ArrayList<Tree<T>>) leafs).get(index);
	}

}
