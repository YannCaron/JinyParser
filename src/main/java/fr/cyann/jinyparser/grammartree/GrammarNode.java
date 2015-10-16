package fr.cyann.jinyparser.grammartree;

import java.util.*;

/**
 * Copyright (C) 07/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The GrammarNode class. An abstract class for all grammar nodes (compounds one),
 */
@SuppressWarnings("WeakerAccess")
public abstract class GrammarNode extends GrammarElement implements Iterable<GrammarElement> {

	private final List<GrammarElement> children;

	/**
	 * Default and mandatory constructor. Initialize internal resources.
	 */
	public GrammarNode() {
		super();
		this.children = new ArrayList<GrammarElement>();
	}

	GrammarNode(GrammarElement[] children) {
		this.children = Arrays.asList(children);
	}

	/** {@inheritDoc} */
	@Override
	public Iterator<GrammarElement> iterator() {
		return children.iterator();
	}

	/**
	 * Set all sub grammar elements to the node.
	 *
	 * @param elements the elements.
	 */
	public GrammarNode addAll(GrammarElement... elements) {
		Collections.addAll(this.children, elements);

		return this;
	}

}
