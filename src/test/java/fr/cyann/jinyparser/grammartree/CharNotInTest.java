package fr.cyann.jinyparser.grammartree;

import junit.framework.TestCase;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.charNotIn;
import static fr.cyann.jinyparser.grammartree.GrammarFactory.oneOrMore;

/**
 * Copyright (C) 17/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

public class CharNotInTest extends TestCase {

	public void testAdd() throws Exception {
		CharNotIn charNotIn = charNotIn('A', 'Z');
		GrammarElement grammar = oneOrMore(charNotIn);

		grammar.process().parse("abcde");

		try {
			grammar.process().parse("abcdeABCDE");
			fail("Should not be parsed !");
		} catch (Exception e) {
		}

		charNotIn.add('a', 'z');

		try {
			grammar.process().parse("abcde");
			fail("Should not be parsed !");
		} catch (Exception e) {
		}

		grammar.process().parse("!+-*/");

	}

	public void testLookahead() throws Exception {
		String source = "dea";
		GrammarContext context = new GrammarContext(source);

		GrammarElement charNotIn = charNotIn("abc");

		assertTrue(charNotIn.lookahead(context));
		assertTrue(charNotIn.lookahead(context));
		assertFalse(charNotIn.lookahead(context));
	}

	public void testParse() throws Exception {
		String source = "dea";
		GrammarContext context = new GrammarContext(source);

		GrammarElement charNotIn = charNotIn("abc");

		assertTrue(charNotIn.parse(context));
		assertTrue(charNotIn.parse(context));
		assertFalse(charNotIn.parse(context));
	}
}