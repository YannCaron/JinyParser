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
public class StringBacktrackingIteratorTest extends TestCase {

	public void testTrivial() {
		String source = "hi !";
		StringBacktrackingIterator it = new StringBacktrackingIterator(source);

		assertEquals(new Character('h'), it.current());
		assertTrue(it.hasNext());
		assertFalse(it.hasPrevious());

		it.next();
		assertEquals(new Character('i'), it.current());
		assertTrue(it.hasNext());
		assertTrue(it.hasPrevious());

		it.next();
		assertEquals(new Character(' '), it.current());
		assertTrue(it.hasNext());
		assertTrue(it.hasPrevious());

		it.next();
		assertEquals(new Character('!'), it.current());
		assertFalse(it.hasNext());
		assertTrue(it.hasPrevious());

		it.previous();
		assertEquals(new Character(' '), it.current());
		assertTrue(it.hasNext());
		assertTrue(it.hasPrevious());

		it.previous();
		assertEquals(new Character('i'), it.current());
		assertTrue(it.hasNext());
		assertTrue(it.hasPrevious());

		it.previous();
		assertEquals(new Character('h'), it.current());
		assertTrue(it.hasNext());
		assertFalse(it.hasPrevious());
	}

	public void testDummy() {
		String source = "hi !";
		StringBacktrackingIterator it = new StringBacktrackingIterator(source);
		StringBacktrackingIterator it2 = new StringBacktrackingIterator(source);

		it.remove();

		assertEquals(it, it2);
	}

	public void testProofOfConcept() {

		class RecursiveDescentParser {

			public boolean parsePlus(StringBacktrackingIterator it, Stack<String> p) {
				if (!parseNumber(it, p)) {
					return false;
				}
				if (!parseSymbol(it, p, '+', "PLUS")) {
					it.previous();
					p.pop();
					return false;
				}
				if (!parseExpression(it, p)) {
					it.previous();
					p.pop();
					return false;
				}
				return true;
			}

			public boolean parseMinus(StringBacktrackingIterator it, Stack<String> p) {
				if (!parseNumber(it, p)) {
					return false;
				}
				if (!parseSymbol(it, p, '-', "MINUS")) {
					it.previous();
					p.pop();
					return false;
				}
				if (!parseExpression(it, p)) {
					it.previous();
					p.pop();
					return false;
				}
				return true;
			}

			public boolean parseSymbol(StringBacktrackingIterator it, Stack<String> p, Character symbol, String s) {
				if (it.current().equals(symbol)) {
					p.push(s);
					if (it.hasNext()) it.next();
					return true;
				}
				return false;
			}

			public boolean parseNumber(StringBacktrackingIterator it, Stack<String> p) {
				return parseSymbol(it, p, '7', "NUM");
			}

			public boolean parseExpression(StringBacktrackingIterator it, Stack<String> p) {
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
		StringBacktrackingIterator it = new StringBacktrackingIterator("7-7+7 ");

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