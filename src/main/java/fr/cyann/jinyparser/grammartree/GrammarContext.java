package fr.cyann.jinyparser.grammartree;/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.lexem.Lexem;
import fr.cyann.jinyparser.lexem.SourcePosition;
import fr.cyann.jinyparser.parsetree.ParsemElement;
import fr.cyann.jinyparser.utils.StringLookaheadIterator;

import java.util.*;

/**
 * The GrammarContext class. A context pass through parser tree that contains all intermediates states of lexing / parsing.<br>
 * All the parsing resources.
 */
public class GrammarContext {

	private final StringLookaheadIterator iterator;
	private final SourcePosition positionManager;
	private final StringBuilder term;
	private final List<Lexem> lexer;
	private final Stack<ParsemElement> parser;
	private final Map<Integer, GrammarElement> packrat;

	/**
	 * Default constructor.
	 * @param source the source code to parse.
	 */
	public GrammarContext(String source) {
		iterator = new StringLookaheadIterator(source);
		positionManager = new SourcePosition(1, 1);
		term = new StringBuilder();
		lexer = new ArrayList<Lexem>();
		parser = new Stack<ParsemElement>();
		packrat = new HashMap<Integer, GrammarElement>();
	}

	//region Char Iterator

	/**
	 * Mark current character to give ability to backtrack to this position.
	 */
	public void markChar() {
		iterator.mark();
	}

	/**
	 * Restore (pop) the stored position to the actual one.
	 */
	public void rollbackChar() {
		iterator.rollback();
	}

	/**
	 * Garbage (pop) the stored position.
	 */
	public void resumeChar() {
		iterator.resume();
	}

	/**
	 * Return the current character of source.
	 * @return the current character.
	 */
	public Character currentChar() {
		return iterator.current();
	}

	/**
	 * Get the iterator current position.
	 */
	public int currentPosition() {
		return iterator.getCurrentPosition();
	}

	/**
	 * Jump to next character in the source code.
	 */
    public void nextCharLookahead() {
	    iterator.next();
	}

	/**
	 * Jump to next character in the source code and process current word.
	 */
    public void nextCharParser() {
	    term.append(iterator.current());
	    iterator.next();
	    positionManager.increment();
	}

	/**
	 * Tell if iteration is terminated.
	 *
	 * @return true if iteration is terminated.
	 */
	public boolean isTerminated() {
		return iterator.isTerminated();
	}

	//endregion

	// region Lexer

	/**
	 * Empty the term buffer.
	 */
	public void resetTerm() {
		term.delete(0, term.length());
	}

	/**
	 * Get the term from the buffer.
	 * @return the constructed term.
	 */
	public String getTerm() {
		return term.toString();
	}

	/**
	 * Jump to next line.
	 */
	public void newLine() {
		positionManager.newLine();
	}

	/**
	 * Get the current position.
	 * @return the current position.
	 */
	public int getPositionManager() {
		return positionManager.getPos();
	}

	/**
	 * Get the current line number.
	 * @return the current line number.
	 */
	public int getLine() {
		return positionManager.getLine();
	}

	/**
	 * Get the current column number.
	 * @return the current column number.
	 */
	public int getColumn() {
		return positionManager.getColumn();
	}

	/** {@inheritDoc} */
	public Lexem getCurrentLexem() {
		return lexer.get(lexer.size() - 1);
	}

	/**
	 * Set the current lexem.
	 * @param lexem the current lexem to store.
	 */
	public void appendLexem(Lexem lexem) {
		this.lexer.add(lexem);
	}

	/**
	 * Give the list of lexem (lexer) that resulting from lexing.
	 * @return the list of lexem
	 */
    public List<Lexem> getLexer() {
        return lexer;
	}

	// endregion

	// region parser
	/**
	 * Push parsemElement element on the top of parser stack.
	 * @param parsemElement the abstract syntax tree element.
	 */
	public void pushParsem(ParsemElement parsemElement) {
		parser.push(parsemElement);
	}

	/** {@inheritDoc} */
	public ParsemElement popParsem() {
		return parser.pop();
	}

	/**
	 * Gives the current parsem.
	 *
	 * @return the current parsem.
	 */
	public ParsemElement peekParsem() {
		return parser.peek();
	}

	/**
	 * Get if parser is empty.
	 *
	 * @return true if empty.
	 */
	public boolean isParserEmpty() {
		return parser.empty();
    }

	/**
	 * Get the root element of the parse tree.
	 * @return the first element in the stack.
	 */
	public ParsemElement getParseTree() {
		if (parser.empty()) return null;
		return parser.firstElement();
	}
	//endregion

	// region packrat
	public void storeToPackrat(int pos, GrammarElement grammar) {
		packrat.put(pos, grammar);
	}

	public GrammarElement retrieveFromPackrat(int pos) {
		return packrat.get(pos);
	}

	// endregion

	public String getPositionToString() {
		return positionManager.toString();
	}

	/**
	 * Give the string representation of the object. Useful for debugging.
	 * @return the string representation of the object.
	 */
	@Override
	public String toString() {
		return "ParseContext:\n" + iterator + "\nAST: " + getParseTree();
	}
}
