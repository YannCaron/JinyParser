package fr.cyann.jinyparser.grammartree;

import java.util.*;

/**
 * Copyright (C) 07/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The GrammarNode class. An abstract class for all grammar nodes (compounds one),
 */
@SuppressWarnings("WeakerAccess")
public abstract class GrammarNode extends GrammarElement implements Iterable<GrammarElement> {

    final List<GrammarElement> children;

    /**
     * {@inheritDoc}
     */
    public GrammarNode(GrammarElement[] children) {
        this.children = new ArrayList<GrammarElement>();

        for (GrammarElement child : children) {
            if (child instanceof Recursive) {
                this.children.add(new Link((Recursive) child, this));
            } else {
                child.setParent(this);
                this.children.add(child);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<GrammarElement> iterator() {
        return children.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void depthFirstPush(Stack<GrammarElement> stack) {
        int pos = stack.size();

        for (GrammarElement element : this.children) {
            stack.add(pos, element);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void breadthFirstAdd(Queue<GrammarElement> queue) {
        for (GrammarElement element : this.children) {
            queue.add(element);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean replace(GrammarElement element, GrammarElement by) {
        boolean result = false;
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).equals(element)) {
                children.set(i, by);
                result = true;
            }
        }
        return result;
    }

    /**
     * Get the next element of specified grammar element in the children list.
     *
     * @param element the grammar element to search.
     * @return the next grammar element in the children list.
     */
    public GrammarElement getNextOf(GrammarElement element) {
        int index = children.indexOf(element);
        return children.get(index + 1);
    }

    /**
     * Remove the element of the list.
     *
     * @param element the element to remove
     */
    public void remove(GrammarElement element) {
        children.remove(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " {" +
                "childrenCount=" + children.size() +
                "}";
    }
}
