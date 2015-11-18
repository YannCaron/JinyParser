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
public abstract class AbstractTree<E, T extends Tree<E, T>> implements Tree<E, T> {

	private static final int indent = 2;
	// attributes
	protected final E head;
	protected T parent;

	// constructor
	public AbstractTree(E head) {
		this.head = head;
	}

	// property
	@Override
	public E getHead() {
		return head;
	}

	@Override
	public boolean hasParent() {
		return parent != null;
	}

	@Override
	public T getParent() {
		return parent;
	}

	@Override
	public void setParent(T parent) {
		this.parent = parent;
	}

	@Override
	public T getRoot() {
		if (parent != null) {
			return getThis();
		}
		return parent.getRoot();
	}

	protected abstract Collection<T> getCollection();

	// method
	public void addLeaf(T leaf) {
		leaf.setParent(getThis());
		getCollection().add(leaf);
	}

	@Override
	public void replace(T that, T by) {
		that.setParent(null);
		getCollection().remove(that);
		addLeaf(by);
	}

	public T insert(T in) {
		T p = this.parent;
		if (p != null) {
			p.replace(getThis(), in);
		}

		in.setParent(p);
		this.setParent(in);
		return in;
	}

	@Override
	public Iterator<T> iterator() {
		return getCollection().iterator();
	}

	@Override
	public int size() {
		return getCollection().size();
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
		for (T leaf : getCollection()) {
			s += "\n" + leaf.printTree(increment + indent);
		}
		return s;
	}
}
