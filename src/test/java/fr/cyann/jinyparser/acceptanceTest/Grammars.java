package fr.cyann.jinyparser.acceptanceTest;/**
 * Copyright (C) 12/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.grammartree.GrammarElement;
import fr.cyann.jinyparser.grammartree.GrammarProduction;
import fr.cyann.jinyparser.grammartree.LexemCreator;
import fr.cyann.jinyparser.grammartree.Recursive;
import fr.cyann.jinyparser.lexem.Lexem;
import fr.cyann.jinyparser.lexem.LexemType;
import fr.cyann.jinyparser.parsetree.*;

import java.util.ArrayList;
import java.util.List;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.*;

/**
 * The Grammars definition.
 */
public class Grammars {

	public static final LexemType NUMBER = new LexemType("produceNumber");
	public static final LexemType OPERATOR = new LexemType("operator");
	public static final LexemType KEYWORD = new LexemType("keyword");

	private Grammars() {
		throw new RuntimeException("Static class cannot be instantiated !");
	}

	public static <C extends VisitorContext> GrammarElement.ProcessedGrammar arithmeticWithLevelAndParenthesis(ParsemVisitor<AstNumber, C> numberVisitor, ParsemVisitor<AstBinaryExpression, C> additionVisitor, ParsemVisitor<AstBinaryExpression, C> multiplicationVisitor) {
		// lexer
		LexemCreator leftParenthesis = lexem(LexemType.SYMBOL, charIn("("));
		LexemCreator rightParenthesis = lexem(LexemType.SYMBOL, charIn(")"));
		LexemCreator lexNum = lexem(NUMBER, oneOrMore(charIn('0', '9')));
		LexemCreator lexAdd = lexem(OPERATOR, charIn("+"));
		LexemCreator lexMult = lexem(OPERATOR, charIn("*"));

		// terminal
		GrammarElement number = terminal("Number", AstNumber.class, lexNum).setVisitor(numberVisitor);
		GrammarElement addSign = terminal("AddSign", lexAdd);
		GrammarElement multiplySign = terminal("MultiplySign", lexMult);

		// recursive
		Recursive multiplication = recursive("Term");
		Recursive addition = recursive("Term");
		Recursive term = recursive("Term");

		// non terminal

		// <multiplication> := <produceNumber> [ { '*' <produceNumber> } ]
		multiplication.setGrammar(nonTerminal("Multiplication", AstBinaryExpression.class,
				sequence(aggregate("left", term), zeroOrOne(sequence(aggregate("sign", multiplySign), create("right", multiplication))))).setVisitor(multiplicationVisitor));

		// <addition> := <multiplication> [ { '+' <multiplication> } ]
		addition.setGrammar(nonTerminal("Addition", AstBinaryExpression.class,
				sequence(aggregate("left", multiplication), zeroOrOne(sequence(aggregate("sign", addSign), create("right", addition))))).setVisitor(additionVisitor));

		// <term> := <number> | '(' <addition> ')'
		term.setGrammar(choice(number, sequence(leftParenthesis, addition, rightParenthesis)));

		// process
		return addition.process();
	}

	public static <C extends VisitorContext> GrammarElement.ProcessedGrammar arithmeticWithLevelAndParenthesisDefault(ParsemVisitor<DefaultTerminal, C> numberVisitor, ParsemVisitor<DefaultNonTerminal, C> additionVisitor, ParsemVisitor<DefaultNonTerminal, C> multiplicationVisitor) {
		// lexer
		LexemCreator leftParenthesis = lexem(LexemType.SYMBOL, charIn("("));
		LexemCreator rightParenthesis = lexem(LexemType.SYMBOL, charIn(")"));
		LexemCreator lexNum = lexem(NUMBER, oneOrMore(charIn('0', '9')));
		LexemCreator lexAdd = lexem(OPERATOR, charIn("+"));
		LexemCreator lexMult = lexem(OPERATOR, charIn("*"));

		// terminal
		GrammarElement number = terminal("Number", lexNum).setVisitor(numberVisitor);
		GrammarElement addSign = terminal("AddSign", lexAdd);
		GrammarElement multiplySign = terminal("MultiplySign", lexMult);

		// recursive
		Recursive term = recursive("Term");

		// non terminal

		// <multiplication> := <produceNumber> [ { '*' <produceNumber> } ]
		GrammarProduction multiplication = nonTerminal("Multiplication",
				sequence(aggregate(term), zeroOrMore(sequence(aggregate(multiplySign), create(term))))).setVisitor(multiplicationVisitor);

		// <addition> := <multiplication> [ { '+' <multiplication> } ]
		GrammarProduction addition = nonTerminal("Addition",
				sequence(aggregate(multiplication), zeroOrMore(sequence(aggregate(addSign), create(multiplication))))).setVisitor(additionVisitor);

		// <term> := <number> | '(' <addition> ')'
		term.setGrammar(choice(number, sequence(leftParenthesis, addition, rightParenthesis)));

		// process
		return addition.process();
	}

	public static GrammarElement.ProcessedGrammar dummyIf() {

		// lexer
		LexemCreator pl = lexem(LexemType.SYMBOL, word("("));
		LexemCreator pr = lexem(LexemType.SYMBOL, word(")"));
		LexemCreator bl = lexem(LexemType.SYMBOL, word("{"));
		LexemCreator br = lexem(LexemType.SYMBOL, word("}"));
		LexemCreator lexIf = lexem(KEYWORD, word("if"));
		LexemCreator lexElseIf = lexem(KEYWORD, word("elseif"));
		LexemCreator lexElse = lexem(KEYWORD, word("else"));

		// terminal / non terminal
		GrammarElement if_ = sequence(create("if", terminal("if", lexIf)), pl, pr, bl, br);
		GrammarElement elseif = sequence(aggregate("elseif", terminal("elseIf", lexElseIf)), pl, pr, bl, br);
		GrammarElement else_ = sequence(aggregate("else", terminal("else", lexElse)), bl, br);

		GrammarElement ifProd = nonTerminal("If", AstIf.class, sequence(if_, zeroOrMore(elseif), zeroOrOne(else_)));

		return ifProd.process();
	}

	public static class AstNumber extends Terminal {

		private final int value;

		public AstNumber(Lexem lexem) {
			super(lexem);
			value = Integer.valueOf(lexem.getTerm());

		}

		public int getValue() {
			return value;
		}

		@Override
		public String toString() {
			return "'n" + value + "'";
		}
	}

	public static class AstBinaryExpression extends NonTerminal {

		@AggregateField()
		private ParsemElement left;
		@AggregateField()
		private ParsemElement sign;
		@AggregateField()
		private ParsemElement right;

		public ParsemElement getLeft() {
			return left;
		}

		public ParsemElement getSign() {
			return sign;
		}

		public ParsemElement getRight() {
			return right;
		}

		@Override
		public String toString() {
			return "(" + sign + " " + left + " " + right + ")";
		}

	}

	public static class AstIf extends NonTerminal {

		@AggregateField(identity = "elseif")
		private final List<ParsemElement> elseifs;
		@AggregateField(identity = "if")
		private ParsemElement if_;
		@AggregateField(identity = "else")
		private ParsemElement else_;

		public AstIf() {
			elseifs = new ArrayList<ParsemElement>();
		}

		@Override
		public String toString() {
			return "if (" + elseifs.toString() + " " + else_ + ")";
		}

	}

}
