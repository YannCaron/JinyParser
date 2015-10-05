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
import fr.cyann.jinyparser.parsetree.*;
import fr.cyann.jinyparser.token.Lexem;
import fr.cyann.jinyparser.token.LexemType;
import junit.framework.TestCase;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.*;

/**
 * The DefaultParseContextTest definition.
 */
public class ParserTest extends TestCase {

    private static final LexemType NUMBER = new LexemType("NUMBER");
    private static final LexemType OPERATOR = new LexemType("OPERATOR");

    public void testTrivialParser() {

        String source = "7 + 10 + 4";

        // term
        GrammarElement digit = lexerCharIn("0123456789");
        GrammarElement sign = lexerCharIn("+-*/%");

        // lexer
        GrammarElement number = parsem(AstNumber.BUILDER, lexem(NUMBER, repeat(digit)));

        GrammarElement operator = parsemDummy(lexem(OPERATOR, sign));

        // parser
        GrammarElement grammar = sequence(number, repeat(parsem(AstBinaryExpression.BUILDER, sequence(operator, number))));

        // parse
        GrammarContext c = new GrammarContext(source);
        grammar.parse(c);

        System.out.println("Parse tree: " + c.getParseTree());

        assertEquals("('+' ('+' 'n7' 'n10') 'n4')", c.getParseTree().toString());

    }

    public void testTrivialDefaultParsemParser() {

        String source = "7 + 10 + 4";

        // term
        GrammarElement digit = lexerCharIn("0123456789");
        GrammarElement sign = lexerCharIn("+-*/%");

        // lexer
        GrammarElement number = parsem(AstNumber.BUILDER, lexem(NUMBER, repeat(digit)));

        GrammarElement operator = parsemDummy(lexem(OPERATOR, sign));

        // parser
        GrammarElement grammar = sequence(number, repeat(parsemNonTerminal(3, sequence(operator, number))));

        // parse
        GrammarContext c = new GrammarContext(source);
        grammar.parse(c);

        System.out.println("Parse tree: " + c.getParseTree());

        assertEquals("(('n7' '+' 'n10') '+' 'n4')", c.getParseTree().toString());

    }

    public void testLLkParser() {

        String source = "7- 10+4";

        // lexer
        GrammarElement number = parsem(AstNumber.BUILDER, lexem(NUMBER, repeat(lexerCharIn("0123456789"))));

        GrammarElement addSign = parsemDummy(lexem(OPERATOR, lexerCharIn("+")));
        GrammarElement minusSign = parsemDummy(lexem(OPERATOR, lexerCharIn("-")));

        GrammarElement addition = parsem(AstBinaryExpression.BUILDER, sequence(addSign, number));
        GrammarElement subtraction = parsem(AstBinaryExpression.BUILDER, sequence(minusSign, number));

        // parser
        GrammarElement grammar = sequence(number, repeat(choice(addition, subtraction)));

        // parse
        GrammarContext c = new GrammarContext(source);
        grammar.parse(c);

        System.out.println("Parse tree: " + c.getParseTree());

        //assertEquals("('-' ('+' 'n7' 'n10') 'n4')", c.getParseTree().toString());

    }

    static class AstNumber extends DefaultTerminal<Integer> {

        public static final ParsemBuilder BUILDER = new ParsemBuilder() {
            @Override
            public ParsemElement buildParsem(ParsemBuildable context) {
                Lexem lexem = context.getCurrentLexem();
                return new AstNumber(lexem, Integer.valueOf(lexem.getTerm()));
            }
        };

        public AstNumber(Lexem lexem, Integer value) {
            super(lexem, value);
        }

        @Override
        public String toString() {
            return "'n" + getValue() + "'";
        }
    }

    static class AstBinaryExpression extends NonTerminal {

        public static final ParsemBuilder BUILDER = new ParsemBuilder() {
            @Override
            public ParsemElement buildParsem(ParsemBuildable context) {
                ParsemElement right = context.popParsem();
                ParsemElement operator = context.popParsem();
                ParsemElement left = context.popParsem();

                return new AstBinaryExpression(left.getLexem(), right.getLexem(), operator, left, right);
            }
        };
        private final ParsemElement sign, left, right;

        public AstBinaryExpression(Lexem lexemBegin, Lexem lexemEnd, ParsemElement sign, ParsemElement left, ParsemElement right) {
            super(lexemBegin, lexemEnd);
            this.sign = sign;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return "(" + sign + " " + left + " " + right + ")";
        }


    }

}
