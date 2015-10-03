package fr.cyann.jinyparser.acceptanceTest;
/**
 * Copyright (C) 09/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.parsetree.Ast;
import fr.cyann.jinyparser.parsetree.NonTerminal;
import fr.cyann.jinyparser.parsetree.Terminal;
import fr.cyann.jinyparser.grammartree.GrammarContext;
import fr.cyann.jinyparser.grammartree.GrammarElement;
import fr.cyann.jinyparser.token.Lexem;
import fr.cyann.jinyparser.token.LexemType;
import junit.framework.TestCase;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.*;

/**
 * The DefaultParseContextTest definition.
 */
public class ParserTest extends TestCase {

	private LexemType NUMBER = new LexemType("NUMBER");
	private LexemType OPERATOR = new LexemType("OPERATOR");

	public void testParserTrivial() {

		String source = "7 + 10 - 5 + 4";

		// term
		GrammarElement digit = lexerCharIn("0123456789");
		GrammarElement sign = lexerCharIn("+-*/%");

		// lexer
		GrammarElement number = lexem(NUMBER, repeat(digit));

		GrammarElement operator = lexem(OPERATOR, sign);

		// parser
		GrammarElement grammar = sequence(number, repeat(sequence(operator, number)));

		// parse
		GrammarContext c = new GrammarContext(source);
		grammar.parse(c);


	}

	class AstNumber extends Terminal<Integer> {

		public AstNumber(Integer value) {
			super(new Lexem(String.valueOf(value), LexemType.SYMBOL), value);
		}

		@Override
		public String toString() {
			return "(N " + getValue() + ")";
		}
	}

	class AstBinaryExpression extends NonTerminal {

		private final String sign;
		private Ast left, right;

		public AstBinaryExpression(String sign) {
			super(new Lexem(sign, LexemType.SYMBOL), new Lexem(sign, LexemType.SYMBOL));
			this.sign = sign;
		}

		@Override
		public int childSize() {
			return 2;
		}

		@Override
		public Ast getChild(int index) {
			if (index == 0) return left;
			return right;
		}

		@Override
		public void setChild(int index, Ast child) {
			if (index == 0) left = child;
			else right = child;
		}

		@Override
		public String toString() {
			return "(" + sign + " " + left + " " + right + ")";
		}


	}

}
