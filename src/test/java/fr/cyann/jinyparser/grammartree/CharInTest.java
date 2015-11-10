package fr.cyann.jinyparser.grammartree;

import junit.framework.TestCase;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.charIn;
import static fr.cyann.jinyparser.grammartree.GrammarFactory.oneOrMore;

/**
 * Copyright (C) 17/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

public class CharInTest extends TestCase {

	public void testAdd() throws Exception {
		CharIn charIn = charIn('a', 'z');
		GrammarElement grammar = oneOrMore(charIn);

		grammar.process().parse("abcde");

		try {
			grammar.process().parse("abcdeABCDE");
			fail("Should not be parsed !");
		} catch (Exception e) {
			System.out.println("Exception OK !");
		}

		charIn.add('A', 'Z');

		grammar.process().parse("abcdeABCDE");

		try {
			grammar.process().parse("abcdeABCDE!+");
			fail("Should not be parsed !");
		} catch (Exception e) {
			System.out.println("Exception OK !");
		}

		charIn.add("!+-*/");
		grammar.process().parse("abcdeABCDE!+");

	}

	public void testLookahead() throws Exception {
		String source = "abd";
        GrammarContext context = new GrammarContext(source);

        GrammarElement charIn = charIn("abc");

        assertTrue(charIn.lookahead(context));
        assertTrue(charIn.lookahead(context));
        assertFalse(charIn.lookahead(context));
    }

    public void testParse() throws Exception {
        String source = "abd";
        GrammarContext context = new GrammarContext(source);

        GrammarElement charIn = charIn("abc");

        assertTrue(charIn.parse(context));
        assertTrue(charIn.parse(context));
        assertFalse(charIn.parse(context));
    }
}