package fr.cyann.jinyparser.visitor;/**
 * Copyright (C) 09/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import junit.framework.TestCase;

import java.util.Stack;

/**
 * The DefaultParseContextTest definition.
 */
public class DefaultParseContextTest extends TestCase {

	public void testProofOfConcept() {

		class RecursiveDescentParser {

			public boolean parsePlus(Context c) {
				it.store();
				if (!parseNumber(it, p)) {
					it.restore();
					return false;
				}
				if (!parseSymbol(it, p, '+', "PLUS")) {
					it.restore();
					p.pop();
					return false;
				}
				if (!parseExpression(it, p)) {
					it.restore();
					p.pop();
					p.pop();
					return false;
				}
				it.dump();
				return true;
			}

			public boolean parseMinus(Context c) {
				it.store();
				if (!parseNumber(it, p)) {
					it.restore();
					return false;
				}
				if (!parseSymbol(it, p, '-', "MINUS")) {
					it.restore();
					p.pop();
					return false;
				}
				if (!parseExpression(it, p)) {
					it.restore();
					p.pop();
					p.pop();
					return false;
				}
				it.dump();
				return true;
			}

			public boolean parseSymbol(Context c, Character symbol, String s) {
				if (it.current().equals(symbol)) {
					p.push(s);
					if (it.hasNext()) it.next();
					return true;
				}
				return false;
			}

			public boolean parseNumber(Context c) {
				return parseSymbol(it, p, '7', "NUM");
			}

			public boolean parseExpression(Context c) {
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
