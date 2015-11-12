package fr.cyann.jinyparser.grammartree;/**
 * Copyright (C) 12/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.parsetree.ParsemElement;
import fr.cyann.jinyparser.parsetree.ParsemVisitor;
import fr.cyann.jinyparser.parsetree.VisitorContext;

/**
 * The GrammarProduction definition.
 */
public abstract class GrammarProduction<P extends ParsemElement> extends GrammarDecorator {

	private final String name;
	private final Class<P> clazz;
	private ParsemVisitor<P, ? extends VisitorContext> visitor;

	/**
	 * Default constructor.
	 *
	 * @param name      the production name.
	 * @param clazz     the grammar element class to createTerminal.
	 * @param decorated the decorated grammar element.
	 */
	public GrammarProduction(String name, Class<P> clazz, GrammarElement decorated) {
		super(decorated);
		this.name = name;
		this.clazz = clazz;
	}

	/**
	 * Get the recursive name.
	 *
	 * @return the recursive name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the parsem class to produce.
	 *
	 * @return the parsem class to produce.
	 */
	public Class<P> getParsemClass() {
		return clazz;
	}

	/**
	 * Get the visitor.
	 *
	 * @return the visitor.
	 */
	public ParsemVisitor<P, ? extends VisitorContext> getVisitor() {
		return visitor;
	}

	/**
	 * The parsem visitor accessor.<br>
	 * Define the behaviour of the parsem element when it will be traversed.
	 *
	 * @param visitor the visitor to delegate to the parsem.
	 * @return this.
	 */
	public GrammarProduction setVisitor(ParsemVisitor<P, ? extends VisitorContext> visitor) {
		this.visitor = visitor;
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
	void buildBnf(BnfContext context) {
		context.newProduction(name, decorated);
	}

}
