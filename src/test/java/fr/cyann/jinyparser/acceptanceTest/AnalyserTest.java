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
        GrammarElement number = terminal("number", lexNum);
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
						nonTerminal("multiplication", sequence(aggregate(expr), create(multiplySign), aggregate(expr))),
						nonTerminal("addition", sequence(aggregate(expr), create(addSign), aggregate(expr))),
                        number,
                        sequence(leftParenthesis, expr, rightParenthesis)
                )
        );

		// process
		return expr.process();
	}

	public static <C extends VisitorContext> GrammarElement.ProcessedGrammar llGrammar() {
		// lexer
		LexemCreator leftParenthesis = lexem(LexemType.SYMBOL, charIn("("));
		LexemCreator rightParenthesis = lexem(LexemType.SYMBOL, charIn(")"));
		LexemCreator lexNum = lexem(Grammars.NUMBER, oneOrMore(charIn('0', '9')));
		LexemCreator lexAdd = lexem(Grammars.OPERATOR, charIn("+"));
		LexemCreator lexMult = lexem(Grammars.OPERATOR, charIn("*"));

		// terminal
		GrammarElement number = terminal("Number", lexNum);
		GrammarElement addSign = terminal("AddSign", lexAdd);
		GrammarElement multiplySign = terminal("MultiplySign", lexMult);

		// recursive
		Recursive expression = recursive("Expression");
		Recursive addition = recursive("Addition");
		Recursive multiplication = recursive("Multiplication");
		Recursive term = recursive("Term");

		// non terminal
		// <expression> := <multiplication> | <addition> | <term>
		expression.setGrammar(choice(multiplication, addition, term));

		// <multiplication> := <term> [ '*' <expression> ]
		multiplication.setGrammar(nonTerminal("Multiplication",
				sequence(aggregate(addition), zeroOrOne(sequence(aggregate(multiplySign), create(expression))))));

		// <addition> := <multiplication> [ '+' <expression> ]
		addition.setGrammar(nonTerminal("Addition",
				sequence(aggregate(term), zeroOrOne(sequence(aggregate(addSign), create(expression))))));

		// <term> := <number> | '(' <expression> ')'
		term.setGrammar(choice(number, sequence(leftParenthesis, expression, rightParenthesis)));

		// process
		return expression.process();
	}

	public void testLRGrammarOnPEG() {

		// grammar
		GrammarElement.ProcessedGrammar grammar = lrGrammar(null, null, null);

		// source
		String source = "7 + 10 + 4 + 7";

		// to BNF
        System.out.println("Grammar tree:\n" + grammar.toBnf());
        System.out.println();

		// parse
        GrammarContext c = grammar.parse(source);

		System.out.println("Parse tree: " + c.getParseTree());

		//assertEquals("('7' '+' ('10' '*' ('4' '+' '7')))", c.getParseTree().toString());

	}

	public void testLLGrammarOnPEG() {

		String source = "7 + 10 + 4 + 7";

		// grammar
		GrammarElement.ProcessedGrammar grammar = llGrammar();

		// to BNF
		System.out.println("Grammar tree:\n" + grammar.toBnf());
		System.out.println();

		// parse
		GrammarContext c = grammar.parse(source);

		System.out.println("Parse tree: " + c.getParseTree());

		//assertEquals("('+' 'n7' ('*' 'n10' ('+' 'n4' 'n7')))", c.getParseTree().toString());

	}

}
