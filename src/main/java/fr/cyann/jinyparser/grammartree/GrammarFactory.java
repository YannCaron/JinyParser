package fr.cyann.jinyparser.grammartree;

import fr.cyann.jinyparser.lexem.LexemType;
import fr.cyann.jinyparser.parsetree.DefaultNonTerminal;
import fr.cyann.jinyparser.parsetree.DefaultTerminal;
import fr.cyann.jinyparser.parsetree.ParsemElement;

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
     * @param elements list of children.
     * @return the new grammar element.
     */
    public static Sequence sequence(GrammarElement... elements) {
        Sequence sequence = new Sequence();
        sequence.addAll(elements);
        return sequence;
    }

    /**
     * Create a new choice grammar element.
     *
     * @param elements list of children.
     * @return the new grammar element.
     */
    public static Choice choice(GrammarElement... elements) {
        Choice choice = new Choice();
        choice.addAll(elements);
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
     * Create also a new token producer grammar element that manage separators.
     * @param lexemType the token type of the token to produce.
     * @param decorated the grammar element to decorate.
     * @return the new grammar element.
     */
    public static GrammarElement lexem(LexemType lexemType, GrammarElement decorated) {
        return new SeparatorsManager(new LexemProducer(lexemType, decorated));
    }

    /**
     * Grammar element that produce a default terminal create.
     * @param decorated the grammar that decide if create will be produced.
     * @return the new grammar element.
     */
    public static GrammarElement create(GrammarElement decorated) {
        return new ParsemCreator(DefaultTerminal.class, decorated);
    }

    /**
     * Grammar element that produce create (build parse tree element in the stack).
     * @param clazz the create element class to create.
     * @param decorated the grammar that decide if create will be produced.
     * @return the new grammar element.
     */
    public static GrammarElement create(Class<? extends ParsemElement> clazz, GrammarElement decorated) {
        return new ParsemCreator(clazz, decorated);
    }

    /**
     * Grammar element that drop the current parsem to the previous code according a code to aggregate them together.
     * @param fieldName the name of the field that will accept the child parsem.
     * @param decorated the grammar that decide if create will be produced.
     * @return the new grammar element.
     */
    public static GrammarElement dropper(String fieldName, GrammarElement decorated) {
        return new ParsemDropper(fieldName, decorated);
    }

    /**
     * Grammar element that catch the previous parsem to aggregate with the current one.
     *
     * @param fieldName the name of the field that will accept the child parsem.
     * @param decorated the grammar that decide if create will be produced.
     * @return the new grammar element.
     */
    public static GrammarElement catcher(String fieldName, GrammarElement decorated) {
        return new ParsemCatcher(fieldName, decorated);
    }

    /**
     * Create a lexem that manage spaces and then a parsem.
     *
     * @param lexemType the type of the lexem to create.
     * @param decorated the grammar that decide if create will be produced.
     * @return the created grammar element.
     */
    public static GrammarElement produce(LexemType lexemType, GrammarElement decorated) {
        return create(DefaultTerminal.class, lexem(lexemType, decorated));
    }

    /**
     * Create a lexem that manage spaces and then a parsem.
     *
     * @param lexemType   the type of the lexem to create.
     * @param parsemClass the class of the parsem to create.
     * @param decorated   the grammar that decide if create will be produced.
     * @return the created grammar element.
     */
    public static GrammarElement produce(LexemType lexemType, Class<? extends ParsemElement> parsemClass, GrammarElement decorated) {
        return create(parsemClass, lexem(lexemType, decorated));
    }

    /**
     * Create a lexem that manage spaces, then a parsem and then drop it to the last created one.
     *
     * @param lexemType the type of the lexem to create.
     * @param fieldName the name of the field that will accept the child parsem.
     * @param decorated the grammar that decide if create will be produced.
     * @return the created grammar element.
     */
    public static GrammarElement produceAndDrop(LexemType lexemType, String fieldName, GrammarElement decorated) {
        return dropper(fieldName, produce(lexemType, decorated));
    }

    /**
     * Create a lexem that manage spaces, then a parsem and then drop it to the last created one.
     *
     * @param lexemType   the type of the lexem to create.
     * @param parsemClass the class of the parsem to create.
     * @param fieldName   the name of the field that will accept the child parsem.
     * @param decorated   the grammar that decide if create will be produced.
     * @return the created grammar element.
     */
    public static GrammarElement produceAndDrop(LexemType lexemType, Class<? extends ParsemElement> parsemClass, String fieldName, GrammarElement decorated) {
        return dropper(fieldName, produce(lexemType, parsemClass, decorated));
    }

    public static GrammarElement createAndCatch(GrammarElement decorated, Class<? extends ParsemElement> parsemClass, String... fieldNames) {
        GrammarElement grammar = create(parsemClass, decorated);
        for (int i = fieldNames.length - 1; i >= 0; i--) {
            String fieldName = fieldNames[i];
            grammar = catcher(fieldName, grammar);
        }

        return grammar;
    }


    /**
     * Grammar element that produce a default non terminal create.
     * @param decorated the grammar that decide if create will be produced.
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


    /**
     * Create a new lexer word recognizer grammar element.
     * @param terms the word to recognize.
     * @return the new grammar element.
     */
    public static Word word(String terms) {
        return new Word(terms);
    }

    // endregion

}
