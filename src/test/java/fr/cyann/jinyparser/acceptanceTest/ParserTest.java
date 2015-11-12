package fr.cyann.jinyparser.acceptanceTest;
/**
 * Copyright (C) 09/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.exceptions.JinyException;
import fr.cyann.jinyparser.grammartree.GrammarContext;
import fr.cyann.jinyparser.grammartree.GrammarElement;
import fr.cyann.jinyparser.lexem.LexemType;
import junit.framework.TestCase;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.charIn;

/**
 * The DefaultParseContextTest definition.
 */
public class ParserTest extends TestCase {

	private static final LexemType NUMBER = new LexemType("produceNumber");
	private static final LexemType OPERATOR = new LexemType("operator");
	private static final LexemType KEYWORD = new LexemType("keyword");

	public void testTrivialParser() {

		String source = "7 + 10 + 4";

		// grammar
		GrammarElement.ProcessedGrammar grammar = Grammars.arithmeticWithLevelAndParenthesis(null, null, null);

		// parse
		GrammarContext c = grammar.parse(source);

		System.out.println("Parse tree: " + c.getParseTree());

		assertEquals("('+' ('+' 'n7' 'n10') 'n4')", c.getParseTree().toString());

	}

	public void testTrivialParsingError() {

		String source = "7 + 10 % 4";

		// term
		GrammarElement digit = charIn('0', '9');
		GrammarElement sign = charIn("+");

		// grammar
		GrammarElement.ProcessedGrammar grammar = Grammars.arithmeticWithLevelAndParenthesis(null, null, null);

		// parse
		try {
			grammar.parse(source);
			fail("must raise an error !");
		} catch (JinyException e) {
			System.out.println("success");
		}

	}

	public void testTrivialDefaultParsemParser() {

		String source = "7 * 10 + 4";

		// grammar
		GrammarElement.ProcessedGrammar grammar = Grammars.arithmeticWithLevelAndParenthesisDefault(null, null, null);

		// parse
		GrammarContext c = grammar.parse(source);

		System.out.println("Parse tree: " + c.getParseTree());

		assertEquals("(('7' '*' '10') '+' '4')", c.getParseTree().toString());

	}

	public void testLLkParser() {

		String source = "7 * 10 + 4";

		// grammar
		GrammarElement.ProcessedGrammar grammar = Grammars.arithmeticWithLevelAndParenthesis(null, null, null);

		// parse
		GrammarContext c = grammar.parse(source);

		System.out.println("Parse tree: " + c.getParseTree());

		assertEquals("('+' ('*' 'n7' 'n10') 'n4')", c.getParseTree().toString());

	}

	public void testOperatorLevelParser() {

		String source = "7 + 10 * 4 + 7";

		// grammar
		GrammarElement.ProcessedGrammar grammar = Grammars.arithmeticWithLevelAndParenthesis(null, null, null);

		// parse
		GrammarContext c = grammar.parse(source);

		System.out.println("Parse tree: " + c.getParseTree());

		assertEquals("('+' ('+' 'n7' ('*' 'n10' 'n4')) 'n7')", c.getParseTree().toString());

	}

	public void testOperatorLevelWithParenthesisParser() {

		String source = "7 + 10 * (4 + 7)";

		// grammar
		GrammarElement.ProcessedGrammar grammar = Grammars.arithmeticWithLevelAndParenthesis(null, null, null);

		// parse
		GrammarContext c = grammar.parse(source);

		System.out.println("Parse tree: " + c.getParseTree());

		assertEquals("('+' 'n7' ('*' 'n10' ('+' 'n4' 'n7')))", c.getParseTree().toString());

	}

	public void testIfParser() {

		String source = "if () {} elseif () {} elseif () {} else {} ";

		// grammar
		GrammarElement.ProcessedGrammar grammar = Grammars.dummyIf();

		// parse
		GrammarContext c = grammar.parse(source);

		System.out.println("Grammar tree: " + grammar.toString());
		System.out.println("Parse tree: " + c.getParseTree());

		assertEquals("if (['elseif', 'elseif'] 'else')", c.getParseTree().toString());

	}

}
