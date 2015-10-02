package fr.cyann.jinyparser.grammartree;/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The GrammarDecorator class. An abstract class for all grammar decorators (add a local parsing functionality).
 */
public abstract class GrammarDecorator extends GrammarElement {

	/**
	 * The decorated object.
	 */
	protected final GrammarElement decorated;

	/**
	 * Default and mandatory constructor. Decorated object if final.
	 * @param decorated
	 */
	protected GrammarDecorator(GrammarElement decorated) {
		this.decorated = decorated;
	}
}