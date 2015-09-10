package fr.cyann.jinyparser.visitor;/**
 * Copyright (C) 09/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.utils.StringLookaheadIterator;

import java.util.Iterator;
import java.util.Stack;

/**
 * The DefaultParseContext class definition.
 */
class DefaultParseContext extends Context implements Iterable<String> {

	// TODO : Refactor, rename, hide and delegate
	private final StringLookaheadIterator it;
	private final Stack<String> tokens;

	/**
	 * Default constructor.
	 *
	 * @param source The source to parse.
	 */
	public DefaultParseContext(String source) {
		super(source);
		it = new StringLookaheadIterator(source);
		tokens = new Stack<String>();
	}

	public boolean hasNext() {
		return it.hasNext();
	}

	/**
	 * Returns an iterator over a set of elements of type T.
	 *
	 * @return an Iterator.
	 */
	@Override
	public Iterator<String> iterator() {
		return tokens.listIterator();
	}

	public Character current() {
		return it.current();
	}

	public void dump() {
		it.dump();
	}

	public void store() {
		it.store();
	}

	public void next() {
		it.next();
	}

	public void restore() {
		it.restore();
	}

	public void pushToken(String token) {
		tokens.push(token);
	}

}
