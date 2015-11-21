package fr.cyann.jinyparser.grammartree;/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import java.util.Queue;
import java.util.Stack;

/**
 * The GrammarDecorator class. An abstract class for all grammar decorators (add a local parsing functionality).
 */
@SuppressWarnings("WeakerAccess")
public abstract class GrammarDecorator<E extends GrammarElement> extends GrammarElement {

    /**
     * The decorated object.
     */
    E decorated;

    /**
     * Default and mandatory constructor. Decorated object if final.
     *
     * @param decorated the decorated object.
     */
    protected GrammarDecorator(E decorated) {
        if (decorated != null)
            decorated.setParent(this);
        this.decorated = decorated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean replace(GrammarElement element, GrammarElement by) {
        if (!decorated.equals(element)) return false;
        decorated = (E) by;
        return true;
    }

    /**
     * Get the decorated grammar element.
     *
     * @return the decorated grammar element.
     */
    public GrammarElement getDecorated() {
        return decorated;
    }

    /**
     * Set the decorated grammar element.
     *
     * @param decorated the decorated grammar element.
     */
    public void setDecorated(E decorated) {
        this.decorated = decorated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void depthFirstPush(Stack<GrammarElement> stack) {
        stack.push(this.decorated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void breadthFirstAdd(Queue<GrammarElement> queue) {
        queue.add(this.decorated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void buildBnf(BnfContext context) {
        decorated.buildBnf(context);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " {" +
                "decorated=" + decorated.getClass().getSimpleName() +
                "}";
    }

}
