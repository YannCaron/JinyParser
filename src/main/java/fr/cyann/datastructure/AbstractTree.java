/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.cyann.datastructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Copyright (C) 18/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/
public abstract class AbstractTree<E> implements Tree<E> {

    private static final int IDENT = 2;

    // attributes
    private final E head;
    private Tree<E> parent;

    // constructor

    /**
     * Default constructor.
     *
     * @param head the element value.
     */
    public AbstractTree(E head) {
        this.head = head;
    }

    // hash code and equals

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractTree<?> that = (AbstractTree<?>) o;

        return !(head != null ? !head.equals(that.head) : that.head != null);

    }

    @Override
    public int hashCode() {
        return head != null ? head.hashCode() : 0;
    }


    // property

    /**
     * {@inheritDoc}
     */
    @Override
    public E getHead() {
        return head;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasParent() {
        return parent != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tree<E> getParent() {
        return parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setParent(Tree<E> parent) {
        this.parent = parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tree<E> getRoot() {
        if (parent != null) {
            return parent.getRoot();
        }
        return this;
    }

    /**
     * Get the internal collection of the concrete class.
     *
     * @return the internal collection.
     */
    // TODO: Replace with add / remove / iterator
    protected abstract Collection<Tree<E>> getCollection();

    // method

    /**
     * Construct a sub-tree and add it to the current tree node.
     *
     * @param leaf the leaf to add.
     */
    protected void addLeaf(Tree<E> leaf) {
        leaf.setParent(this);
        getCollection().add(leaf);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void replace(Tree<E> that, Tree<E> by) {
        that.setParent(null);
        getCollection().remove(that);
        addLeaf(by);
    }

    /**
     * Insert sub-tree between this and it's parent.
     *
     * @param in the sub-tree to insert.
     */
    public void insert(Tree<E> in) {
        Tree<E> p = this.parent;
        if (p != null) {
            p.replace(this, in);
        }

        in.setParent(p);
        this.setParent(in);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Tree<E>> iterator() {
        return getCollection().iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return getCollection().size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return printTree(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String printTree(int increment) {
        String s;
        String inc = "";
        for (int i = 0; i < increment; ++i) {
            inc = inc + " ";
        }
        s = inc + head;
        for (Tree<E> leaf : getCollection()) {
            s += "\n" + leaf.printTree(increment + IDENT);
        }
        return s;
    }

    private void buildList(AbstractTree<E> node, List<E> listAcc, List<List<E>> listListAcc) {
        listAcc.add(node.getHead());

        if (node.size() == 0) listListAcc.add(listAcc);

        for (Tree<E> c : node.getCollection()) {
            AbstractTree<E> child = (AbstractTree<E>) c;
            ArrayList<E> list = new ArrayList<E>(listAcc);
            buildList(child, list, listListAcc);
        }

    }

    /**
     * Get the tree to a table data structure where rows are a depth first traversal of each branches.
     *
     * @return the table representation of the tree.
     */
    public List<List<E>> toTable() {
        List<List<E>> lists = new ArrayList<List<E>>();

        buildList(this, new ArrayList<E>(), lists);

        return lists;
    }

    /**
     * Get the tree to a table data structure where rows are a depth first traversal of each branches.<br>
     * Exclude the root from the computation.
     *
     * @return the table representation of the tree.
     */
    public List<List<E>> toTableNoRoot() {
        List<List<E>> lists = new ArrayList<List<E>>();

        for (Tree<E> c : getCollection()) {
            AbstractTree<E> child = (AbstractTree<E>) c;
            ArrayList<E> list = new ArrayList<E>();
            buildList(child, list, lists);
        }

        return lists;
    }
}
