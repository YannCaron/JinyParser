package fr.cyann.jinyparser.grammartree;

import junit.framework.TestCase;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.freeWord;
import static fr.cyann.jinyparser.grammartree.GrammarFactory.word;

/**
 * Copyright (C) 17/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

public class WordTest extends TestCase {

    public void testLookahead() throws Exception {
        String source = "abc";

        assertTrue(word("abc").lookahead(new GrammarContext(source)));
        assertFalse(word("abd").lookahead(new GrammarContext(source)));
        assertTrue(word("ab").lookahead(new GrammarContext(source)));

	    source = "AbC";

	    assertFalse(word("abc").lookahead(new GrammarContext(source)));
	    assertTrue(freeWord("abc").lookahead(new GrammarContext(source)));

    }

    public void testParse() throws Exception {
        String source = "abc";

        assertTrue(word("abc").parse(new GrammarContext(source)));
        assertFalse(word("abd").parse(new GrammarContext(source)));
        assertTrue(word("ab").parse(new GrammarContext(source)));

	    source = "AbC";
	    assertFalse(word("abc").parse(new GrammarContext(source)));
	    assertTrue(freeWord("abc").parse(new GrammarContext(source)));

    }
}