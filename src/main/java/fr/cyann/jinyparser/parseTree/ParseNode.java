package fr.cyann.jinyparser.parseTree;

import java.util.Iterator;
import java.util.List;

/**
 * Copyright (C) 07/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/
public abstract class ParseNode extends ParseElement implements Iterable<ParseElement> {

	private List<ParseElement> children;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<ParseElement> iterator() {
		return children.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	public ParseNode add(ParseElement parseElement) {
		children.add(parseElement);
		return this;
	}
}
