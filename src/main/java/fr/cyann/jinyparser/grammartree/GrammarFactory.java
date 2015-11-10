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

    // region lexer

    /**
     * Create a new lexer char recognizer grammar element.
     *
     * @param terms the list of character to recognize.
     * @return the new grammar element.
     */
    public static CharIn charIn(String terms) {
        return new CharIn(terms);
    }

	/**
	 * Create a new lexer char recognizer grammar element.
	 *
	 * @param start the first character of the range.
	 * @param end   the last character (included) of the range.
	 * @return the new grammar element.
	 */
	public static CharIn charIn(char start, char end) {
		return new CharIn(start, end);
	}

	/**
	 * Create a new lexer exclude char recognizer grammar element.
	 *
	 * @param terms the list of character to recognize.
	 * @return the new grammar element.
	 */
	public static CharNotIn charNotIn(String terms) {
		return new CharNotIn(terms);
	}

	/**
	 * Create a new lexer exclude char recognizer grammar element.
	 *
	 * @param start the first character of the range.
	 * @param end   the last character (included) of the range.
	 * @return the new grammar element.
	 */
	public static CharNotIn charNotIn(char start, char end) {
		return new CharNotIn(start, end);
	}

	/**
	 * Create a new lexer word recognizer grammar element.
     *
     * @param terms the word to recognize.
     * @return the new grammar element.
     */
    public static Word word(String terms) {
        return new Word(terms);
    }

	// endregion

	// region decorator

	/**
	 * Create a wrapper where grammar element can be repeated zero or one time (equivalent to optional(g)).
	 *
	 * @param decorated the grammar element to decorate.
     * @return the new grammar element.
	 */
	public static Optional zeroOrOne(GrammarElement decorated) {
		return new Optional(decorated);
	}

	/**
	 * Create a wrapper where grammar element can be repeated any time (equivalent to zeroOrOne(oneOrMore(g))).
	 *
	 * @param decorated the grammar element to decorate.
	 * @return the new grammar element.
	 */
	public static Optional zeroOrMore(GrammarElement decorated) {
		return new Optional(new Repeat(decorated));
	}

	/**
	 * Create a wrapper where grammar element can be repeated any time (equivalent to repeat(g)).
	 *
	 * @param decorated the grammar element to decorate.
	 * @return the new grammar element.
	 */
	public static Repeat oneOrMore(GrammarElement decorated) {
		return new Repeat(decorated);
	}

	/**
	 * Create a new token producer grammar element.
	 *
     * @param decorated the grammar that decide if lexem will be produced.
     * @param lexemType the token type of the token to produce.
     * @return the new grammar element.
     */
    public static LexemCreatorCore lexemCore(GrammarElement decorated, LexemType lexemType) {
        return new LexemCreatorCore(decorated, lexemType);
    }

    /**
     * Grammar element that produce lexem (process token on lexer) of DEFAULT lexemType.<br>
     * Create also a new token producer grammar element that manage separators.
     *
     * @param decorated the grammar element to decorate.
     * @return the new grammar element.
     */
    public static LexemCreator lexem(GrammarElement decorated) {
        return new LexemCreator(decorated, LexemType.DEFAULT);
    }

    /**
     * Grammar element that produce lexem (process token on lexer).<br>
     * Create also a new token producer grammar element that manage separators.
     *
     * @param decorated the grammar element to decorate.
     * @param lexemType the token type of the token to produce.
     * @return the new grammar element.
     */
    public static LexemCreator lexem(GrammarElement decorated, LexemType lexemType) {
        return new LexemCreator(decorated, lexemType);
    }


    /**
     * Grammar element that produce create (process parse tree element in the stack).
     *
     *
     * @param name
     * @param decorated the grammar that decide if create will be produced.
     * @param clazz     the create element class to create.
     * @return the new grammar element.
     */
    public static <P extends ParsemElement> ParsemCreator<P> create(String name, GrammarElement decorated, Class<P> clazz) {
        return new ParsemCreator<P>(name, decorated, clazz);
    }

    /**
     * Grammar element that produce a default terminal create.
     *
     *
     * @param name
     * @param decorated the grammar that decide if create will be produced.
     * @return the new grammar element.
     */
    public static ParsemCreator<DefaultTerminal> createTerminal(String name, LexemCreator decorated) {
        return new ParsemCreator<DefaultTerminal>(name, decorated, DefaultTerminal.class);
    }

    /**
     * Grammar element that produce a default terminal create.
     *
     *
     * @param name
     * @param decorated the grammar that decide if create will be produced.
     * @return the new grammar element.
     */
    public static ParsemCreator<DefaultNonTerminal> createNonTerminal(String name, GrammarElement decorated) {
        return new ParsemCreator<DefaultNonTerminal>(name, decorated, DefaultNonTerminal.class);
    }

    /**
     * Create a lexem that manage spaces and then a terminal parsem.
     *
     *
     * @param name
     * @param decorated the grammar that decide if create will be produced.
     * @param lexemType the type of the lexem to create.
     * @return the created grammar element.
     */
    public static ParsemCreator<DefaultTerminal> produceTerminal(String name, GrammarElement decorated, LexemType lexemType) {
        return GrammarFactory.createTerminal(name, lexem(decorated, lexemType));
    }

    /**
     * Create a lexem that manage spaces and then a non terminal parsem.
     *
     *
     * @param name
     * @param decorated the grammar that decide if create will be produced.
     * @param lexemType the type of the lexem to create.
     * @return the created grammar element.
     */
    public static ParsemCreator<DefaultNonTerminal> produceNonTerminal(String name, GrammarElement decorated, LexemType lexemType) {
        return GrammarFactory.createNonTerminal(name, lexem(decorated, lexemType));
    }

    /**
     * Create a lexem that manage spaces and then a parsem.
     *
     * @param name
     * @param decorated   the grammar that decide if create will be produced.
     * @param lexemType   the type of the lexem to create.
     * @param parsemClass the class of the parsem to create.
     * @return the created grammar element.
     */
    public static <P extends ParsemElement> ParsemCreator produce(String name, GrammarElement decorated, LexemType lexemType, Class<P> parsemClass) {
        return GrammarFactory.create(name, lexem(decorated, lexemType), parsemClass);
    }

    /**
     * Grammar element that drop the current parsem to the previous one according a code to aggregate them together.
     *
     * @param decorated the grammar that decide if create will be produced.
     * @param fieldName the name of the field that will accept the child parsem.
     * @return the new grammar element.
     */
    public static ParsemDropper dropper(GrammarElement decorated, String fieldName) {
        return new ParsemDropper(decorated, fieldName);
    }

	/**
	 * Grammar element that drop the current parsem to the previous DefaultNonTerminal parsem to aggregate them together.
	 *
	 * @param decorated the grammar that decide if create will be produced.
	 * @return the new grammar element.
	 */
	public static ParsemDropper dropperDefault(GrammarElement decorated) {
		return new ParsemDropper(decorated, DefaultNonTerminal.SUB_NODE_IDENTITY);
	}

	/**
	 * Grammar element that catch the previous parsem to aggregate with the current one.
     *
     * @param decorated the grammar that decide if create will be produced.
     * @return the new grammar element.
     */
    public static GrammarElement catcher(GrammarElement decorated, String... fieldNames) {
        GrammarElement grammar = decorated;
	    for (String fieldName : fieldNames) {
		    grammar = new ParsemCatcher(grammar, fieldName);
	    }

        return grammar;
    }

    /**
     * Grammar element that catch the nth previous parsem(s) to aggregate with the current DefaultNonTerminal grammar element.
     *
     * @param decorated     the grammar that decide if create will be produced.
     * @param howManyParsem how many previous parsem to aggregate with.
     * @return the new grammar element.
     */
    public static GrammarElement catcherDefault(GrammarElement decorated, int howManyParsem) {
        String[] names = new String[howManyParsem];
        for (int i = 0; i < howManyParsem; i++) {
            names[i] = DefaultNonTerminal.SUB_NODE_IDENTITY;
        }
        return catcher(decorated, names);
    }

    /**
     * Create a new line incrementer grammar element.
     *
     * @param decorated the grammar element to decorate.
     * @return the new grammar element.
     */
    public static LineIncrementer lineIncrementer(GrammarElement decorated) {
        return new LineIncrementer(decorated);
    }

    // endregion

    // region node

    /**
     * Create a new sequence grammar element.
     *
     *
     * @param elements list of children.
     * @return the new grammar element.
     */
    public static Sequence sequence(GrammarElement... elements) {
	    return new Sequence(elements);
    }

	/**
	 * Create a wrapper where grammar element must be repeated a number of time (equivalent to sequence(g, g, g, ...)).
	 *
     *
     * @param element the grammar element to decorate.
     * @param count   how many time, the element must be repeated.
     * @return the new grammar element.
     */
	public static Sequence asMany(GrammarElement element, int count) {
		GrammarElement[] elements = new GrammarElement[count];
		for (int i = 0; i < count; i++)
			elements[i] = element;
		return new Sequence(elements);
	}

	/**
	 * Create a new choice grammar element.
     *
     *
     *
     * @param elements list of children.
     * @return the new grammar element.
     */
    public static Choice choice(GrammarElement... elements) {
	    Choice choice = new Choice(elements);
	    return choice;
    }

	public static Recursive recursive(String name) {
		return new Recursive(name);
	}

    // endregion

}
