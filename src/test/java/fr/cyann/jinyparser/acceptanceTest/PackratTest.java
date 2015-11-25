package fr.cyann.jinyparser.acceptanceTest;
/**
 * Copyright (C) 09/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.grammartree.*;
import fr.cyann.jinyparser.lexem.LexemType;
import fr.cyann.jinyparser.parsetree.VisitorContext;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.*;

/**
 * The DefaultParseContextTest definition.
 */
public class PackratTest extends TestCase {

	static List<MocChoice> choices = new ArrayList<MocChoice>();

	public static Choice mocChoice(GrammarElement... elements) {
		MocChoice choice = new MocChoice(elements);
		choices.add(choice);
		return choice;
	}

	public static <C extends VisitorContext> GrammarElement.ProcessedGrammar issue_16_Grammar() {
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
		expression.setGrammar(mocChoice(multiplication, addition, term));

		// <multiplication> := <term> [ '*' <expression> ]
		multiplication.setGrammar(nonTerminal("Multiplication",
				sequence(aggregate(addition), zeroOrOne(sequence(aggregate(multiplySign), create(expression))))));

		// <addition> := <multiplication> [ '+' <expression> ]
		addition.setGrammar(nonTerminal("Addition",
				sequence(aggregate(term), zeroOrOne(sequence(aggregate(addSign), create(expression))))));

		// <term> := <number> | '(' <expression> ')'
		term.setGrammar(mocChoice(number, sequence(leftParenthesis, expression, rightParenthesis)));

		// process
		return expression.process();
	}

	public void testIssue_16() {

		String source = "7 + 10 + 4 + 7";

		// grammar
		GrammarElement.ProcessedGrammar grammar = issue_16_Grammar();

		// to BNF
		System.out.println("Grammar tree:\n" + grammar.toBnf());
		System.out.println();

		// parse
		GrammarContext c = grammar.parse(source);

		System.out.println("Parse tree: " + c.getParseTree());

		//assertEquals("('+' 'n7' ('*' 'n10' ('+' 'n4' 'n7')))", c.getParseTree().toString());

	}

	public void testPackrat() {

		String source = "1 + 1";

		// grammar
		GrammarElement.ProcessedGrammar grammar = issue_16_Grammar();

		// to BNF
		System.out.println("Grammar tree:\n" + grammar.toBnf());
		System.out.println();

		// parse
		GrammarContext c = grammar.parse(source);

		System.out.println("Parse tree: " + c.getParseTree());

		System.out.println(choices.get(0).getLookaheadCounter());
		System.out.println(choices.get(1).getLookaheadCounter());

		//assertEquals("('+' 'n7' ('*' 'n10' ('+' 'n4' 'n7')))", c.getParseTree().toString());

	}

	static class MocChoice extends Choice {

		private int lookaheadCounter;

		public MocChoice(GrammarElement[] children) {
			super(children);
			lookaheadCounter = 0;
		}

		public int getLookaheadCounter() {
			return lookaheadCounter;
		}

		@Override
		protected boolean lookahead(GrammarContext context) {
			lookaheadCounter++;
			return super.lookahead(context);
		}
	}
}
