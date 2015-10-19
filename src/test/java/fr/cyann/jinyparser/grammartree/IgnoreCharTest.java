package fr.cyann.jinyparser.grammartree;

import fr.cyann.jinyparser.lexem.Lexem;
import fr.cyann.jinyparser.lexem.LexemType;
import junit.framework.TestCase;

import java.util.List;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.*;

/**
 * Copyright (C) 19/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

public class IgnoreCharTest extends TestCase {

    public void testLookahead() throws Exception {

        String source = "abc";

        GrammarElement grammar = lexem(sequence(word("a"), ignoreChar(word("b")), word("c")), LexemType.SYMBOL);

        GrammarContext context = new GrammarContext(source);
        grammar.lookahead(context);
        List<Lexem> result = context.getLexer();

        assertTrue(result.isEmpty());

    }

    public void testParse() throws Exception {

        String source = "abc";

        GrammarElement grammar = lexem(sequence(word("a"), ignoreChar(word("b")), word("c")), LexemType.SYMBOL);

        GrammarContext context = grammar.parse(source);
        List<Lexem> result = context.getLexer();

        assertEquals(1, result.size());
        assertEquals("ac", result.get(0).getTerm());

    }
}