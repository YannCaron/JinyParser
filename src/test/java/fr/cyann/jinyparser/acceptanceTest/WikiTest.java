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
import fr.cyann.jinyparser.grammartree.LexemCreator;
import fr.cyann.jinyparser.grammartree.Recursive;
import fr.cyann.jinyparser.lexem.LexemType;
import junit.framework.TestCase;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.*;

/**
 * The VisitorTest definition.
 */
@SuppressWarnings("ALL")
public class WikiTest extends TestCase {

	public static final LexemType NUMBER = new LexemType("number");
	public static final LexemType OPERATOR = new LexemType("operator");

	public void testWikiTenMunitesTutorial() {

		// lexer
		LexemCreator lexLParent = lexem(LexemType.SYMBOL, word("("));
		LexemCreator lexRParent = lexem(LexemType.SYMBOL, word(")"));
		LexemCreator lexNum = lexem(NUMBER, oneOrMore(charIn('0', '9')));
		LexemCreator lexAdd = lexem(OPERATOR, word("+"));
		LexemCreator lexSub = lexem(OPERATOR, word("-"));
		LexemCreator lexMult = lexem(OPERATOR, word("*"));
		LexemCreator lexDiv = lexem(OPERATOR, word("/"));

		// terminal
		GrammarElement number = terminal("number", lexNum);
		GrammarElement addOp = terminal("addOp", lexAdd);
        GrammarElement subOp = terminal("subOp", lexSub);
        GrammarElement multOp = terminal("multOp", lexMult);
		GrammarElement divOp = terminal("divOp", lexDiv);

		// recursive
		Recursive expr = recursive("expr");

		// non terminal

		// <expr> := <expr> '*' <expr>
		//		   | <expr> '+' <expr>
		//         | <num>
		//         | '(' <expr> ')'
		expr.setGrammar(
				choice(
                        nonTerminal("hiOperation", sequence(create(expr), aggregate(choice(addOp, subOp)), aggregate(expr))),   // E ::= E ('+' | '-') E
                        nonTerminal("lowOperation", sequence(create(expr), aggregate(choice(multOp, divOp)), aggregate(expr))), //     | E ('*' | '/') Edetermin
                        number,                                                                                                 //     | Number
                        sequence(lexLParent, expr, lexRParent)                                                                  //     | '(' E ')'
                )
		);

		// grammar
		// process
		GrammarElement.ProcessedGrammar grammar = expr.process();

		// and print
		System.out.println("Grammar tree:");
		System.out.println(grammar.toBnf());

		// source
        String source = "(7 + 10) * 4 + 7";

		// parse
		GrammarContext c = grammar.parse(source);

		System.out.println("Parse tree: " + c.getParseTree());

		//assertEquals("('7' '+' ('10' '*' ('4' '+' '7')))", c.getParseTree().toString());

	}


}
