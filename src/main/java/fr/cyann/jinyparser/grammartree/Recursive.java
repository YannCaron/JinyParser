package fr.cyann.jinyparser.grammartree;
/**
 * Copyright (C) 20/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.exceptions.JinyException;
import fr.cyann.jinyparser.utils.MultilingualMessage;

/**
 * The Recursive grammar element definition.<br>
 * Give the ability to loop grammars togathers (and manage cycles).
 */
public class Recursive extends GrammarElement {

	private final String name;
	private GrammarElement grammar;

	/**
	 * Default and mandatory constructor.
	 *
	 * @param name the bnf name.
	 */
	public Recursive(String name) {
		this.name = name;
	}

	/**
	 * Set the grammar to delegate to.<br>
	 * This call is mandatory before parsing. Elsewhere it throw an exception.
	 *
	 * @param grammar the grammar to delegate to.
	 */
	public void setGrammar(GrammarElement grammar) {
		this.grammar = grammar;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean lookahead(GrammarContext context) {
		if (grammar != null) {
			return grammar.lookahead(context);
		}
		throw new JinyException(MultilingualMessage.create("Recursive grammar must have a grammar to delegate it the lookahead parsing! Please use the Recursive.setGrammar() method before parsing."));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean parse(GrammarContext context) {
		if (grammar != null) {
			return grammar.parse(context);
		}
		throw new JinyException(MultilingualMessage.create("Recursive grammar must have a grammar to delegate it the parsing! Please use the Recursive.setGrammar() method before parsing."));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void visit(Visitor visitor) {
		visitor.visitRecursiveBefore(this);
		grammar.visit(visitor);
		visitor.visitRecursiveAfter(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void buildBnf(BnfContext context) {
		context.newProduction(name, grammar);
	}
}
