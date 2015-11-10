package fr.cyann.jinyparser.acceptanceTest;/**
 * Copyright (C) 04/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.grammartree.GrammarElement;
import fr.cyann.jinyparser.grammartree.Recursive;
import fr.cyann.jinyparser.lexem.Lexem;
import fr.cyann.jinyparser.lexem.LexemType;
import fr.cyann.jinyparser.parsetree.AggregateField;
import fr.cyann.jinyparser.parsetree.NonTerminal;
import fr.cyann.jinyparser.parsetree.ParsemElement;
import fr.cyann.jinyparser.parsetree.Terminal;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.*;

/**
 * The VisitorTest definition.
 */
@SuppressWarnings("ALL")
public class BnfTest extends TestCase {

	private static final LexemType NUMBER = new LexemType("produceNumber");
	private static final LexemType OPERATOR = new LexemType("operator");
	private static final LexemType KEYWORD = new LexemType("keyword");

	public void testOperatorLevelWithParenthesisParser() {

		// lexer
		GrammarElement leftParenthesis = lexem(charIn("("), LexemType.SYMBOL);
		GrammarElement rightParenthesis = lexem(charIn(")"), LexemType.SYMBOL);
		GrammarElement number = produce("Number", oneOrMore(charIn('0', '9')), NUMBER, AstNumber.class);

        GrammarElement addSign = produceTerminal("AddSign", charIn("+"), OPERATOR);
        GrammarElement multiplySign = produceTerminal("MultiplySign", charIn("*"), OPERATOR);

		// recursive
		Recursive term = recursive("Term");

		// <multiplication> := <produceNumber> [ { '*' <produceNumber> } ]
        GrammarElement multiplyOperation = catcher(produce("Multiplication", term, NUMBER, AstBinaryExpression.class), "right", "sign", "left");
        GrammarElement multiplication = sequence(term, zeroOrMore(sequence(multiplySign, multiplyOperation)));

		// <addition> := <multiplication> [ { '+' <multiplication> } ]
        GrammarElement addOperation = catcher(produce("Addition", multiplication, NUMBER, AstBinaryExpression.class), "right", "sign", "left");
        GrammarElement addition = sequence(multiplication, zeroOrMore(sequence(addSign, addOperation)));

		// <num> := <number> | '(' <addition> ')'
		term.setGrammar(choice(number, sequence(leftParenthesis, addition, rightParenthesis)));

		// parser
		GrammarElement.ProcessedGrammar grammar = addition.process();

		// to BNF
		System.out.println("Grammar tree:\n" + grammar.toBnf());
		System.out.println();

		//RailroadDiagram.Browse(grammar);

	}

	public void testIfParser() {

		// lexer
		GrammarElement pl = lexem(word("("), LexemType.SYMBOL);
		GrammarElement pr = lexem(word(")"), LexemType.SYMBOL);
		GrammarElement bl = lexem(word("{"), LexemType.SYMBOL);
		GrammarElement br = lexem(word("}"), LexemType.SYMBOL);

        GrammarElement if_ = sequence(produce("If", word("if"), KEYWORD, AstIf.class), pl, pr, bl, br);
        GrammarElement elseif = sequence(dropper(produceTerminal("elseIf", word("elseif"), KEYWORD), "elseif"), pl, pr, bl, br);
        GrammarElement else_ = sequence(dropper(produceTerminal("else", word("else"), KEYWORD), "else"), bl, br);

		GrammarElement ifProd = sequence(if_, zeroOrMore(elseif), zeroOrOne(else_));
		GrammarElement.ProcessedGrammar grammar = ifProd.process();

		// to BNF
		System.out.println("Grammar tree:\n" + grammar.toBnf());
		System.out.println();

		//RailroadDiagram.Browse(grammar);

	}

	//region tools

	static class AstNumber extends Terminal {

		private final int value;

		public AstNumber(Lexem lexem) {
			super(lexem);
			value = Integer.valueOf(lexem.getTerm());

		}

		@Override
		public String toString() {
			return "'n" + value + "'";
		}
	}

	static class AstBinaryExpression extends NonTerminal {

		@AggregateField()
		private ParsemElement left;
		@AggregateField()
		private ParsemElement sign;
		@AggregateField()
		private ParsemElement right;

		public AstBinaryExpression(Lexem lexem) {
			super(lexem);
		}

		@Override
		public String toString() {
			return "(" + sign + " " + left + " " + right + ")";
		}

	}

	static class AstIf extends NonTerminal {

		@AggregateField(identity = "elseif")
		private final List<ParsemElement> elseifs;
		private ParsemElement if_;
		@AggregateField(identity = "else")
		private ParsemElement else_;

		public AstIf(Lexem lexem) {
			super(lexem);
			elseifs = new ArrayList<ParsemElement>();
		}

		@Override
		public String toString() {
			return "if (" + elseifs.toString() + " " + else_ + ")";
		}

	}
	// endregion
}
