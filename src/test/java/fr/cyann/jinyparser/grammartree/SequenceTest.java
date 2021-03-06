package fr.cyann.jinyparser.grammartree;

import junit.framework.TestCase;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.sequence;
import static fr.cyann.jinyparser.grammartree.GrammarFactory.word;

/**
 * Copyright (C) 17/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

public class SequenceTest extends TestCase {

    public void testLookahead() throws Exception {

	    GrammarElement sequence = sequence(word("a"), word("b"));

        assertTrue(sequence.lookahead(new GrammarContext("ab")));
        assertTrue(sequence.lookahead(new GrammarContext("abc")));
        assertFalse(sequence.lookahead(new GrammarContext("ac")));

    }

    public void testParse() throws Exception {

	    GrammarElement sequence = sequence(word("a"), word("b"));

        assertTrue(sequence.parse(new GrammarContext("ab")));
        assertTrue(sequence.parse(new GrammarContext("abc")));
        assertFalse(sequence.parse(new GrammarContext("ac")));

    }
}