package fr.cyann.jinyparser.grammartree;

import fr.cyann.jinyparser.lexem.LexemType;
import junit.framework.TestCase;

import java.util.Arrays;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.*;
import static fr.cyann.jinyparser.testUtils.Utils.lexerToTerms;

/**
 * Copyright (C) 18/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

public class LexemCreatorTest extends TestCase {

    public void testLookahead() throws Exception {

        String source = " a b c ";

        GrammarElement grammar = repeat(lexemRaw(charIn("abc"), LexemType.SYMBOL));
        assertFalse(grammar.lookahead(new GrammarContext(source)));

        grammar = repeat(lexem(charIn("abc"), LexemType.SYMBOL)); // with separator management
        assertTrue(grammar.lookahead(new GrammarContext(source)));

    }

    public void testParse() throws Exception {

        String source = " a b c ";

        GrammarElement grammar = repeat(lexemRaw(charIn("abc"), LexemType.SYMBOL));
        assertFalse(grammar.parse(new GrammarContext(source)));

        grammar = repeat(lexem(charIn("abc"), LexemType.SYMBOL)); // with separator management
        assertTrue(grammar.parse(new GrammarContext(source)));

        GrammarContext context = grammar.parse(source);

        // test
        assertEquals(Arrays.asList("a", "b", "c"), lexerToTerms(context.getLexer()));

    }
}