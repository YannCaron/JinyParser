/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.cyann.datastructure;

import java.util.HashSet;
import java.util.Set;

/**
 * Copyright (C) 18/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/
public class UniqueListTree<E> extends ListTree<E> {

    final Set<Tree<E>> existings;

    /**
     * Default constructor.
     *
     * @param head the element.
     */
    public UniqueListTree(E head) {
        super(head);
        existings = new HashSet<Tree<E>>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueListTree<E> addLeaf(E leaf) {
        UniqueListTree<E> tree = new UniqueListTree<E>(leaf);

        if (!existings.contains(tree)) {
            super.addLeaf(tree);
            existings.add(tree);
            return tree;
        } else {
            return get(leafs.indexOf(tree));
        }
    }

    /**
     * Get the element in order of its insertion.
     *
     * @param index the index to search.
     * @return the element.
     */
    public UniqueListTree<E> get(int index) {
        return (UniqueListTree<E>) leafs.get(index);
    }

}
