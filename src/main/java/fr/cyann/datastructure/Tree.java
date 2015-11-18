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

	E getHead();

	boolean hasParent();

	T getParent();

	void setParent(T parent);

	T getThis();

	T getRoot();

	int size();

	void addLeaf(E leaf);

	void replace(T that, T by);

	String printTree(int increment);

}
