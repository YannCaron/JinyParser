package fr.cyann.jinyparser.grammartree;
/**
 * Copyright (C) 14/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.exceptions.JinyException;
import fr.cyann.jinyparser.parsetree.NonTerminal;
import fr.cyann.jinyparser.parsetree.ParsemElement;
import fr.cyann.jinyparser.utils.MultilingualMessage;

/**
 * The NonTerminalAggregator class definition.<br>
 * Aggregate self parsem with previous created one. Result parsem is the previous one.
 */
public class NonTerminalAggregator extends GrammarDecorator {

	protected final String fieldName;
	private boolean create;

	/**
	 * Default constructor.
	 *
	 * @param fieldName the name of the field to drop.
	 * @param decorated the decorated element.
	 */
	public NonTerminalAggregator(String fieldName, boolean create, GrammarElement decorated) {
		super(decorated);
		this.fieldName = fieldName;
		this.create = create;
	}

	/**
	 * Create getter. Indicate if aggregator will incorporate the non terminal to stack.
	 *
	 * @return the getter value.
	 */
	public boolean isCreate() {
		return create;
	}

	/**
	 * Create setter. Set if aggregator will incorporate the non terminal to stack or not.
	 *
	 * @param create the create value.
	 */
	public void setCreate(boolean create) {
		this.create = create;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean lookahead(GrammarContext context) {
		return decorated.lookahead(context);
	}

	protected void checkLastElementOnParser(GrammarContext context) {
		if (context.isParserEmpty())
			throw new JinyException(MultilingualMessage.create("NonTerminalAggregator [%s] try to pop parsem from the stack, but the stack is empty. No parsem was previously created.").setArgs(fieldName));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean parse(GrammarContext context) {
		context.resetTerm();

		boolean res = decorated.parse(context);

		if (res) {

			NonTerminal lastPending = context.getLastPending();

			if (lastPending instanceof GrammarContext.NonTerminalDummy) {
				// normal aggregation

				checkLastElementOnParser(context);
				ParsemElement elementToAggregate = context.popParsem();

				checkLastElementOnParser(context);
				ParsemElement nonTerminal = context.popParsem();

				nonTerminal.aggregate(fieldName, elementToAggregate);
				context.pushParsem(nonTerminal);

			} else {
				// pending aggregation

				checkLastElementOnParser(context);
				ParsemElement elementToAggregate = context.peekParsem();

				context.getLastPending().aggregate(fieldName, elementToAggregate);

				if (create) {
					context.incorporateLastPending();
				}

			}

		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param context
	 */
	@Override
	void buildBnf(BnfContext context) {
		super.buildBnf(context);
		if (create)
			context.append("_CX");
	}
}
