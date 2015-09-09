package fr.cyann.jinyparser.visitor;/**
 * Copyright (C) 09/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.utils.StringLookaheadIterator;

/**
 * The DefaultParseContext class definition.
 */
public class DefaultParseContext extends Context {

	private final StringLookaheadIterator it;

	/**
	 * Default constructor.
	 *
	 * @param source The source to parse.
	 */
	public DefaultParseContext(String source) {
		super(source);
		it = new StringLookaheadIterator(source);
	}
}
