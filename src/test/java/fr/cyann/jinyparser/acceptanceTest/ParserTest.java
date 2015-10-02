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
import fr.cyann.jinyparser.grammartree.GrammarDecorator;
import fr.cyann.jinyparser.grammartree.GrammarElement;
import fr.cyann.jinyparser.token.TokenType;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.*;

/**
 * The DefaultParseContextTest definition.
 */
public class ParserTest extends TestCase {

	private TokenType NUMBER = new TokenType("NUMBER");

	public void testParserTrivial() {

		List<String> results = new ArrayList<String>();

		String source = "7 + 10 - 5 + 4";

		// term
		GrammarElement digit = lexerCharIn("0123456789");
		GrammarElement sign = lexerCharIn("+-*/%");

		// lexer
		GrammarElement number = separatorsManager(tokenProducer(NUMBER, repeat(digit)));

		GrammarElement operator = separatorsManager(tokenProducer(TokenType.SYMBOL, sign));

		// parser
		GrammarElement grammar = sequence(number, repeat(sequence(operator, number)));

		// parse
		GrammarContext c = new GrammarContext(source);
		grammar.parse(c);

		// test
		assertEquals(Arrays.asList("7", "+", "10", "-", "5", "+", "4"), results);


	}

}
