package fr.cyann.jinyparser.grammartree;

import fr.cyann.jinyparser.lexem.LexemType;
import junit.framework.TestCase;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.produceTerminal;

/**
 * Copyright (C) 17/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

public class LexemCreatorCoreTest extends TestCase {

    public void testLookahead() throws Exception {

        String source = "abc";
        GrammarContext context = new GrammarContext(source);

        GrammarElement producer = produceTerminal(new Word("abc"), LexemType.SYMBOL);

        boolean result = producer.lookahead(context);

        assertTrue("'abc' literal should be parsed by grammar !", result);
        try {
            assertNull(context.popParsem());
            fail("parsem stack should be empty !");
        } finally {
            System.out.println("success");
        }
        assertEquals(true, context.isTerminated());

    }

    public void testParse() throws Exception {

        String source = "abc";
        GrammarContext context = new GrammarContext(source);

        GrammarElement producer = produceTerminal(new Word("abc"), LexemType.SYMBOL);

        boolean result = producer.parse(context);

        assertTrue("'abc' literal should be parsed by grammar !", result);
        assertEquals("'abc'", context.popParsem().toString());
        assertEquals(true, context.isTerminated());

    }
}