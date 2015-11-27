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
public abstract class GrammarDecorator extends GrammarElement {

    /**
     * The decorated object.
     */
    protected GrammarElement decorated;

    /**
     * Default and mandatory constructor. Decorated object if final.
     *
     * @param decorated the decorated object.
     */
    protected GrammarDecorator(GrammarElement decorated) {
        setDecorated(decorated);
    }

    // region equals and hashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GrammarDecorator that = (GrammarDecorator) o;

        return !(decorated != null ? !decorated.equals(that.decorated) : that.decorated != null);

    }

    @Override
    public int hashCode() {
        return decorated != null ? decorated.hashCode() : 0;
    }

    // endregion

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean replace(GrammarElement element, GrammarElement by) {
        if (!decorated.equals(element)) return false;
        decorated = by;
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
    GrammarDecorator setDecorated(GrammarElement decorated) {
        if (decorated instanceof Recursive) {
            Link link = new Link((Recursive) decorated, this);
            link.setParent(this);
            this.decorated = link;
        } else {
            if (decorated != null)
                decorated.setParent(this);
            this.decorated = decorated;
        }

        return this;
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
