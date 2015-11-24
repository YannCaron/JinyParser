package fr.cyann.jinyparser.acceptanceTest;
/**
 * Copyright (C) 09/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.grammartree.GrammarContext;
import fr.cyann.jinyparser.grammartree.GrammarElement;
import fr.cyann.jinyparser.grammartree.LexemCreator;
import fr.cyann.jinyparser.grammartree.Recursive;
import fr.cyann.jinyparser.lexem.LexemType;
import fr.cyann.jinyparser.parsetree.VisitorContext;
import junit.framework.TestCase;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.*;

/**
 * The DefaultParseContextTest definition.
 */
public class PackratTest extends TestCase {

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
}
