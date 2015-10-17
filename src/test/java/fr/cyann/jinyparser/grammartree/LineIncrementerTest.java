package fr.cyann.jinyparser.grammartree;

import junit.framework.TestCase;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.*;

/**
 * Copyright (C) 17/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

public class LineIncrementerTest extends TestCase {

    public void testLookahead() throws Exception {
        String source = "a\na\na";
        GrammarContext context = new GrammarContext(source);

        GrammarElement grammar = repeat(choice(word("a"), lineIncrementer(word("\n"))));

        assertTrue(grammar.lookahead(context));
        assertEquals(1, context.getLine());
    }

    public void testParse() throws Exception {
        String source = "a\na\na";
        GrammarContext context = new GrammarContext(source);

        GrammarElement grammar = repeat(choice(word("a"), lineIncrementer(word("\n"))));

        assertTrue(grammar.parse(context));
        assertEquals(3, context.getLine());
    }
}