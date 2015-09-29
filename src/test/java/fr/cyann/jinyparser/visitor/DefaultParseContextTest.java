package fr.cyann.jinyparser.visitor;
/**
 * Copyright (C) 09/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.ast.Ast;
import fr.cyann.jinyparser.ast.NonTerminal;
import fr.cyann.jinyparser.ast.Terminal;
import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * The DefaultParseContextTest definition.
 */
public class DefaultParseContextTest extends TestCase {

	public void testProofOfConcept() {

		@SuppressWarnings("BooleanMethodIsAlwaysInverted")
		class ParsingExpressionGrammarLLk {

			// tools
			private boolean isBinary(DefaultParseContext c, Character symbol) {
				c.mark();
				if (!isSymbol(c, symbol)) {
					c.rollback();
					return false;
				}
				if (!isExpression(c)) {
					c.rollback();
					return false;
				}
				c.rollback(); // always rollback because it is in look ahead section
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
				try {
					c.mark();
					return isSymbol(c, '7');
				} finally {
					c.rollback();
				}
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

			// product
			public void parseSymbol(DefaultParseContext c, String token) {
				c.pushToken(token);
				if (c.hasNext()) c.next();
			}

			public void parsePlus(DefaultParseContext c) {
				parseSymbol(c, "PLUS");
				parseNumber(c);

				AstBinaryExpression ast = new AstBinaryExpression("+");
				ast.buildAst(c);
			}

			public void parseMinus(DefaultParseContext c) {
				parseSymbol(c, "MINUS");
				parseNumber(c);

				AstBinaryExpression ast = new AstBinaryExpression("-");
				ast.buildAst(c);
			}

			public void parseNumber(DefaultParseContext c) {
				parseSymbol(c, "NUM");

				AstNumber ast = new AstNumber(7);
				ast.buildAst(c);
			}

			public void parseExpression(DefaultParseContext c) {
				if (isNumber(c)) {
					parseNumber(c);

					boolean isp, ism;
					while ((isp = isPlus(c)) | (ism = isMinus(c))) {
						if (isp) parsePlus(c);
						if (ism) parseMinus(c);
					}
					System.out.println(c.toString());

				} else {
					throw new RuntimeException("Parsing error: no candidate to expression !");
				}

			}


		}

		DefaultParseContext c = new DefaultParseContext("7+7-7+7");
		new ParsingExpressionGrammarLLk().parseExpression(c);

		for (String token : c) {
			System.out.print(token + " ");
		}
		System.out.println(c);

		ArrayList<String> expected = new ArrayList<String>();
		expected.add("NUM");
		expected.add("PLUS");
		expected.add("NUM");
		expected.add("MINUS");
		expected.add("NUM");
		expected.add("PLUS");
		expected.add("NUM");

		int i = 0;
		for (String token : c) {
			assertEquals(expected.get(i), token);
			i++;
		}

		assertEquals("(+ (- (+ (N 7) (N 7)) (N 7)) (N 7))", c.getAst().toString());

	}

	class AstNumber extends Terminal<Integer> {

		private final Integer value;

		public AstNumber(Integer value) {
			this.value = value;
		}

		@Override
		public Integer getValue() {
			return value;
		}

		@Override
		public String toString() {
			return "(N " + value + ")";
		}

	}

	class AstBinaryExpression extends NonTerminal {

		private String sign;
		private Ast left, right;

		public AstBinaryExpression(String sign) {
			this.sign = sign;
		}

		@Override
		public int childSize() {
			return 2;
		}

		@Override
		public Ast getChild(int index) {
			if (index == 0) return left;
			return right;
		}

		@Override
		public void setChild(int index, Ast child) {
			if (index == 0) left = child;
			else right = child;
		}

		@Override
		public String toString() {
			return "(" + sign + " " + left + " " + right + ")";
		}


	}

}
