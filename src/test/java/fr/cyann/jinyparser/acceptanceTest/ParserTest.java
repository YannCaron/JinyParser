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
import fr.cyann.jinyparser.grammartree.GrammarNode;
import fr.cyann.jinyparser.lexem.Lexem;
import fr.cyann.jinyparser.lexem.LexemType;
import fr.cyann.jinyparser.parsetree.*;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.*;

/**
 * The DefaultParseContextTest definition.
 */
public class ParserTest extends TestCase {

    private static final LexemType NUMBER = new LexemType("number");
    private static final LexemType OPERATOR = new LexemType("operator");
    private static final LexemType KEYWORD = new LexemType("keyword");

    private static final FieldCode ELSEIF_STMT = new FieldCode("elseif statement");
    private static final FieldCode ELSE_STMT = new FieldCode("else statement");

    public void testTrivialParser() {

        String source = "7 + 10 + 4";

        // term
        GrammarElement digit = lexerCharIn("0123456789");
        GrammarElement sign = lexerCharIn("+-*/%");

        // lexer
        GrammarElement number = create(AstNumber.class, lexem(NUMBER, repeat(digit)));

        GrammarElement operator = create(lexem(OPERATOR, sign));

        // parser
        GrammarElement grammar = sequence(number, repeat(create(AstBinaryExpression.class, sequence(operator, number))));

        // parse
        GrammarContext c = grammar.parse(source);

        System.out.println("Parse tree: " + c.getParseTree());

        assertEquals("('+' ('+' 'n7' 'n10') 'n4')", c.getParseTree().toString());

    }

    public void testTrivialNotParsing() {

        String source = "7 + 10 - 4";

        // term
        GrammarElement digit = lexerCharIn("0123456789");
        GrammarElement sign = lexerCharIn("+");

        // lexer
        GrammarElement number = create(AstNumber.class, lexem(NUMBER, repeat(digit)));

        GrammarElement operator = create(lexem(OPERATOR, sign));

        // parser
        GrammarElement grammar = sequence(number, repeat(sequence(operator, create(AstBinaryExpression.class, number))));

        // parse
        GrammarContext c = grammar.parse(source);

        System.out.println("Parse tree: " + c.getParseTree());

        //assertEquals("('+' ('+' 'n7' 'n10') 'n4')", c.getParseTree().toString());

    }

    public void testTrivialDefaultParsemParser() {

        String source = "7 + 10 + 4";

        // term
        GrammarElement digit = lexerCharIn("0123456789");
        GrammarElement sign = lexerCharIn("+-*/%");

        // lexer
        GrammarElement number = create(AstNumber.class, lexem(NUMBER, repeat(digit)));

        GrammarElement operator = create(lexem(OPERATOR, sign));

        // parser
        GrammarElement grammar = sequence(number, repeat(parsemNonTerminal(3, sequence(operator, number))));

        // parse
        GrammarContext c = grammar.parse(source);

        System.out.println("Parse tree: " + c.getParseTree());

        assertEquals("(('n7' '+' 'n10') '+' 'n4')", c.getParseTree().toString());

    }

    public void testLLkParser() {

        String source = "7 - 10 + 4";

        // lexer
        GrammarElement number = create(AstNumber.class, lexem(NUMBER, repeat(lexerCharIn("0123456789"))));

        GrammarElement addSign = create(lexem(OPERATOR, lexerCharIn("+")));
        GrammarElement minusSign = create(lexem(OPERATOR, lexerCharIn("-")));

        GrammarElement addition = create(AstBinaryExpression.class, sequence(addSign, number));
        GrammarElement subtraction = create(AstBinaryExpression.class, sequence(minusSign, number));

        // parser
        GrammarElement grammar = sequence(number, repeat(choice(addition, subtraction)));

        // parse
        GrammarContext c = grammar.parse(source);

        System.out.println("Parse tree: " + c.getParseTree());

        assertEquals("('+' ('-' 'n7' 'n10') 'n4')", c.getParseTree().toString());

    }

    public void testOperatorLevelParser() {

        String source = "7 + 10 * 4 + 7";

        // lexer
        GrammarElement number = create(AstNumber.class, lexem(NUMBER, repeat(lexerCharIn("0123456789"))));

        GrammarElement addSign = create(lexem(OPERATOR, lexerCharIn("+")));
        GrammarElement multiplySign = create(lexem(OPERATOR, lexerCharIn("*")));

        // <multiplication> := <number> [ { '*' <number> } ]
        GrammarElement multiplication = sequence(number, optional(repeat(sequence(multiplySign, create(AstBinaryExpression.class, number)))));

        // <addition> := <multiplication> [ { '+' <multiplication> } ]
        GrammarElement addition = sequence(multiplication, optional(repeat(sequence(addSign, create(AstBinaryExpression.class, multiplication)))));

        // parse
        GrammarContext c = addition.parse(source);

        System.out.println("Parse tree: " + c.getParseTree());

        assertEquals("('+' ('+' 'n7' ('*' 'n10' 'n4')) 'n7')", c.getParseTree().toString());

    }

    public void testOperatorLevelParser2() {

        String source = "7 + 10";

        // lexer
        GrammarElement number = produce(NUMBER, AstNumber.class, repeat(lexerCharIn("0123456789")));

        GrammarElement addSign = produce(OPERATOR, lexerCharIn("+"));

        // <addition> := <number> [ { '+' <number> } ]
        GrammarElement addition = sequence(number, optional(repeat(sequence(addSign, createAndCatch(number, AstBinaryExpression.class, "left", "sign", "right")))));

        // parse
        GrammarContext c = addition.parse(source);

        System.out.println("Parse tree: " + c.getParseTree());

        assertEquals("('+' 'n7' 'n10')", c.getParseTree().toString());

    }

    public void testIfParser() {

        String source = "if () {} elseif () {} elseif () {} else {} ";

        // lexer
        GrammarElement pl = lexem(LexemType.SYMBOL, word("("));
        GrammarElement pr = lexem(LexemType.SYMBOL, word(")"));
        GrammarElement bl = lexem(LexemType.SYMBOL, word("{"));
        GrammarElement br = lexem(LexemType.SYMBOL, word("}"));

        GrammarElement if_ = sequence(produce(KEYWORD, AstIf.class, word("if")), pl, pr, bl, br);
        GrammarElement elseif = sequence(produceAndDrop(KEYWORD, "elseif", word("elseif")), pl, pr, bl, br);
        GrammarElement else_ = sequence(produceAndDrop(KEYWORD, "else", word("else")), bl, br);

        GrammarElement grammar = sequence(if_, optional(repeat(elseif)), optional(else_));

        // parse
        GrammarContext c = grammar.parse(source);

        System.out.println("Parse tree: " + c.getParseTree());

        assertEquals("if (['elseif', 'elseif'] 'else')", c.getParseTree().toString());

    }

    public void testOperatorLevelWithParenthesisParser() {

        String source = "7 + 10 * (4 + 7)";

        // lexer
        GrammarElement leftParenthesis = lexem(LexemType.SYMBOL, lexerCharIn("("));
        GrammarElement rightParenthesis = lexem(LexemType.SYMBOL, lexerCharIn(")"));
        GrammarElement number = create(AstNumber.class, lexem(NUMBER, repeat(lexerCharIn("0123456789"))));

        GrammarElement addSign = create(lexem(OPERATOR, lexerCharIn("+")));
        GrammarElement multiplySign = create(lexem(OPERATOR, lexerCharIn("*")));

        GrammarElement addition;
        GrammarElement multiplication;
        GrammarNode ident = choice();

        // <multiplication> := <number> [ { '*' <number> } ]
        multiplication = sequence(ident, optional(repeat(sequence(multiplySign, create(AstBinaryExpression.class, ident)))));

        // <addition> := <multiplication> [ { '+' <multiplication> } ]
        addition = sequence(multiplication, optional(repeat(sequence(addSign, create(AstBinaryExpression.class, multiplication)))));

        // <num> := <number> | '(' <addition> ')'
        ident.addAll(number, sequence(leftParenthesis, addition, rightParenthesis));

        // parser
        GrammarElement grammar = addition;

        // parse
        GrammarContext c = grammar.parse(source);

        System.out.println("Parse tree: " + c.getParseTree());

        assertEquals("('+' 'n7' ('*' 'n10' ('+' 'n4' 'n7')))", c.getParseTree().toString());

    }

    public void testGrammarToStringLoop() {

        // lexer
        GrammarElement leftParenthesis = lexem(LexemType.SYMBOL, lexerCharIn("("));
        GrammarElement rightParenthesis = lexem(LexemType.SYMBOL, lexerCharIn(")"));
        GrammarElement number = create(AstNumber.class, lexem(NUMBER, repeat(lexerCharIn("0123456789"))));

        GrammarElement addSign = create(lexem(OPERATOR, lexerCharIn("+")));
        GrammarElement multiplySign = create(lexem(OPERATOR, lexerCharIn("*")));

        GrammarNode addition = sequence();
        GrammarNode multiplication = sequence();
        GrammarNode ident = choice();

        // <multiplication> := <ident> [ { '*' <ident> } ]
        multiplication.addAll(ident, optional(repeat(sequence(multiplySign, create(AstBinaryExpression.class, ident)))));

        // <addition> := <multiplication> [ { '+' <multiplication> } ]
        addition.addAll(multiplication, optional(repeat(sequence(addSign, create(AstBinaryExpression.class, multiplication)))));

        // <ident> := <number> | '(' <addition> ')'
        ident.addAll(number, sequence(leftParenthesis, addition, rightParenthesis));

        System.out.println(addition);

    }

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

        @AggregateField("left")
        private ParsemElement left;
        @AggregateField("sign")
        private ParsemElement sign;
        @AggregateField("right")
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

        @AggregateField("elseif")
        private final List<ParsemElement> elseifs;
        private ParsemElement if_;
        @AggregateField("else")
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
}
