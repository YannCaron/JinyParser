package fr.cyann.jinyparser.acceptanceTest;/**
 * Copyright (C) 04/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.grammartree.GrammarContext;
import fr.cyann.jinyparser.grammartree.GrammarElement;
import fr.cyann.jinyparser.grammartree.Recursive;
import fr.cyann.jinyparser.lexem.Lexem;
import fr.cyann.jinyparser.lexem.LexemType;
import fr.cyann.jinyparser.parsetree.*;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.*;

/**
 * The VisitorTest definition.
 */
public class VisitorTest extends TestCase {

	static final ParsemVisitor<AstNumber, ArithmeticContext> VISIT_NUMBER = new ParsemVisitor<AstNumber, ArithmeticContext>() {
		@Override
		public void visit(AstNumber parsem, ArithmeticContext context) {
			context.push(parsem.value);
		}
	};
	static final ParsemVisitor<AstBinaryExpression, ArithmeticContext> VISIT_ADDITION = new ParsemVisitor<AstBinaryExpression, ArithmeticContext>() {
		@Override
		public void visit(AstBinaryExpression parsem, ArithmeticContext context) {
			parsem.left.visit(context);
			parsem.right.visit(context);

			int i1 = context.pop();
			int i2 = context.pop();

			context.push(i1 + i2);
		}
	};
	static final ParsemVisitor<AstBinaryExpression, ArithmeticContext> VISIT_MULTIPLICATION = new ParsemVisitor<AstBinaryExpression, ArithmeticContext>() {
		@Override
		public void visit(AstBinaryExpression parsem, ArithmeticContext context) {
			parsem.left.visit(context);
			parsem.right.visit(context);

			int i1 = context.pop();
			int i2 = context.pop();

			context.push(i1 * i2);
		}
	};
	private static final LexemType NUMBER = new LexemType("produceNumber");
	private static final LexemType OPERATOR = new LexemType("operator");
	private static final LexemType KEYWORD = new LexemType("keyword");

	public void testTrivialParser() {

		String source = "7 + 10 + 4";

		// term
		GrammarElement digit = charIn('0', '9');
		GrammarElement sign = charIn("+-*/%");

		// lexer
		GrammarElement number = produce("Number", oneOrMore(digit), NUMBER, AstNumber.class).setVisitor(VISIT_NUMBER);

		GrammarElement operation = catcher(create("Operation", number, AstBinaryExpression.class).setVisitor(VISIT_ADDITION), "right", "sign", "left");

		GrammarElement operator = produceTerminal("Operator", sign, OPERATOR);

		// parser
		// grammar := <produceNumber> { <operator> <produceNumber>}
		GrammarElement grammar = sequence(number, oneOrMore(sequence(operator, operation)));

		// parse
		GrammarContext c = grammar.process().parse(source);

		System.out.println("Parse tree: " + c.getParseTree());

		assertEquals("('+' ('+' 'n7' 'n10') 'n4')", c.getParseTree().toString());

		// visit and test result
		ArithmeticContext context = new ArithmeticContext();
		c.getParseTree().visit(context);
		assertEquals(21, context.firstElement().intValue());

	}

	public void testOperatorLevelParser() {

		String source = "7 + 10 * 4 + 7";

		// lexer
		GrammarElement number = produce("Number", oneOrMore(charIn('0', '9')), NUMBER, AstNumber.class).setVisitor(VISIT_NUMBER);

		GrammarElement addSign = produceTerminal("AddSign", charIn("+"), OPERATOR);
		GrammarElement multiplySign = produceTerminal("MultiplySign", charIn("*"), OPERATOR);

		// <multiplication> := <produceNumber> [ { '*' <produceNumber> } ]
		GrammarElement multiplyOperation = catcher(produce("Multiplication", number, NUMBER, AstBinaryExpression.class).setVisitor(VISIT_MULTIPLICATION), "right", "sign", "left");
		GrammarElement multiplication = sequence(number, zeroOrOne(oneOrMore(sequence(multiplySign, multiplyOperation))));

		// <addition> := <multiplication> [ { '+' <multiplication> } ]
		GrammarElement addOperation = catcher(produce("Addition", multiplication, NUMBER, AstBinaryExpression.class).setVisitor(VISIT_ADDITION), "right", "sign", "left");
		GrammarElement addition = sequence(multiplication, zeroOrOne(oneOrMore(sequence(addSign, addOperation))));

		// parse
		GrammarContext c = addition.process().parse(source);

		System.out.println("Parse tree: " + c.getParseTree());

		assertEquals("('+' ('+' 'n7' ('*' 'n10' 'n4')) 'n7')", c.getParseTree().toString());

		// visit and test result
		ArithmeticContext context = new ArithmeticContext();
		c.getParseTree().visit(context);
		assertEquals(54, context.firstElement().intValue());
	}

	public void testOperatorLevelWithParenthesisParser() {

		String source = "7 + 10 * (4 + 7)";

		// lexer
		GrammarElement leftParenthesis = lexem(charIn("("), LexemType.SYMBOL);
		GrammarElement rightParenthesis = lexem(charIn(")"), LexemType.SYMBOL);
		GrammarElement number = produce("Number", oneOrMore(charIn('0', '9')), NUMBER, AstNumber.class).setVisitor(VISIT_NUMBER);

		GrammarElement addSign = produceTerminal("AddSign", charIn("+"), OPERATOR);
		GrammarElement multiplySign = produceTerminal("MultiplySign", charIn("*"), OPERATOR);

		Recursive term = recursive("Term");

		// <multiplication> := <produceNumber> [ { '*' <produceNumber> } ]
		GrammarElement multiplyOperation = catcher(produce("Multiplication", term, NUMBER, AstBinaryExpression.class).setVisitor(VISIT_MULTIPLICATION), "right", "sign", "left");
		GrammarElement multiplication = sequence(term, zeroOrOne(oneOrMore(sequence(multiplySign, multiplyOperation))));

		// <addition> := <multiplication> [ { '+' <multiplication> } ]
		GrammarElement addOperation = catcher(produce("Addition", multiplication, NUMBER, AstBinaryExpression.class).setVisitor(VISIT_ADDITION), "right", "sign", "left");
		GrammarElement addition = sequence(multiplication, zeroOrOne(oneOrMore(sequence(addSign, addOperation))));

		// <num> := <number> | '(' <addition> ')'
		term.setGrammar(choice(number, sequence(leftParenthesis, addition, rightParenthesis)));

		// parser
		GrammarElement grammar = addition;

		// parse
		GrammarContext c = grammar.process().parse(source);

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
						context.append("n" + parsem.value);
					}
				});

				this.addVisitor(AstBinaryExpression.class, new ParsemVisitor<AstBinaryExpression, ToStringContext>() {
					@Override
					public void visit(AstBinaryExpression parsem, ToStringContext context) {
						context.append("(op ");
						parsem.left.visit(context);
						context.append(" ");
						parsem.right.visit(context);
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

	private GrammarElement produceNumber(GrammarElement decorated) {
		return produce("Number", decorated, NUMBER, AstNumber.class);
	}

	private GrammarElement produceBinaryExpression(GrammarElement decorated) {
		return catcher(produce("BinaryOperation", decorated, NUMBER, AstBinaryExpression.class), "right", "sign", "left");
	}

	static class AstNumber extends Terminal {

		private final int value;

		public AstNumber(Lexem lexem) {
			super(lexem);
			value = Integer.valueOf(lexem.getTerm());

		}

		@Override
		public String toString() {
			return "'n" + value + "'";
		}
	}

	static class AstBinaryExpression extends NonTerminal {

		@AggregateField()
		private ParsemElement left;
		@AggregateField()
		private ParsemElement sign;
		@AggregateField()
		private ParsemElement right;

		public AstBinaryExpression(Lexem lexem) {
			super(lexem);
		}

		@Override
		public String toString() {
			return "(" + sign + " " + left + " " + right + ")";
		}

	}

	static class AstIf extends NonTerminal {

		@AggregateField(identity = "elseif")
		private final List<ParsemElement> elseifs;
		private ParsemElement if_;
		@AggregateField(identity = "else")
		private ParsemElement else_;

		public AstIf(Lexem lexem) {
			super(lexem);
			elseifs = new ArrayList<ParsemElement>();
		}

		@Override
		public String toString() {
			return "if (" + elseifs.toString() + " " + else_ + ")";
		}

	}

	static class ArithmeticContext extends VisitorContext {

		private final Stack<Integer> stack;

		public ArithmeticContext() {
			stack = new Stack<Integer>();
		}

		public Integer pop() {
			return stack.pop();
		}

		public Integer push(Integer item) {
			return stack.push(item);
		}

		public Integer firstElement() {
			return stack.firstElement();
		}
	}

	static class ToStringContext extends VisitorContext {

		private StringBuilder sb;

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
