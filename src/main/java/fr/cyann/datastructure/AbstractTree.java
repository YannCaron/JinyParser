/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.cyann.datastructure;

import java.util.Collection;
import java.util.Iterator;

/**
 * Copyright (C) 18/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/
public abstract class AbstractTree<T> implements Tree<T> {

	private static final int indent = 2;
	// attributes
	protected final T head;
	protected final Collection<Tree<T>> leafs;
	protected Tree<T> parent;

	// constructor
	public AbstractTree(T head, Collection<Tree<T>> leafs) {
		this.head = head;
		this.leafs = leafs;
	}

	// property
	@Override
	public T getHead() {
		return head;
	}

	@Override
	public boolean hasParent() {
		return parent != null;
	}

	@Override
	public Tree<T> getParent() {
		return parent;
	}

	@Override
	public void setParent(Tree<T> parent) {
		this.parent = parent;
	}

	@Override
	public Tree<T> getRoot() {
		if (parent != null) {
			return this;
		}
		return parent.getRoot();
	}

	// method
	public Tree<T> addLeaf(Tree<T> leaf) {
		leaf.setParent(this);
		leafs.add(leaf);
		return leaf;
	}

	@Override
	public void replace(Tree<T> that, Tree<T> by) {
		that.setParent(null);
		leafs.remove(that);
		addLeaf(by);
	}

	public Tree<T> insert(Tree<T> in) {
		Tree<T> p = this.parent;
		if (p != null) {
			p.replace(this, in);
		}

		in.setParent(p);
		this.setParent(in);
		return in;
	}

	@Override
	public Iterator<Tree<T>> iterator() {
		return leafs.iterator();
	}

	@Override
	public int size() {
		return leafs.size();
	}

	@Override
	public String toString() {
		return printTree(0);
	}

	@Override
	public final String printTree(int increment) {
		String s = "";
		String inc = "";
		for (int i = 0; i < increment; ++i) {
			inc = inc + " ";
		}
		s = inc + head;
		for (Tree<T> leaf : leafs) {
			s += "\n" + leaf.printTree(increment + indent);
		}
		return s;
	}
}
