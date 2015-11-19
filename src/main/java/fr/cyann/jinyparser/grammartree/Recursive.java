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

import java.util.Queue;
import java.util.Stack;

/**
 * The Recursive grammar element definition.<br>
 * Give the ability to loop grammars togathers (and manage cycles).
 */
public class Recursive extends GrammarElement implements NamedGrammar {

	protected String name;
	protected GrammarElement grammar;
	private boolean hide;

	/**
	 * Default and mandatory constructor.
	 *
	 * @param name the bnf name.
	 */
	public Recursive(String name) {
		this.name = name;
		this.hide = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isHidden() {
		return hide;
	}

	/**
	 * Set the hide property.
	 *
	 * @param hide the value to set.
	 */
	public void setHide(boolean hide) {
		this.hide = hide;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 */
	public GrammarElement getGrammar() {
		return grammar;
	}

	/**
	 * Set the grammar to delegate to.<br>
	 * This call is mandatory before parsing. Elsewhere it throw an exception.
	 *
	 * @param grammar the grammar to delegate to.
	 */
	public Recursive setGrammar(GrammarElement grammar) {
		grammar.setParent(this);
		this.grammar = grammar;
		return this;
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
	public boolean replace(GrammarElement element, GrammarElement by) {
		if (!grammar.equals(element)) return false;
		grammar = by;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void depthFirstPush(Stack<GrammarElement> stack) {
		stack.push(this.grammar);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void breadthFirstAdd(Queue<GrammarElement> queue) {
		queue.add(this.grammar);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void buildBnf(BnfContext context) {
		if (!isHidden()) {
			context.newProduction(name, grammar);
		} else {
			grammar.buildBnf(context);
		}
	}

	@Override
	public String toString() {
		return "Recursive {" +
				"name='" + name + '\'' +
				'}';
	}


}
