package fr.cyann.jinyparser.grammartree;/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.parsetree.Ast;
import fr.cyann.jinyparser.token.Lexem;
import fr.cyann.jinyparser.token.SourcePosition;
import fr.cyann.jinyparser.parsetree.ParsemBuildable;
import fr.cyann.jinyparser.utils.StringLookaheadIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * The GrammarContext class. A context pass through parser tree that contains all intermediates states of lexing / parsing.<br>
 * All the parsing resources.
 */
public class GrammarContext implements ParsemBuildable {

	private final StringLookaheadIterator it;
	private final SourcePosition pos;
	private final StringBuilder term;
	private final List<Lexem> lexer;
	private final Stack<Ast> parser;

	/**
	 * Default constructor.
	 * @param source the source code to parse.
	 */
	public GrammarContext(String source) {
		it = new StringLookaheadIterator(source);
		pos = new SourcePosition(1, 1);
		term = new StringBuilder();
		lexer = new ArrayList<Lexem>();
		parser = new Stack<Ast>();
	}

	//region Char Iterator

	/**
	 * Mark current character to give ability to backtrack to this position.
	 */
	public void markChar() {
		it.mark();
	}

	/**
	 * Backtrack to the last marked position.
	 */
	public void rollbackChar() {
		it.rollback();
	}

	/**
	 * Return the current character of source.
	 * @return the current character.
	 */
	public Character currentChar() {
		return it.current();
	}

	/**
	 * Jump to next character in the source code.
	 */
	public void nextChar() {
		term.append(it.current());
		it.next();
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
		pos.newLine();
	}

	/**
	 * Get the current position.
	 * @return the current position.
	 */
	public int getPos() {
		return pos.getPos();
	}

	/**
	 * Get the current line number.
	 * @return the current line number.
	 */
	public int getLine() {
		return pos.getLine();
	}

	/**
	 * Get the current column number.
	 * @return the current column number.
	 */
	public int getColumn() {
		return pos.getColumn();
	}

	/** {@inheritDoc} */
	@Override
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
	public Iterable<Lexem> getLexer() {
		return lexer;
	}

	// endregion

	// region parser

	/**
	 * Push ast element on the top of parser stack.
	 * @param ast the abstract syntax tree element.
	 */
	public void pushParsem(Ast ast) {
		parser.push(ast);
	}

	/** {@inheritDoc} */
	@Override
	public Ast popParsem() {
		return parser.pop();
	}

	/**
	 * Get the root element of the parse tree.
	 * @return the first element in the stack.
	 */
	public Ast getParseTree() {
		return parser.firstElement();
	}
	//endregion

	// endregion

	/**
	 * Give the string representation of the object. Useful for debugging.
	 * @return the string representation of the object.
	 */
	@Override
	public String toString() {
		return "ParseContext:\n" + it.toString() + /*"\nAST: " + getParseTree().toString()*/ +'\n';
	}
}
