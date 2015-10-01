package fr.cyann.jinyparser.acceptanceTest;
/**
 * Copyright (C) 09/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.grammar.*;
import fr.cyann.jinyparser.token.TokenType;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The DefaultParseContextTest definition.
 */
public class LexerTest extends TestCase {

	private TokenType NUMBER = new TokenType("NUMBER");

	public void testLexerTrivial() {

		List<String> results = new ArrayList<String>();

		String source = " 12 345\n8";

		// term
		GrammarElement digit = new LexerCharIn("0123456789");

		// lexer
		GrammarElement number = new SeparatorsManager(new TokenProducer(NUMBER, new Repeat(digit)));
		number = new TermTestAppender(number, results);

		// parser
		GrammarElement grammar = new Sequence().add(number).add(number).add(number);

		// parse
		GrammarContext c = new GrammarContext(source);
		grammar.parse(c);

		// test
		assertEquals(Arrays.asList("12", "345", "8"), results);

	}

	public void testLexerExpression() {

		List<String> results = new ArrayList<String>();

		String source = "7 + 10 - 5 + 4";

		// term
		GrammarElement digit = new LexerCharIn("0123456789");
		GrammarElement sign = new LexerCharIn("+-*/%");

		// lexer
		GrammarElement number = new SeparatorsManager(new TokenProducer(NUMBER, new Repeat(digit)));
		number = new TermTestAppender(number, results);

		GrammarElement operator = new SeparatorsManager(new TokenProducer(TokenType.SYMBOL, sign));
		operator = new TermTestAppender(operator, results);

		// parser
		GrammarElement grammar = new Sequence().add(number).add(new Repeat(new Sequence().add(operator).add(number)));

		// parse
		GrammarContext c = new GrammarContext(source);
		grammar.parse(c);

		// test
		assertEquals(Arrays.asList("7", "+", "10", "-", "5", "+", "4"), results);

	}

	public static class TermTestAppender extends GrammarDecorator {

		private final List<String> results;

		public TermTestAppender(GrammarElement decorated, List<String> results) {
			super(decorated);
			this.results = results;
		}

		@Override
		public boolean lookahead(GrammarContext context) {
			return false;
		}

		@Override
		public boolean parse(GrammarContext context) {
			boolean result = decorated.parse(context);

			if (result)
				results.add(context.getCurrentToken().getTerm());

			return result;
		}

	}
}
