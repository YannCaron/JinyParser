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

    private static final LexemType NUMBER = new LexemType("produceNumber");
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
        GrammarElement number = produceNumber(repeat(digit));
        GrammarElement numberRight = produceBinaryExpresion(number);

        GrammarElement operator = produce(sign, OPERATOR);

        // parser
        // grammar := <produceNumber> { <operator> <produceNumber>}
        GrammarElement grammar = sequence(number, repeat(sequence(operator, numberRight)));

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
        GrammarElement number = produceNumber(repeat(digit));
        GrammarElement numberRight = produceBinaryExpresion(number);

        GrammarElement operator = produce(sign, OPERATOR);

        // parser
        // grammar := <produceNumber> { '+' <produceNumber>}
        GrammarElement grammar = sequence(number, repeat(sequence(operator, numberRight)));

        // parse
        GrammarContext c;
        try {
            c = grammar.parse(source);
            fail("must raise an error !");
        } catch (Exception e) {
        }

    }

    public void testTrivialDefaultParsemParser() {

        String source = "7 + 10 + 4";

        // term
        GrammarElement digit = lexerCharIn("0123456789");
        GrammarElement sign = lexerCharIn("+-*/%");

        // lexer
        GrammarElement number = create(lexem(repeat(digit), NUMBER), AstNumber.class);

        GrammarElement operator = create(lexem(sign, OPERATOR));

        // parser
        GrammarElement grammar = sequence(number, repeat(parsemNonTerminal(sequence(operator, number), 3)));

        // parse
        GrammarContext c = grammar.parse(source);

        System.out.println("Parse tree: " + c.getParseTree());

        assertEquals("(('n7' '+' 'n10') '+' 'n4')", c.getParseTree().toString());

    }

    public void testLLkParser() {

        String source = "7 - 10 + 4";

        // lexer
        GrammarElement number = produceNumber(repeat(lexerCharIn("0123456789")));

        GrammarElement addSign = create(lexem(lexerCharIn("+"), OPERATOR));
        GrammarElement minusSign = create(lexem(lexerCharIn("-"), OPERATOR));

        GrammarElement addition = sequence(addSign, produceBinaryExpresion(number));
        GrammarElement subtraction = sequence(minusSign, produceBinaryExpresion(number));

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
        GrammarElement number = produceNumber(repeat(lexerCharIn("0123456789")));

        GrammarElement addSign = create(lexem(lexerCharIn("+"), OPERATOR));
        GrammarElement multiplySign = create(lexem(lexerCharIn("*"), OPERATOR));

        // <multiplication> := <produceNumber> [ { '*' <produceNumber> } ]
        GrammarElement multiplication = sequence(number, optional(repeat(sequence(multiplySign, produceBinaryExpresion(number)))));

        // <addition> := <multiplication> [ { '+' <multiplication> } ]
        GrammarElement addition = sequence(multiplication, optional(repeat(sequence(addSign, produceBinaryExpresion(multiplication)))));

        // parse
        GrammarContext c = addition.parse(source);

        System.out.println("Parse tree: " + c.getParseTree());

        assertEquals("('+' ('+' 'n7' ('*' 'n10' 'n4')) 'n7')", c.getParseTree().toString());

    }

    public void testOperatorLevelWithParenthesisParser() {

        String source = "7 + 10 * (4 + 7)";

        // lexer
        GrammarElement leftParenthesis = lexem(lexerCharIn("("), LexemType.SYMBOL);
        GrammarElement rightParenthesis = lexem(lexerCharIn(")"), LexemType.SYMBOL);
        GrammarElement number = produceNumber(repeat(lexerCharIn("0123456789")));

        GrammarElement addSign = create(lexem(lexerCharIn("+"), OPERATOR));
        GrammarElement multiplySign = create(lexem(lexerCharIn("*"), OPERATOR));

        GrammarElement addition;
        GrammarElement multiplication;
        GrammarNode ident = choice();

        // <multiplication> := <produceNumber> [ { '*' <produceNumber> } ]
        multiplication = sequence(ident, optional(repeat(sequence(multiplySign, produceBinaryExpresion(ident)))));

        // <addition> := <multiplication> [ { '+' <multiplication> } ]
        addition = sequence(multiplication, optional(repeat(sequence(addSign, produceBinaryExpresion(multiplication)))));

        // <num> := <produceNumber> | '(' <addition> ')'
        ident.addAll(number, sequence(leftParenthesis, addition, rightParenthesis));

        // parser
        GrammarElement grammar = addition;

        // parse
        GrammarContext c = grammar.parse(source);

        System.out.println("Parse tree: " + c.getParseTree());

        assertEquals("('+' 'n7' ('*' 'n10' ('+' 'n4' 'n7')))", c.getParseTree().toString());

    }

    public void testIfParser() {

        String source = "if () {} elseif () {} elseif () {} else {} ";

        // lexer
        GrammarElement pl = lexem(word("("), LexemType.SYMBOL);
        GrammarElement pr = lexem(word(")"), LexemType.SYMBOL);
        GrammarElement bl = lexem(word("{"), LexemType.SYMBOL);
        GrammarElement br = lexem(word("}"), LexemType.SYMBOL);

        GrammarElement if_ = sequence(produce(word("if"), KEYWORD, AstIf.class), pl, pr, bl, br);
        GrammarElement elseif = sequence(produceAndDrop(word("elseif"), KEYWORD, "elseif"), pl, pr, bl, br);
        GrammarElement else_ = sequence(produceAndDrop(word("else"), KEYWORD, "else"), bl, br);

        GrammarElement grammar = sequence(if_, optional(repeat(elseif)), optional(else_));

        // parse
        GrammarContext c = grammar.parse(source);

        System.out.println("Parse tree: " + c.getParseTree());

        assertEquals("if (['elseif', 'elseif'] 'else')", c.getParseTree().toString());

    }

    public void testGrammarToStringLoop() {

        // lexer
        GrammarElement leftParenthesis = lexem(lexerCharIn("("), LexemType.SYMBOL);
        GrammarElement rightParenthesis = lexem(lexerCharIn(")"), LexemType.SYMBOL);
        GrammarElement number = produceNumber(repeat(lexerCharIn("0123456789")));

        GrammarElement addSign = create(lexem(lexerCharIn("+"), OPERATOR));
        GrammarElement multiplySign = create(lexem(lexerCharIn("*"), OPERATOR));

        GrammarNode addition = sequence();
        GrammarNode multiplication = sequence();
        GrammarNode ident = choice();

        // <multiplication> := <ident> [ { '*' <ident> } ]
        multiplication.addAll(ident, optional(repeat(sequence(multiplySign, produceBinaryExpresion(ident)))));

        // <addition> := <multiplication> [ { '+' <multiplication> } ]
        addition.addAll(multiplication, optional(repeat(sequence(addSign, produceBinaryExpresion(multiplication)))));

        // <ident> := <produceNumber> | '(' <addition> ')'
        ident.addAll(number, sequence(leftParenthesis, addition, rightParenthesis));

        System.out.println(addition);

    }

    //region tools

    private GrammarElement produceNumber(GrammarElement decorated) {
        return produce(decorated, NUMBER, AstNumber.class);
    }

    private GrammarElement produceBinaryExpresion(GrammarElement decorated) {
        return produceAndCatch(decorated, NUMBER, AstBinaryExpression.class, "left", "sign", "right");
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

    // endregion

}
