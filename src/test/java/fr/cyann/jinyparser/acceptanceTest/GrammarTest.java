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

/**
 * The DefaultParseContextTest definition.
 */
public class GrammarTest extends TestCase {

	private TokenType NUMBER = new TokenType("NUMBER");

	public void testProofOfConcept() {

		String source = "12 345";

		GrammarContext c = new GrammarContext(source);

		// term
		GrammarElement digit = new LexerCharIn("0123456789");
		GrammarElement separator = new LexerCharIn(" \t");
		GrammarElement lineSeparator = new LexerCharIn("\n");

		// lexer
		GrammarElement num = new TokenProducer(NUMBER, new Repeat(digit));

		// parser
		GrammarElement g = new Sequence().add(num).add(separator).add(num);

		g.parse(c);

		System.out.println(c);

	}

}
