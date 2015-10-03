package fr.cyann.jinyparser.grammartree;

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
     * @param children list of children.
     * @return the new grammar element.
     */
    public static Sequence sequence(GrammarElement... children) {
        return new Sequence(children);
    }

    /**
     * Create a new choice grammar element.
     * @param children list of children.
     * @return the new grammar element.
     */
    public static Choice choice(GrammarElement... children) {
        return new Choice(children);
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
     * @param decorated the grammar element to decorate.
     * @return the new grammar element.
     */
    public static LexemProducer tokenProducer(LexemType lexemType, GrammarElement decorated) {
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

    /**
     * Create a new position incrementer grammar element.
     * @param decorated the grammar element to decorate.
     * @return the new grammar element.
     */
    public static PosIncrementer posIncrementer(GrammarElement decorated) {
        return new PosIncrementer(decorated);
    }

    // endregion

    // region Lexer

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
