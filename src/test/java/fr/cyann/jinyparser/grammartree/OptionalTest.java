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

public class OptionalTest extends TestCase {

    public void testLookahead() throws Exception {

        String source = "a";

	    assertFalse(sequence(word("a"), word("b")).lookahead(new GrammarContext(source)));
	    assertTrue(sequence(word("a"), zeroOrOne(word("b"))).lookahead(new GrammarContext(source)));

    }

    public void testParse() throws Exception {

        String source = "a";

	    assertFalse(sequence(word("a"), word("b")).parse(new GrammarContext(source)));
	    assertTrue(sequence(word("a"), zeroOrOne(word("b"))).parse(new GrammarContext(source)));

    }
}