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
public interface Tree<T> extends Iterable<Tree<T>> {

	T getHead();

	boolean hasParent();

	Tree<T> getParent();

	void setParent(Tree<T> parent);

	Tree<T> getRoot();

	Tree<T> addLeaf(T leaf);

	void replace(Tree<T> that, Tree<T> by);

	String printTree(int increment);

	int size();

}
