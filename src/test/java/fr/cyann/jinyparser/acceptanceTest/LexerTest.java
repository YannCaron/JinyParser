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
import fr.cyann.jinyparser.lexem.LexemType;
import junit.framework.TestCase;

import java.util.Arrays;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.*;
import static fr.cyann.jinyparser.testUtils.Utils.lexerToTerms;

/**
 * The DefaultParseContextTest definition.
 */
public class LexerTest extends TestCase {

	private static final LexemType NUMBER = new LexemType("NUMBER");

	public void testTrivialLexer() {

		String source = " 12 345\n8";

		// term
        GrammarElement digit = charIn("0123456789");

		// lexer
		GrammarElement number = lexem(oneOrMore(digit), NUMBER);

		// parser
		GrammarElement grammar = asMany(number, 3);

		// parse
		GrammarContext c = grammar.process().parse(source);

		// test
		assertEquals(Arrays.asList("12", "345", "8"), lexerToTerms(c.getLexer()));

	}

	public void testHexadecimal() {

		String source = "0xff00aa";

		// term
		GrammarElement hexDigit = charIn('0', '9').add('a', 'f').add('A', 'F');

		// lexer
		GrammarElement grammar = lexem(sequence(word("0x"), asMany(hexDigit, 6)), NUMBER);

		// parse
		GrammarContext c = grammar.process().parse(source);

		// test
		assertEquals(Arrays.asList("0xff00aa"), lexerToTerms(c.getLexer()));

		try {
			grammar.process().parse("0xff00aaaa");
			fail("Should not works!");
		} catch (Exception e) {
		}

		try {
			grammar.process().parse("0xff00a");
			fail("Should not works!");
		} catch (Exception e) {
		}

	}

	public void testLexerExpression() {

		String source = "7 + 10 - 5 + 4";

		// term
        GrammarElement digit = charIn("0123456789");
        GrammarElement sign = charIn("+-*/%");

		// lexer
		GrammarElement number = lexem(oneOrMore(digit), NUMBER);

        GrammarElement operator = lexem(sign, LexemType.SYMBOL);

		// parser
		GrammarElement grammar = sequence(number, oneOrMore(sequence(operator, number)));

		// parse
		GrammarContext c = grammar.process().parse(source);

		// test
		assertEquals(Arrays.asList("7", "+", "10", "-", "5", "+", "4"), lexerToTerms(c.getLexer()));

	}

}
