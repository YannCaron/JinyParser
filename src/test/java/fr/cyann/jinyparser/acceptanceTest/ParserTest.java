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
import fr.cyann.jinyparser.parsetree.*;
import fr.cyann.jinyparser.lexem.Lexem;
import fr.cyann.jinyparser.lexem.LexemType;
import junit.framework.TestCase;

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

    public void testTrivialParser() {

        String source = "7 + 10 + 4";

        // term
        GrammarElement digit = lexerCharIn("0123456789");
        GrammarElement sign = lexerCharIn("+-*/%");

        // lexer
        GrammarElement number = parsem(AstNumber.class, lexem(NUMBER, repeat(digit)));

        GrammarElement operator = parsem(lexem(OPERATOR, sign));

        // parser
        GrammarElement grammar = sequence(number, repeat(parsem(AstBinaryExpression.class, sequence(operator, number))));

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
        GrammarElement number = parsem(AstNumber.class, lexem(NUMBER, repeat(digit)));

        GrammarElement operator = parsem(lexem(OPERATOR, sign));

        // parser
        GrammarElement grammar = sequence(number, repeat(sequence(operator, parsem(AstBinaryExpression.class, number))));

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
        GrammarElement number = parsem(AstNumber.class, lexem(NUMBER, repeat(digit)));

        GrammarElement operator = parsem(lexem(OPERATOR, sign));

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
        GrammarElement number = parsem(AstNumber.class, lexem(NUMBER, repeat(lexerCharIn("0123456789"))));

        GrammarElement addSign = parsem(lexem(OPERATOR, lexerCharIn("+")));
        GrammarElement minusSign = parsem(lexem(OPERATOR, lexerCharIn("-")));

        GrammarElement addition = parsem(AstBinaryExpression.class, sequence(addSign, number));
        GrammarElement subtraction = parsem(AstBinaryExpression.class, sequence(minusSign, number));

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
        GrammarElement number = parsem(AstNumber.class, lexem(NUMBER, repeat(lexerCharIn("0123456789"))));

        GrammarElement addSign = parsem(lexem(OPERATOR, lexerCharIn("+")));
        GrammarElement multiplySign = parsem(lexem(OPERATOR, lexerCharIn("*")));

        // <multiplication> := <number> [ { '*' <number> } ]
        GrammarElement multiplication = sequence(number, optional(repeat(sequence(multiplySign, parsem(AstBinaryExpression.class, number)))));

        // <addition> := <multiplication> [ { '+' <multiplication> } ]
        GrammarElement addition = sequence(multiplication, optional(repeat(sequence(addSign, parsem(AstBinaryExpression.class, multiplication)))));

        // parse
        GrammarContext c = addition.parse(source);

        System.out.println("Parse tree: " + c.getParseTree());

        assertEquals("('+' ('+' 'n7' ('*' 'n10' 'n4')) 'n7')", c.getParseTree().toString());

    }

    public void testIfParser() {

        String source = "if () {} elseif () {} else {} ";

        // lexer
        GrammarElement pl = lexem(LexemType.SYMBOL, word("("));
        GrammarElement pr = lexem(LexemType.SYMBOL, word(")"));
        GrammarElement bl = lexem(LexemType.SYMBOL, word("{"));
        GrammarElement br = lexem(LexemType.SYMBOL, word("}"));

        GrammarElement if_ = sequence(lexem(KEYWORD, word("if")), pl, pr, parsem(bl), br);
        GrammarElement elseif_ = sequence(lexem(KEYWORD, word("elseif")), pl, pr, parsem(bl), br);
        GrammarElement else_ = sequence(lexem(KEYWORD, word("else")), parsem(bl), br);

        GrammarElement grammar = sequence(if_, optional(repeat(elseif_)), optional(else_));

        // parse
        GrammarContext c = grammar.parse(source);

        System.out.println("Parse tree: " + c.getParseTree());

        //assertEquals("('+' ('+' 'n7' ('*' 'n10' 'n4')) 'n7')", c.getParseTree().toString());

    }

    public void testOperatorLevelWithParenthesisParser() {

        String source = "7 + 10 * (4 + 7)";

        // lexer
        GrammarElement leftParenthesis = lexem(LexemType.SYMBOL, lexerCharIn("("));
        GrammarElement rightParenthesis = lexem(LexemType.SYMBOL, lexerCharIn(")"));
        GrammarElement number = parsem(AstNumber.class, lexem(NUMBER, repeat(lexerCharIn("0123456789"))));

        GrammarElement addSign = parsem(lexem(OPERATOR, lexerCharIn("+")));
        GrammarElement multiplySign = parsem(lexem(OPERATOR, lexerCharIn("*")));

        GrammarElement addition;
        GrammarElement multiplication;
        GrammarNode ident = choice();

        // <multiplication> := <number> [ { '*' <number> } ]
        multiplication = sequence(ident, optional(repeat(sequence(multiplySign, parsem(AstBinaryExpression.class, ident)))));

        // <addition> := <multiplication> [ { '+' <multiplication> } ]
        addition = sequence(multiplication, optional(repeat(sequence(addSign, parsem(AstBinaryExpression.class, multiplication)))));

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
        GrammarElement number = parsem(AstNumber.class, lexem(NUMBER, repeat(lexerCharIn("0123456789"))));

        GrammarElement addSign = parsem(lexem(OPERATOR, lexerCharIn("+")));
        GrammarElement multiplySign = parsem(lexem(OPERATOR, lexerCharIn("*")));

        GrammarNode addition = sequence();
        GrammarNode multiplication = sequence();
        GrammarNode ident = choice();

        // <multiplication> := <ident> [ { '*' <ident> } ]
        multiplication.addAll(ident, optional(repeat(sequence(multiplySign, parsem(AstBinaryExpression.class, ident)))));

        // <addition> := <multiplication> [ { '+' <multiplication> } ]
        addition.addAll(multiplication, optional(repeat(sequence(addSign, parsem(AstBinaryExpression.class, multiplication)))));

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

        private ParsemElement sign, left, right;

        public AstBinaryExpression(Lexem lexem) {
            super(lexem);
        }

        @Override
        public void build(ParsemBuildable context) {
            right = context.popParsem();
            sign = context.popParsem();
            left = context.popParsem();
        }

        @Override
        public void aggregate(FieldCode code, ParsemBuildable context) {
        }

        @Override
        public String toString() {
            return "(" + sign + " " + left + " " + right + ")";
        }

    }

    static class AstIf extends NonTerminal {

        private ParsemElement if_, else_;
        private List<ParsemElement> elseifs;

        public AstIf(Lexem lexem) {
            super(lexem);
        }

        @Override
        public void build(ParsemBuildable context) {
            if_ = context.popParsem();
        }

        @Override
        public void aggregate(FieldCode code, ParsemBuildable context) {
        }

//        @Override
//        public String toString() {
//            return "(" + sign + " " + left + " " + right + ")";
//        }

    }
}
