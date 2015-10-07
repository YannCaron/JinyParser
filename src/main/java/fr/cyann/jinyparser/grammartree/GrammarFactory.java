package fr.cyann.jinyparser.grammartree;

import fr.cyann.jinyparser.parsetree.DefaultNonTerminal;
import fr.cyann.jinyparser.parsetree.Dummy;
import fr.cyann.jinyparser.parsetree.ParsemBuilder;
import fr.cyann.jinyparser.token.LexemType;

/**
 * The ${CLASS_NAME} class.
 * Created by cyann on 02/10/15.
 */
public final class GrammarFactory {

    private GrammarFactory() {
        throw new RuntimeException("Static class cannot be instanced");
    }

    // region node

    /**
     * Create a new sequence grammar element.
     * @return the new grammar element.
     */
    public static Sequence sequence(String name) {
        return new Sequence(name);
    }

    /**
     * Create a new sequence grammar element.
     * @param element1 the first element of he list (mandatory).
     * @param elements list of children.
     * @return the new grammar element.
     */
    public static Sequence sequence(GrammarElement element1, GrammarElement... elements) {
        Sequence sequence = new Sequence();
        sequence.addAll(element1, elements);
        return sequence;
    }

    /**
     * Create a new choice grammar element.
     * @return the new grammar element.
     */
    public static Choice choice(String name) {
        return new Choice(name);
    }

    /**
     * Create a new choice grammar element.
     *
     * @param element1 the first element of the list (mandatory).
     * @param elements list of children.
     * @return the new grammar element.
     */
    public static Choice choice(GrammarElement element1, GrammarElement... elements) {
        Choice choice = new Choice();
        choice.addAll(element1, elements);
        return choice;
    }

    // endregion

    // region decorator

    /**
     * Create a new repeat grammar element.
     * @param decorated the grammar element to decorate.
     * @return the new grammar element.
     */
    public static Repeat repeat(GrammarElement decorated) {
        return new Repeat(decorated);
    }

    /**
     * Create a new optional grammar element.
     * @param decorated the grammar element to decorate.
     * @return the new grammar element.
     */
    public static Optional optional(GrammarElement decorated) {
        return new Optional(decorated);
    }

    /**
     * Create a new token producer grammar element.
     * @param lexemType the token type of the token to produce.
     * @param decorated the grammar that decide if lexem will be produced.
     * @return the new grammar element.
     */
    public static LexemProducer lexemProducer(LexemType lexemType, GrammarElement decorated) {
        return new LexemProducer(lexemType, decorated);
    }

    /**
     * Grammar element that produce lexem (build token on lexer).<br>
     * Create a new token producer grammar element that manage separators.
     * @param lexemType the token type of the token to produce.
     * @param decorated the grammar element to decorate.
     * @return the new grammar element.
     */
    public static GrammarElement lexem(LexemType lexemType, GrammarElement decorated) {
        return new SeparatorsManager(new LexemProducer(lexemType, decorated));
    }

    /**
     * Grammar element that produce parsem (build parse tree element in the stack).
     * @param builder the token type of the token to produce.
     * @param decorated the grammar that decide if parsem will be produced.
     * @return the new grammar element.
     */
    public static GrammarElement parsem(ParsemBuilder builder, GrammarElement decorated) {
        return new ParsemProducer(builder, decorated);
    }

    /**
     * Grammar element that produce a default terminal parsem.
     * @param decorated the grammar that decide if parsem will be produced.
     * @return the new grammar element.
     */
    public static GrammarElement parsemDummy(GrammarElement decorated) {
        return new ParsemProducer(Dummy.BUILDER, decorated);
    }

    /**
     * Grammar element that produce a default non terminal parsem.
     * @param decorated the grammar that decide if parsem will be produced.
     * @return the new grammar element.
     */
    public static GrammarElement parsemNonTerminal(int length, GrammarElement decorated) {
        return new ParsemProducer(DefaultNonTerminal.BUILDER(length), decorated);
    }
    /**
     * Create a new separator manager grammar element.
     * @param decorated the grammar element to decorate.
     * @return the new grammar element.
     */
    public static SeparatorsManager separatorsManager(GrammarElement decorated) {
        return new SeparatorsManager(decorated);
    }

    /**
     * Create a new line incrementer grammar element.
     * @param decorated the grammar element to decorate.
     * @return the new grammar element.
     */
    public static LineIncrementer lineIncrementer(GrammarElement decorated) {
        return new LineIncrementer(decorated);
    }

    // endregion

    // region lexer

    /**
     * Create a new lexer char recognizer grammar element.
     * @param terms the list of character to recognize.
     * @return the new grammar element.
     */
    public static LexerCharIn lexerCharIn(String terms) {
        return new LexerCharIn(terms);
    }

    // endregion

}
