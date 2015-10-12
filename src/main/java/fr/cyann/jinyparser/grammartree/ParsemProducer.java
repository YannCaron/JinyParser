package fr.cyann.jinyparser.grammartree;
/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.parsetree.ParsemBuilder;
import fr.cyann.jinyparser.parsetree.ParsemElement;

/**
 * The ParsemProducer class. Each time the decorated grammar element is parsed, it produce a Parse tree element and store it into the context.<br>
 * That represent the parser production function.
 */
public class ParsemProducer extends GrammarDecorator {

	private final ParsemBuilder builder;

	/**
	 * Default constructor.
	 *
	 * @param builder   the builder to build parse tree element.
	 * @param decorated the decorated grammar element.
	 */
	public ParsemProducer(ParsemBuilder builder, GrammarElement decorated) {
		super(decorated);
		this.builder = builder;
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
			ParsemElement parsem = builder.buildParsem(context);

			context.pushParsem(parsem);
		}

		return res;
	}

}
