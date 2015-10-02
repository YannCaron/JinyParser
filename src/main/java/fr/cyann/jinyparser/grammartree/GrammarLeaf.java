package fr.cyann.jinyparser.grammartree;/**
 * Copyright (C) 07/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The GrammarLeaf definition.
 */
abstract class GrammarLeaf extends GrammarElement {

    /**
     * Test if source code is the term corresponding to grammar.
     * @param context the grammar resources.
     * @return true if the term correspond to grammar.
     */
    protected abstract boolean isTerm(GrammarContext context);

    /**
     * What to do when term is found (usually go to next).
     * @param context the grammar resources.
     */
    protected abstract void action(GrammarContext context);

    /**  {@inheritDoc} */
    @Override
    protected boolean lookahead(GrammarContext context) {
        return isTerm(context);
    }

    /**  {@inheritDoc} */
    @Override
    public boolean parse(GrammarContext context) {
        boolean ret = isTerm(context);
        if (ret) action(context);
        return ret;
    }
}
