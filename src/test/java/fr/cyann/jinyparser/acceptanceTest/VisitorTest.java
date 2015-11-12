package fr.cyann.jinyparser.acceptanceTest;/**
 * Copyright (C) 04/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.acceptanceTest.Grammars.AstBinaryExpression;
import fr.cyann.jinyparser.acceptanceTest.Grammars.AstNumber;
import fr.cyann.jinyparser.grammartree.GrammarContext;
import fr.cyann.jinyparser.grammartree.GrammarElement;
import fr.cyann.jinyparser.parsetree.ParsemVisitor;
import fr.cyann.jinyparser.parsetree.VisitorContext;
import fr.cyann.jinyparser.parsetree.VisitorInjectorClassMap;
import junit.framework.TestCase;

import java.util.Stack;

/**
 * The VisitorTest definition.
 */
public class VisitorTest extends TestCase {

	static final ParsemVisitor<AstNumber, ArithmeticContext> VISIT_NUMBER = new ParsemVisitor<AstNumber, ArithmeticContext>() {
		@Override
		public void visit(AstNumber parsem, ArithmeticContext context) {
			context.push(parsem.getValue());
		}
	};
	static final ParsemVisitor<AstBinaryExpression, ArithmeticContext> VISIT_ADDITION = new ParsemVisitor<AstBinaryExpression, ArithmeticContext>() {
		@Override
		public void visit(AstBinaryExpression parsem, ArithmeticContext context) {
			parsem.getLeft().visit(context);
			parsem.getRight().visit(context);

			int i1 = context.pop();
			int i2 = context.pop();

			context.push(i1 + i2);
		}
	};
	static final ParsemVisitor<AstBinaryExpression, ArithmeticContext> VISIT_MULTIPLICATION = new ParsemVisitor<AstBinaryExpression, ArithmeticContext>() {
		@Override
		public void visit(AstBinaryExpression parsem, ArithmeticContext context) {
			parsem.getLeft().visit(context);
			parsem.getRight().visit(context);

			int i1 = context.pop();
			int i2 = context.pop();

			context.push(i1 * i2);
		}
	};

	public void testTrivialParser() {

		String source = "7 + 10 + 4";

		// parser
		GrammarElement.ProcessedGrammar grammar = Grammars.arithmeticWithLevelAndParenthesis(VISIT_NUMBER, VISIT_ADDITION, VISIT_MULTIPLICATION);

		// parse
		GrammarContext c = grammar.parse(source);

		System.out.println("Parse tree: " + c.getParseTree());

		assertEquals("('+' ('+' 'n7' 'n10') 'n4')", c.getParseTree().toString());

		// visit and test result
		ArithmeticContext context = new ArithmeticContext();
		c.getParseTree().visit(context);
		assertEquals(21, context.firstElement().intValue());

	}

	public void testOperatorLevelParser() {

		String source = "7 + 10 * 4 + 7";

		// parser
		GrammarElement.ProcessedGrammar grammar = Grammars.arithmeticWithLevelAndParenthesis(VISIT_NUMBER, VISIT_ADDITION, VISIT_MULTIPLICATION);

		// parse
		GrammarContext c = grammar.parse(source);

		System.out.println("Parse tree: " + c.getParseTree());

		assertEquals("('+' ('+' 'n7' ('*' 'n10' 'n4')) 'n7')", c.getParseTree().toString());

		// visit and test result
		ArithmeticContext context = new ArithmeticContext();
		c.getParseTree().visit(context);
		assertEquals(54, context.firstElement().intValue());
	}

	public void testOperatorLevelWithParenthesisParser() {

		String source = "7 + 10 * (4 + 7)";

		// parser
		GrammarElement.ProcessedGrammar grammar = Grammars.arithmeticWithLevelAndParenthesis(VISIT_NUMBER, VISIT_ADDITION, VISIT_MULTIPLICATION);

		// parse
		GrammarContext c = grammar.parse(source);

		System.out.println("Parse tree: " + c.getParseTree());

		assertEquals("('+' 'n7' ('*' 'n10' ('+' 'n4' 'n7')))", c.getParseTree().toString());

		// visit and test result
		ArithmeticContext context = new ArithmeticContext();
		c.getParseTree().visit(context);
		assertEquals(117, context.firstElement().intValue());

		// inject visitor
		c.getParseTree().injectVisitor(new VisitorInjectorClassMap<ToStringContext>() {
			@Override
			public void buildVisitors() {
				this.addVisitor(AstNumber.class, new ParsemVisitor<AstNumber, ToStringContext>() {
					@Override
					public void visit(AstNumber parsem, ToStringContext context) {
						context.append("n" + parsem.getValue());
					}
				});

				this.addVisitor(AstBinaryExpression.class, new ParsemVisitor<AstBinaryExpression, ToStringContext>() {
					@Override
					public void visit(AstBinaryExpression parsem, ToStringContext context) {
						context.append("(op ");
						parsem.getLeft().visit(context);
						context.append(" ");
						parsem.getRight().visit(context);
						context.append(")");
					}
				});
			}
		});

		ToStringContext toStringContext = new ToStringContext();
		c.getParseTree().visit(toStringContext);
		System.out.println(toStringContext.toString());
		assertEquals("(op n7 (op n10 (op n4 n7)))", toStringContext.toString());

	}

	//region tools

	static class ArithmeticContext extends VisitorContext {

		private final Stack<Integer> stack;

		public ArithmeticContext() {
			stack = new Stack<Integer>();
		}

		public Integer pop() {
			return stack.pop();
		}

		public void push(Integer item) {
			stack.push(item);
		}

		public Integer firstElement() {
			return stack.firstElement();
		}
	}

	static class ToStringContext extends VisitorContext {

		private final StringBuilder sb;

		ToStringContext() {
			sb = new StringBuilder();
		}

		public void append(String string) {
			sb.append(string);
		}

		public String toString() {
			return sb.toString();
		}

	}

	// endregion
}
