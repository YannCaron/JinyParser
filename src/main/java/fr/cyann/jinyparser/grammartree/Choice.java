package fr.cyann.jinyparser.grammartree;/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import java.util.Set;

/**
 * The Choice class. A compound grammar node that choosing among its children nodes to determine the appropriate grammar.<br>
 * Run as an <b>or</b> operator (BNF:+ sign); check if this or this or this is the appropriate grammar.<br>
 * One of the most important node in the parser tree.<br>
 */
public class Choice extends GrammarNode {

    /**
     * {@inheritDoc}
     */
    // TODO : Why public is needed ?
    public Choice() {
        super();
    }

    /** {@inheritDoc} */
    Choice(GrammarElement[] children) {
        super(children);
    }

    /** {@inheritDoc} */
    @Override
    protected boolean lookahead(GrammarContext context) {

        for (GrammarElement child : this) {

            context.markChar();
            if (child.lookahead(context)) {
                context.resumeChar();
                return true;
            }
            context.rollbackChar();

        }

        return false;

    }

    /** {@inheritDoc} */
    @Override
    public boolean parse(GrammarContext context) {
        for (GrammarElement child : this) {

            context.markChar();
            boolean lookaheadResult = child.lookahead(context);
            context.rollbackChar();

            if (lookaheadResult) {
                child.parse(context);
                return true;
            }

        }

        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void abstractBuildString(Set<GrammarElement> alreadyBuilt, StringBuilder sb) {
        boolean first = true;
        for (GrammarElement child : this) {
            if (!first) sb.append(" | ");
            first = false;
            child.buildString(alreadyBuilt, sb);
        }
    }

}
