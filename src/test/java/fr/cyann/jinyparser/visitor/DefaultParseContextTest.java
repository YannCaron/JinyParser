package fr.cyann.jinyparser.visitor;/**
 * Copyright (C) 09/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import junit.framework.TestCase;

/**
 * The DefaultParseContextTest definition.
 */
public class DefaultParseContextTest extends TestCase {

	public void testProofOfConcept() {

		class RecursiveDescentParser {

			// tools
			private boolean isBinary(DefaultParseContext c, Character symbol) {
				c.store();
				if (!isNumber(c)) {
					c.restore();
					return false;
				}
				if (!isSymbol(c, symbol)) {
					c.restore();
					return false;
				}
				if (!isExpression(c)) {
					c.restore();
					return false;
				}
				c.restore();
				return true;
			}

			private boolean isSymbol(DefaultParseContext c, Character symbol) {
				if (c.current().equals(symbol)) {
					if (c.hasNext()) c.next();
					return true;
				}
				return false;
			}

			// lookahead
			public boolean isPlus(DefaultParseContext c) {
				return isBinary(c, '+');
			}

			public boolean isMinus(DefaultParseContext c) {
				return isBinary(c, '-');
			}

			public boolean isNumber(DefaultParseContext c) {
				return isSymbol(c, '7');
			}

			public boolean isExpression(DefaultParseContext c) {
				if (isPlus(c)) {
					return true;
				} else if (isMinus(c)) {
					return true;
				} else if (isNumber(c)) {
					return true;
				}
				return false;
			}

			public void parseSymbol(DefaultParseContext c, String token) {
				c.pushToken(token);
				if (c.hasNext()) c.next();
			}

			public void parsePlus(DefaultParseContext c) {
				parseNumber(c);
				parseSymbol(c, "PLUS");
				parseExpression(c);
			}

			public void parseMinus(DefaultParseContext c) {
				parseNumber(c);
				parseSymbol(c, "MINUS");
				parseExpression(c);
			}

			public void parseNumber(DefaultParseContext c) {
				parseSymbol(c, "NUM");
			}

			public void parseExpression(DefaultParseContext c) {
				if (isPlus(c)) {
					parsePlus(c);
				} else if (isMinus(c)) {
					parseMinus(c);
				} else if (isNumber(c)) {
					parseNumber(c);
				} else {
					throw new RuntimeException("Parsing error: no candidate to expression !");
				}
			}


		}

		DefaultParseContext c = new DefaultParseContext("7+7-7");
		new RecursiveDescentParser().parseExpression(c);

		for (String token : c) {
			System.out.println(token);
		}

		//assertEquals(expected, production);
		//assertTrue(result);

	}

}
