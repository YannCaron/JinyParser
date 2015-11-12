package fr.cyann.jinyparser.grammartree;

import fr.cyann.jinyparser.lexem.LexemType;
import fr.cyann.jinyparser.parsetree.DefaultNonTerminal;
import fr.cyann.jinyparser.parsetree.DefaultTerminal;
import fr.cyann.jinyparser.parsetree.NonTerminal;
import fr.cyann.jinyparser.parsetree.Terminal;

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
		return new Word(terms, true);
	}

	/**
	 * Create a new lexer not case sensitive word recognizer grammar element.
	 *
	 * @param terms the word to recognize.
	 * @return the new grammar element.
	 */
	public static Word freeWord(String terms) {
		return new Word(terms, false);
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
	 * Create a new line incrementer grammar element.
	 *
	 * @param decorated the grammar element to decorate.
	 * @return the new grammar element.
	 */
	public static LineIncrementer lineIncrementer(GrammarElement decorated) {
		return new LineIncrementer(decorated);
	}

	// endregion

	// region production

	/**
	 * Grammar element that produce lexem (process token on lexer) of DEFAULT lexemType.<br>
	 * Create also a new token producer grammar element that manage separators.
	 *
	 * @param decorated the grammar element to decorate.
	 * @return the new grammar element.
	 */
	public static LexemCreator lexem(GrammarElement decorated) {
		return new LexemCreator(LexemType.DEFAULT, decorated);
	}

	/**
	 * Grammar element that produce lexem (process token on lexer).<br>
	 * Create also a new token producer grammar element that manage separators.
	 *
	 * @param lexemType the token type of the token to produce.
	 * @param decorated the grammar element to decorate.
	 * @return the new grammar element.
	 */
	public static LexemCreator lexem(LexemType lexemType, GrammarElement decorated) {
		return new LexemCreator(lexemType, decorated);
	}

	/**
	 * Create a production that create a terminal parsem when decorated element (a lexem creator) is parser.
	 *
	 * @param name        the name of the production.
	 * @param parsemClass the class of the parsem to create.
	 * @param decorated   the decorated element.
	 * @param <P>         the parsem type.
	 * @return the production.
	 */
	public static <P extends Terminal> TerminalProduction<P> terminal(String name, Class<P> parsemClass, LexemCreator decorated) {
		return new TerminalProduction(name, parsemClass, decorated);
	}

	/**
	 * Create a production that create a defaultTerminal parsem when decorated element (a lexem creator) is parser.
	 *
	 * @param name        the name of the production.
	 * @param decorated   the decorated element.
	 * @return the production.
	 */
	public static TerminalProduction<DefaultTerminal> terminal(String name, LexemCreator decorated) {
		return terminal(name, DefaultTerminal.class, decorated);
	}

	/**
	 * Create a production that create a nonTerminal parsem container.<br>
	 * Must be employed with NonTerminalCreator to raise the real production.
	 *
	 * @param name        the name of the production.
	 * @param parsemClass the class of the parsem to create.
	 * @param decorated   the decorated element.
	 * @param <P>         the parsem type.
	 * @return the production.
	 */
	public static <P extends NonTerminal> NonTerminalProduction<P> nonTerminal(String name, Class<P> parsemClass, GrammarElement decorated) {
		return new NonTerminalProduction<P>(name, parsemClass, decorated);
	}

	public static NonTerminalProduction<DefaultNonTerminal> nonTerminal(String name, GrammarElement decorated) {
		return nonTerminal(name, DefaultNonTerminal.class, decorated);
	}

	public static NonTerminalCreator create(String fieldName, GrammarElement decorated) {
		return new NonTerminalCreator(fieldName, decorated);
	}

	public static NonTerminalCreator create(GrammarElement decorated) {
		return create(DefaultNonTerminal.SUB_NODE_IDENTITY, decorated);
	}

	/**
	 * Grammar element that drop the current parsem to the previous one according a code to dropper them together.
	 *
	 * @param fieldName the name of the field that will accept the child parsem.
	 * @param decorated the grammar that decide if createTerminal will be produced.
	 * @return the new grammar element.
	 */
	public static NonTerminalAggregator aggregate(String fieldName, GrammarElement decorated) {
		return new NonTerminalAggregator(fieldName, decorated);
	}

	/**
	 * Grammar element that drop the current parsem to the previous DefaultNonTerminal parsem to dropper them together.
	 *
	 * @param decorated the grammar that decide if createTerminal will be produced.
	 * @return the new grammar element.
	 */
	public static NonTerminalAggregator aggregate(GrammarElement decorated) {
		return aggregate(DefaultNonTerminal.SUB_NODE_IDENTITY, decorated);
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
		return new Sequence(elements);
	}

	/**
	 * Create a wrapper where grammar element must be repeated a number of time (equivalent to sequence(g, g, g, ...)).
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
