package fr.cyann.jinyparser.grammartree;

/**
 * Copyright (C) 07/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The Sequence class. A compound grammar node that parse sequentially each of its children.<br>
 * Run as an <b>and</b> operator (BNF:[SPACE] sign); check if this followed by this and followed by this parse the source code.<br>
 * One of the most important node in the parser tree.<br>
 */
public class Sequence extends GrammarNode {

    /**
     * {@inheritDoc}
     */
    Sequence() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    Sequence(GrammarElement[] children) {
        super(children);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean lookahead(GrammarContext context) {
        for (GrammarElement child : this) {
            boolean result = child.parse(context);
            if (!result) {
                return false;
            }
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean parse(GrammarContext context) {
        for (GrammarElement child : this) {
            boolean result = child.parse(context);
            if (!result) {
                return false;
            }
        }

        return true;
    }
}
