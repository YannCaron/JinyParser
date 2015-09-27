package fr.cyann.jinyparser.utils;

import junit.framework.TestCase;

import java.util.Stack;

/**
 * Copyright (C) 07/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/
public class StringLookaheadIteratorTest extends TestCase {

	public void testTrivial() {
		String source = "hi !";
		LookaheadIterator<Character> it = new StringLookaheadIterator(source);

		assertEquals(new Character('h'), it.current());
		assertTrue(it.hasNext());

		it.next();
		assertEquals(new Character('i'), it.current());
		assertTrue(it.hasNext());

		it.next();
		assertEquals(new Character(' '), it.current());
		assertTrue(it.hasNext());

		it.next();
		assertEquals(new Character('!'), it.current());
		assertFalse(it.hasNext());
	}

	public void testLookahead() {
		String source = "hi !";
		LookaheadIterator<Character> it = new StringLookaheadIterator(source);

		assertEquals(new Character('h'), it.current());
		assertTrue(it.hasNext());

		it.next();
		assertEquals(new Character('i'), it.current());
		assertTrue(it.hasNext());

		it.next();
		it.mark();
		assertEquals(new Character(' '), it.current());
		assertTrue(it.hasNext());

		it.next();
		it.mark();
		assertEquals(new Character('!'), it.current());
		assertFalse(it.hasNext());

		try {
			it.next();
			fail("Exception should be thrown");
		} catch (Exception ex) {
			// do nothing
		}

		it.resume();

		it.rollback();
		assertEquals(new Character(' '), it.current());
		assertTrue(it.hasNext());

	}

	public void testProofOfConcept() {

		class RecursiveDescentParser {

			public boolean parsePlus(LookaheadIterator it, Stack<String> p) {
				it.mark();
				if (!parseNumber(it, p)) {
					it.rollback();
					return false;
				}
				if (!parseSymbol(it, p, '+', "PLUS")) {
					it.rollback();
					p.pop();
					return false;
				}
				if (!parseExpression(it, p)) {
					it.rollback();
					p.pop();
					p.pop();
					return false;
				}
				it.resume();
				return true;
			}

			public boolean parseMinus(LookaheadIterator it, Stack<String> p) {
				it.mark();
				if (!parseNumber(it, p)) {
					it.rollback();
					return false;
				}
				if (!parseSymbol(it, p, '-', "MINUS")) {
					it.rollback();
					p.pop();
					return false;
				}
				if (!parseExpression(it, p)) {
					it.rollback();
					p.pop();
					p.pop();
					return false;
				}
				it.resume();
				return true;
			}

			public boolean parseSymbol(LookaheadIterator it, Stack<String> p, Character symbol, String s) {
				if (it.current().equals(symbol)) {
					p.push(s);
					if (it.hasNext()) it.next();
					return true;
				}
				return false;
			}

			public boolean parseNumber(LookaheadIterator it, Stack<String> p) {
				return parseSymbol(it, p, '7', "NUM");
			}

			public boolean parseExpression(LookaheadIterator it, Stack<String> p) {
				if (parsePlus(it, p)) {
					return true;
				} else if (parseMinus(it, p)) {
					return true;
				} else if (parseNumber(it, p)) {
					return true;
				}
				return false;
			}

		}

		Stack<String> production = new Stack<String>();
		LookaheadIterator it = new StringLookaheadIterator("7-7+7");

		boolean result = new RecursiveDescentParser().parseExpression(it, production);

		Stack<String> expected = new Stack<String>();
		expected.push("NUM");
		expected.push("MINUS");
		expected.push("NUM");
		expected.push("PLUS");
		expected.push("NUM");

		assertEquals(expected, production);
		assertTrue(result);

	}

}