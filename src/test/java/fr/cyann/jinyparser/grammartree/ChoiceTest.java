package fr.cyann.jinyparser.grammartree;

import junit.framework.TestCase;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.choice;
import static fr.cyann.jinyparser.grammartree.GrammarFactory.word;

/**
 * Copyright (C) 16/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

public class ChoiceTest extends TestCase {

    public void testLookahead() throws Exception {

        String source = "abc";
        GrammarContext context = new GrammarContext(source);

        GrammarElement choice = choice(word("a"), word("b"));

        assertTrue(choice.lookahead(context));
        assertTrue(choice.lookahead(context));
        context.nextChar();
        assertFalse(choice.lookahead(context));

    }

    public void testParse() throws Exception {

        String source = "abc";
        GrammarContext context = new GrammarContext(source);

        GrammarElement choice = choice(word("a"), word("b"));

        assertTrue(choice.parse(context));
        assertTrue(choice.parse(context));
        assertFalse(choice.parse(context));

    }
}