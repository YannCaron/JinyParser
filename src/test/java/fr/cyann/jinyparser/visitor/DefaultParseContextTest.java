package fr.cyann.jinyparser.visitor;
/**
 * Copyright (C) 09/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.parsetree.ParsemElement;
import fr.cyann.jinyparser.parsetree.NonTerminal;
import fr.cyann.jinyparser.parsetree.Terminal;
import fr.cyann.jinyparser.token.Lexem;
import fr.cyann.jinyparser.token.LexemType;
import junit.framework.TestCase;

/**
 * The DefaultParseContextTest definition.
 */
public class DefaultParseContextTest extends TestCase {
/*
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
			public void parsePlus(DefaultParseContext c) {
				c.next();
				parseNumber(c);

				AstBinaryExpression ast = new AstBinaryExpression("+");
				ast.buildAst(c);
			}

			public void parseMinus(DefaultParseContext c) {
				c.next();
				parseNumber(c);

				AstBinaryExpression ast = new AstBinaryExpression("-");
				ast.buildAst(c);
			}

			public void parseNumber(DefaultParseContext c) {
				c.next();

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

		System.out.println(c);

		assertEquals("(+ (- (+ (N 7) (N 7)) (N 7)) (N 7))", c.getAst().toString());

	}

	class AstNumber extends Terminal<Integer> {

		public AstNumber(Integer value) {
			super(new Lexem(String.valueOf(value), LexemType.SYMBOL), value);
		}

		@Override
		public String toString() {
			return "(N " + getValue() + ")";
		}
	}

	class AstBinaryExpression extends NonTerminal {

		private final String sign;
		private ParsemElement left, right;

		public AstBinaryExpression(String sign) {
			super(new Lexem(sign, LexemType.SYMBOL), new Lexem(sign, LexemType.SYMBOL));
			this.sign = sign;
		}

		@Override
		public int childSize() {
			return 2;
		}

		@Override
		public ParsemElement getChild(int index) {
			if (index == 0) return left;
			return right;
		}

		@Override
		public void setChild(int index, ParsemElement child) {
			if (index == 0) left = child;
			else right = child;
		}

		@Override
		public String toString() {
			return "(" + sign + " " + left + " " + right + ")";
		}


	}
*/
}
