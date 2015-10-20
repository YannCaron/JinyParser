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
	 * Create a new repeat grammar element.
	 *
	 * @param decorated the grammar element to decorate.
	 * @return the new grammar element.
	 */
	public static Repeat repeat(GrammarElement decorated) {
		return new Repeat(decorated);
	}

	/**
	 * Create a new optional grammar element.
	 *
	 * @param decorated the grammar element to decorate.
	 * @return the new grammar element.
	 */
	public static Optional optional(GrammarElement decorated) {
		return new Optional(decorated);
	}

	// endregion

	// region production

	/**
	 * Create a new token producer grammar element.
	 *
	 * @param decorated the grammar that decide if lexem will be produced.
	 * @param lexemType the token type of the token to produce.
	 * @return the new grammar element.
	 */
	public static LexemCreatorCore lexemRaw(GrammarElement decorated, LexemType lexemType) {
		return new LexemCreatorCore(decorated, lexemType);
	}

	/**
	 * Grammar element that produce lexem (build token on lexer).<br>
	 * Create also a new token producer grammar element that manage separators.
	 *
	 * @param decorated the grammar element to decorate.
	 * @param lexemType the token type of the token to produce.
	 * @return the new grammar element.
	 */
	public static LexemCreatorCore lexem(GrammarElement decorated, LexemType lexemType) {
		return new LexemCreator(decorated, lexemType);
	}

	/**
	 * Grammar element that produce a default terminal create.
	 *
	 * @param decorated the grammar that decide if create will be produced.
	 * @return the new grammar element.
	 */
	public static ParsemCreator create(LexemCreatorCore decorated) {
		return new ParsemCreator(decorated, DefaultTerminal.class);
	}

	/**
	 * Grammar element that produce create (build parse tree element in the stack).
	 *
	 * @param decorated the grammar that decide if create will be produced.
	 * @param clazz     the create element class to create.
	 * @return the new grammar element.
	 */
	public static ParsemCreator create(LexemCreatorCore decorated, Class<? extends ParsemElement> clazz) {
		return new ParsemCreator(decorated, clazz);
	}

	/**
	 * Grammar element that drop the current parsem to the previous code according a code to aggregate them together.
	 *
	 * @param decorated the grammar that decide if create will be produced.
	 * @param fieldName the name of the field that will accept the child parsem.
	 * @return the new grammar element.
	 */
	public static ParsemDropper dropper(GrammarElement decorated, String fieldName) {
		return new ParsemDropper(decorated, fieldName);
	}

	/**
	 * Grammar element that catch the previous parsem to aggregate with the current one.
	 *
	 * @param decorated the grammar that decide if create will be produced.
	 * @param fieldName the name of the field that will accept the child parsem.
	 * @return the new grammar element.
	 */
	public static ParsemCatcher catcher(GrammarElement decorated, String fieldName) {
		return new ParsemCatcher(decorated, fieldName);
	}

	/**
	 * Create a lexem that manage spaces and then a parsem.
	 *
	 * @param decorated the grammar that decide if create will be produced.
	 * @param lexemType the type of the lexem to create.
	 * @return the created grammar element.
	 */
	public static ParsemCreator produce(GrammarElement decorated, LexemType lexemType) {
		return create(lexem(decorated, lexemType), DefaultTerminal.class);
	}

	/**
	 * Create a lexem that manage spaces and then a parsem.
	 *
	 * @param decorated   the grammar that decide if create will be produced.
	 * @param lexemType   the type of the lexem to create.
	 * @param parsemClass the class of the parsem to create.
	 * @return the created grammar element.
	 */
	public static ParsemCreator produce(GrammarElement decorated, LexemType lexemType, Class<? extends ParsemElement> parsemClass) {
		return create(lexem(decorated, lexemType), parsemClass);
	}

	/**
	 * Create a lexem that manage spaces, then a parsem and then drop it to the last created one.
	 *
	 * @param decorated the grammar that decide if create will be produced.
	 * @param lexemType the type of the lexem to create.
	 * @param fieldName the name of the field that will accept the child parsem.
	 * @return the created grammar element.
	 */
	public static ParsemDropper produceAndDrop(GrammarElement decorated, LexemType lexemType, String fieldName) {
		return dropper(produce(decorated, lexemType), fieldName);
	}

	/**
	 * Create a lexem that manage spaces, then a parsem and then drop it to the last created one (a defaultNonTerminal node).
	 *
	 * @param decorated the grammar that decide if create will be produced.
	 * @param lexemType the type of the lexem to create.
	 * @return the created grammar element.
	 */
	public static ParsemDropper produceAndDrop(GrammarElement decorated, LexemType lexemType) {
		return dropper(produce(decorated, lexemType), DefaultNonTerminal.SUB_NODE_IDENTITY);
	}

	/**
	 * Create a lexem that manage spaces, then a parsem and then drop it to the last created one.
	 *
	 * @param decorated   the grammar that decide if create will be produced.
	 * @param lexemType   the type of the lexem to create.
	 * @param parsemClass the class of the parsem to create.
	 * @param fieldName   the name of the field that will accept the child parsem.
	 * @return the created grammar element.
	 */
	public static ParsemDropper produceAndDrop(GrammarElement decorated, LexemType lexemType, Class<? extends ParsemElement> parsemClass, String fieldName) {
		return dropper(produce(decorated, lexemType, parsemClass), fieldName);
	}

	/**
	 * Create a defaultNonTerminal node and catch the previously created parsem to it.
	 *
	 * @param decorated     the grammar that decide if create will be produced.
	 * @param howManyParsem how many previously created parsem.
	 * @return the created grammar element.
	 */
	public static GrammarElement createAndCatch(LexemCreatorCore decorated, int howManyParsem) {
		String[] names = new String[howManyParsem];
		for (int i = 0; i < howManyParsem; i++) {
			names[i] = DefaultNonTerminal.SUB_NODE_IDENTITY;
		}
		return createAndCatch(decorated, DefaultNonTerminal.class, names);
	}

	// TODO : javadoc
	public static GrammarElement createAndCatch(LexemCreatorCore decorated, Class<? extends ParsemElement> parsemClass, String... fieldNames) {
		GrammarElement grammar = create(decorated, parsemClass);
		for (int i = fieldNames.length - 1; i >= 0; i--) {
			String fieldName = fieldNames[i];
			grammar = catcher(grammar, fieldName);
		}

		return grammar;
	}

	// TODO : javadoc
	public static GrammarElement produceAndCatch(GrammarElement decorated, LexemType lexemType, int howManyParsem) {
		String[] names = new String[howManyParsem];
		for (int i = 0; i < howManyParsem; i++) {
			names[i] = DefaultNonTerminal.SUB_NODE_IDENTITY;
		}
		return produceAndCatch(decorated, lexemType, DefaultNonTerminal.class, names);
	}

	// TODO : javadoc
	public static GrammarElement produceAndCatch(GrammarElement decorated, LexemType lexemType, Class<? extends ParsemElement> parsemClass, String... fieldNames) {
		GrammarElement grammar = produce(decorated, lexemType, parsemClass);
		for (int i = fieldNames.length - 1; i >= 0; i--) {
			String fieldName = fieldNames[i];
			grammar = catcher(grammar, fieldName);
		}

		return grammar;
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

}
