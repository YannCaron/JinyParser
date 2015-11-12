package fr.cyann.jinyparser.grammartree;
/**
 * Copyright (C) 14/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.parsetree.DefaultNonTerminal;
import fr.cyann.jinyparser.parsetree.NonTerminal;
import fr.cyann.jinyparser.parsetree.ParsemElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The TerminalProduction class definition.<br>
 */
public class NonTerminalCreator extends GrammarDecorator {

	private final List<String> fieldNames;

	/**
	 * Default constructor.
	 *
	 * @param fieldName the field name to aggregate the decorated grammar production with.
	 * @param decorated the decorated element.
	 */
	public NonTerminalCreator(String fieldName, GrammarElement decorated) {
		super(decorated);
		fieldNames = new ArrayList<String>();
		fieldNames.add(fieldName);
	}

	/**
	 * When non terminal node is created, aggregate the nth previous parsem with it.
	 *
	 * @param fieldNames the fields to aggregate with.
	 * @return this.
	 */
	public NonTerminalCreator aggregateWith(String... fieldNames) {
		this.fieldNames.addAll(Arrays.asList(fieldNames));
		return this;
	}

	/**
	 * When default non terminal node is created, aggregate the nth previous parsem with it.
	 *
	 * @param number the number of previous parsem to aggregate.
	 * @return this.
	 */
	public NonTerminalCreator aggregateWith(int number) {
		for (int i = 0; i < number; i++) {
			this.fieldNames.add(DefaultNonTerminal.SUB_NODE_IDENTITY);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean lookahead(GrammarContext context) {
		return decorated.lookahead(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean parse(GrammarContext context) {
		context.resetTerm();

		boolean res = decorated.parse(context);

		if (res) {
			NonTerminal nonTerminal = context.getCurrentCreator().createParsem();

			for (String fieldName : fieldNames) {
				ParsemElement elementToAggregate = context.popParsem();
				nonTerminal.aggregate(fieldName, elementToAggregate);
			}

			context.pushParsem(nonTerminal);
		}

		return res;
	}

}
