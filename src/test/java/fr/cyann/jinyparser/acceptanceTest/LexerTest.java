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
import fr.cyann.jinyparser.lexem.Lexem;
import fr.cyann.jinyparser.lexem.LexemType;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.*;

/**
 * The DefaultParseContextTest definition.
 */
public class LexerTest extends TestCase {

	private static final LexemType NUMBER = new LexemType("NUMBER");

	private List<String> lexerToTerms(Iterable<Lexem> lexer) {
		List<String> result = new ArrayList<String>();
		for (Lexem lexem : lexer) {
			result.add(lexem.getTerm());
		}

		return result;
	}

	public void testTrivialLexer() {

		String source = " 12 345\n8";

		// term
        GrammarElement digit = charIn("0123456789");

		// lexer
        GrammarElement number = lexem(repeat(digit), NUMBER);

		// parser
		GrammarElement grammar = sequence(number, number, number, number);

		// parse
		GrammarContext c = grammar.parse(source);

		// test
		assertEquals(Arrays.asList("12", "345", "8"), lexerToTerms(c.getLexer()));

	}

	public void testLexerExpression() {

		String source = "7 + 10 - 5 + 4";

		// term
        GrammarElement digit = charIn("0123456789");
        GrammarElement sign = charIn("+-*/%");

		// lexer
        GrammarElement number = lexem(repeat(digit), NUMBER);

        GrammarElement operator = lexem(sign, LexemType.SYMBOL);

		// parser
		GrammarElement grammar = sequence(number, repeat(sequence(operator, number)));

		// parse
		GrammarContext c = grammar.parse(source);

		// test
		assertEquals(Arrays.asList("7", "+", "10", "-", "5", "+", "4"), lexerToTerms(c.getLexer()));

	}

}
