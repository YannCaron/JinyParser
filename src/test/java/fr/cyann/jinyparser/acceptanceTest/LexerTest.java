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
import fr.cyann.jinyparser.token.LexemType;
import fr.cyann.jinyparser.token.Lexem;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static fr.cyann.jinyparser.grammartree.GrammarFactory.*;

/**
 * The DefaultParseContextTest definition.
 */
public class LexerTest extends TestCase {

	private LexemType NUMBER = new LexemType("NUMBER");

	private List<String> lexerToTerms(Iterable<Lexem> lexer) {
		List<String> result = new ArrayList<String>();
		for (Lexem lexem : lexer) {
			result.add(lexem.getTerm());
		}

		return result;
	}

	public void testLexerTrivial() {

		String source = " 12 345\n8";

		// term
		GrammarElement digit = lexerCharIn("0123456789");

		// lexer
		GrammarElement number = lexem(NUMBER, repeat(digit));

		// parser
		GrammarElement grammar = sequence(number, number, number, number);

		// parse
		GrammarContext c = new GrammarContext(source);
		grammar.parse(c);

		// test
		assertEquals(Arrays.asList("12", "345", "8"), lexerToTerms(c.getLexer()));

	}

	public void testLexerExpression() {

		String source = "7 + 10 - 5 + 4";

		// term
		GrammarElement digit = lexerCharIn("0123456789");
		GrammarElement sign = lexerCharIn("+-*/%");

		// lexer
		GrammarElement number = lexem(NUMBER, repeat(digit));

		GrammarElement operator = lexem(LexemType.SYMBOL, sign);

		// parser
		GrammarElement grammar = sequence(number, repeat(sequence(operator, number)));

		// parse
		GrammarContext c = new GrammarContext(source);
		grammar.parse(c);

		// test
		assertEquals(Arrays.asList("7", "+", "10", "-", "5", "+", "4"), lexerToTerms(c.getLexer()));

	}

}
