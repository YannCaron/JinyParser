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
import fr.cyann.jinyparser.grammartree.GrammarElement;
import fr.cyann.jinyparser.grammartree.LexemCreator;
import fr.cyann.jinyparser.grammartree.Recursive;
import fr.cyann.jinyparser.lexem.LexemType;
import fr.cyann.jinyparser.parsetree.ParsemVisitor;
import fr.cyann.jinyparser.parsetree.VisitorContext;
import junit.framework.TestCase;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.*;

/**
 * The VisitorTest definition.
 */
@SuppressWarnings("ALL")
public class AnalyserTest extends TestCase {

	public static <C extends VisitorContext> GrammarElement.ProcessedGrammar lrGrammar(ParsemVisitor<AstNumber, C> numberVisitor, ParsemVisitor<AstBinaryExpression, C> additionVisitor, ParsemVisitor<AstBinaryExpression, C> multiplicationVisitor) {
		// lexer
		LexemCreator leftParenthesis = lexem(LexemType.SYMBOL, charIn("("));
		LexemCreator rightParenthesis = lexem(LexemType.SYMBOL, charIn(")"));
		LexemCreator lexNum = lexem(Grammars.NUMBER, oneOrMore(charIn('0', '9')));
		LexemCreator lexAdd = lexem(Grammars.OPERATOR, charIn("+"));
		LexemCreator lexMult = lexem(Grammars.OPERATOR, charIn("*"));

		// terminal
		GrammarElement number = terminal("number", AstNumber.class, lexNum).setVisitor(numberVisitor);
		GrammarElement addSign = terminal("addSign", lexAdd);
		GrammarElement multiplySign = terminal("multiplySign", lexMult);

		// recursive
		Recursive expr = recursive("expr");

		// non terminal

		// <expr> := <expr> '*' <expr>
		//		   | <expr> '+' <expr>
		//         | <num>
		//         | '(' <expr> ')'
		expr.setGrammar(
				choice(
						sequence(expr, multiplySign, expr),
						sequence(expr, addSign, expr),
						number,
						sequence(leftParenthesis, expr, rightParenthesis)
				)
		);

		// process
		return expr.process();
	}

	public static <C extends VisitorContext> GrammarElement.ProcessedGrammar redundantArithmeticGrammar() {
		// lexer
		LexemCreator leftParenthesis = lexem(LexemType.SYMBOL, charIn("("));
		LexemCreator rightParenthesis = lexem(LexemType.SYMBOL, charIn(")"));
		LexemCreator lexNum = lexem(Grammars.NUMBER, oneOrMore(charIn('0', '9')));
		LexemCreator lexAdd = lexem(Grammars.OPERATOR, charIn("+"));
		LexemCreator lexMult = lexem(Grammars.OPERATOR, charIn("*"));

		// terminal
		GrammarElement number = terminal("Number", AstNumber.class, lexNum);
		GrammarElement addSign = terminal("AddSign", lexAdd);
		GrammarElement multiplySign = terminal("MultiplySign", lexMult);

		// recursive
		Recursive term = recursive("Expression");

		// non terminal

		// <multiplication> := <produceNumber> [ { '*' <produceNumber> } ]
		GrammarElement multiplication = nonTerminal("Expression", AstBinaryExpression.class,
				sequence(term, zeroOrMore(sequence(multiplySign, create("right", term).aggregateWith("sign", "left")))));

		// <addition> := <multiplication> [ { '+' <multiplication> } ]
		GrammarElement addition = nonTerminal("Expression", AstBinaryExpression.class,
				sequence(multiplication, zeroOrMore(sequence(addSign, create("right", multiplication).aggregateWith("sign", "left")))));

		// <term> := <number> | '(' <addition> ')'
		term.setGrammar(choice(number, sequence(leftParenthesis, addition, rightParenthesis)));

		// process
		return addition.process();
	}

	public void testLRGrammarOnPEG() {

		// grammar
		GrammarElement.ProcessedGrammar grammar = lrGrammar(null, null, null);

		// source
		String source = "7 + 10 * (4 + 7)";

		// to BNF
		//System.out.println("Grammar tree:\n" + grammar.toBnf());
		//System.out.println();

		// parse
		/*GrammarContext c = grammar.parse(source);

		System.out.println("Parse tree: " + c.getParseTree());

		assertEquals("('+' 'n7' ('*' 'n10' ('+' 'n4' 'n7')))", c.getParseTree().toString());*/

	}

	public void testRedundantGrammar() {

		// grammar
		GrammarElement.ProcessedGrammar grammar = redundantArithmeticGrammar();

		// source
		String source = "7 + 10 * (4 + 7)";

		// to BNF
		System.out.println("Grammar tree:\n" + grammar.toBnf());
		System.out.println();

	}
}
