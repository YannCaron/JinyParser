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
import fr.cyann.jinyparser.parsetree.ParsemElement;
import fr.cyann.jinyparser.utils.MultilingualMessage;

/**
 * The NonTerminalAggregator class definition.<br>
 * Aggregate self parsem with previous created one. Result parsem is the previous one.
 */
public class NonTerminalAggregator extends GrammarDecorator {

	private final String fieldName;

	/**
	 * Default constructor.
	 *
	 * @param fieldName the name of the field to drop.
	 * @param decorated the decorated element.
	 */
	public NonTerminalAggregator(String fieldName, GrammarElement decorated) {
		super(decorated);
		this.fieldName = fieldName;
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

			if (context.isParserEmpty())
				throw new JinyException(MultilingualMessage.create("NonTerminalAggregator [%s] try to pop parsem from the stack, but the stack is empty. No parsem was previously created.").setArgs(fieldName));
			ParsemElement elementToAggregate = context.popParsem();

			if (context.isParserEmpty())
				throw new JinyException(MultilingualMessage.create("NonTerminalAggregator [%s] try to pop parsem from the stack, but the stack is empty. Not enough parsem has been created.").setArgs(fieldName));
			ParsemElement nonTerminal = context.popParsem();

			nonTerminal.aggregate(fieldName, elementToAggregate);

			context.pushParsem(nonTerminal);

		}

		return res;
	}
}
