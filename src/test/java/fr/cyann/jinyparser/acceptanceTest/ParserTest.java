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
import fr.cyann.jinyparser.grammartree.GrammarRecursive;
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
        GrammarElement digit = charIn("0123456789");
        GrammarElement sign = charIn("+-*/%");

        // lexer
	    GrammarElement number = produce(repeat(digit), NUMBER, AstNumber.class).setVisitor(new ParsemVisitor<AstNumber>() {
		    @Override
		    public void visit(AstNumber parsem) {
			    System.out.println(parsem.value);
		    }
	    });

	    GrammarElement numberRight = catcher(catcher(catcher(produce(number, NUMBER, AstBinaryExpression.class).setVisitor(new ParsemVisitor<AstBinaryExpression>() {
		    @Override
		    public void visit(AstBinaryExpression parsem) {
			    parsem.left.visit();
			    System.out.println(parsem.sign);
			    parsem.right.visit();
		    }
	    }), "right"), "sign"), "left");

        GrammarElement operator = produce(sign, OPERATOR);

        // parser
        // grammar := <produceNumber> { <operator> <produceNumber>}
        GrammarElement grammar = sequence(number, repeat(sequence(operator, numberRight)));

        // parse
        GrammarContext c = grammar.parse(source);

        System.out.println("Parse tree: " + c.getParseTree());

	    assertEquals("('+' ('+' 'n7' 'n10') 'n4')", c.getParseTree().toString());

	    // visite result
	    c.getParseTree().visit();

    }

    public void testTrivialParsingError() {

        String source = "7 + 10 - 4";

        // term
        GrammarElement digit = charIn("0123456789");
        GrammarElement sign = charIn("+");

        // lexer
        GrammarElement number = produceNumber(repeat(digit));
        GrammarElement numberRight = produceBinaryExpression(number);

        GrammarElement operator = produce(sign, OPERATOR);

        // parser
        // grammar := <produceNumber> { '+' <produceNumber>}
        GrammarElement grammar = sequence(number, repeat(sequence(operator, numberRight)));

        // parse
        try {
            grammar.parse(source);
            fail("must raise an error !");
        } catch (JinyException e) {
            System.out.println("success");
        }

    }

    public void testTrivialDefaultParsemParser() {

        String source = "7 + 10 - 4";

        // term
        GrammarElement digit = charIn("0123456789");
        GrammarElement sign = charIn("+-*%/");

        // lexer
        GrammarElement number = produce(repeat(digit), NUMBER);

        GrammarElement operator = produce(sign, OPERATOR);

        // parser
        GrammarElement grammar = sequence(number, repeat(sequence(operator, produceAndCatch(number, NUMBER, 3))));

        // parse
        GrammarContext c = grammar.parse(source);

        System.out.println("Parse tree: " + c.getParseTree());

        assertEquals("(('7' '+' '10') '-' '4')", c.getParseTree().toString());

    }

    public void testLLkParser() {

        String source = "7 - 10 + 4";

        // lexer
        GrammarElement number = produceNumber(repeat(charIn("0123456789")));

        GrammarElement addSign = produce(charIn("+"), OPERATOR);
        GrammarElement minusSign = produce(charIn("-"), OPERATOR);

        GrammarElement addition = sequence(addSign, produceBinaryExpression(number));
        GrammarElement subtraction = sequence(minusSign, produceBinaryExpression(number));

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
        GrammarElement number = produceNumber(repeat(charIn("0123456789")));

        GrammarElement addSign = create(lexem(charIn("+"), OPERATOR));
        GrammarElement multiplySign = create(lexem(charIn("*"), OPERATOR));

        // <multiplication> := <produceNumber> [ { '*' <produceNumber> } ]
        GrammarElement multiplication = sequence(number, optional(repeat(sequence(multiplySign, produceBinaryExpression(number)))));

        // <addition> := <multiplication> [ { '+' <multiplication> } ]
        GrammarElement addition = sequence(multiplication, optional(repeat(sequence(addSign, produceBinaryExpression(multiplication)))));

        // parse
        GrammarContext c = addition.parse(source);

        System.out.println("Parse tree: " + c.getParseTree());

        assertEquals("('+' ('+' 'n7' ('*' 'n10' 'n4')) 'n7')", c.getParseTree().toString());

    }

    public void testOperatorLevelWithParenthesisParser() {

        String source = "7 + 10 * (4 + 7)";

        // lexer
        GrammarElement leftParenthesis = lexem(charIn("("), LexemType.SYMBOL);
        GrammarElement rightParenthesis = lexem(charIn(")"), LexemType.SYMBOL);
        GrammarElement number = produceNumber(repeat(charIn("0123456789")));

        GrammarElement addSign = produce(charIn("+"), OPERATOR);
        GrammarElement multiplySign = produce(charIn("*"), OPERATOR);

	    GrammarRecursive addition = recursive("Addition");
	    GrammarRecursive multiplication = recursive("Multiplication");
	    GrammarRecursive term = recursive("Term");

        // <addition> := <multiplication> [ { '+' <multiplication> } ]
	    addition.setGrammar(sequence(multiplication, optional(repeat(sequence(addSign, produceBinaryExpression(multiplication))))));

	    // <multiplication> := <produceNumber> [ { '*' <produceNumber> } ]
	    multiplication.setGrammar(sequence(term, optional(repeat(sequence(multiplySign, produceBinaryExpression(term))))));

	    // <num> := <number> | '(' <addition> ')'
	    term.setGrammar(choice(number, sequence(leftParenthesis, addition, rightParenthesis)));

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

	    System.out.println("Grammar tree: " + grammar.toString());
	    System.out.println("Parse tree: " + c.getParseTree());

        assertEquals("if (['elseif', 'elseif'] 'else')", c.getParseTree().toString());

    }

    public void testGrammarToStringLoop() {

        // lexer
        GrammarElement leftParenthesis = lexem(charIn("("), LexemType.SYMBOL);
        GrammarElement rightParenthesis = lexem(charIn(")"), LexemType.SYMBOL);
        GrammarElement number = produceNumber(repeat(charIn("0123456789")));

        GrammarElement addSign = create(lexem(charIn("+"), OPERATOR));
        GrammarElement multiplySign = create(lexem(charIn("*"), OPERATOR));

	    GrammarRecursive addition = recursive("Addition");
	    GrammarRecursive multiplication = recursive("Multiplication");
	    GrammarRecursive term = recursive("Term");

        // <multiplication> := <ident> [ { '*' <ident> } ]
	    multiplication.setGrammar(sequence(term, optional(repeat(sequence(multiplySign, produceBinaryExpression(term))))));

        // <addition> := <multiplication> [ { '+' <multiplication> } ]
	    addition.setGrammar(sequence(optional(repeat(sequence(addSign, produceBinaryExpression(multiplication))))));

        // <ident> := <produceNumber> | '(' <addition> ')'
	    term.setGrammar(choice(number, sequence(leftParenthesis, addition, rightParenthesis)));

        System.out.println(addition);

    }

    //region tools

    private GrammarElement produceNumber(GrammarElement decorated) {
        return produce(decorated, NUMBER, AstNumber.class);
    }

    private GrammarElement produceBinaryExpression(GrammarElement decorated) {
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
