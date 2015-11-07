package fr.cyann.jinyparser.grammartree;

import java.util.Arrays;
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

/**
 * The GrammarNode class. An abstract class for all grammar nodes (compounds one),
 */
@SuppressWarnings("WeakerAccess")
public abstract class GrammarNode extends GrammarElement implements Iterable<GrammarElement> {

	final List<GrammarElement> children;

	/**
	 * {@inheritDoc}
	 */
	public GrammarNode(GrammarElement[] children) {
		this.children = Arrays.asList(children);

		for (GrammarElement child : children) {
			child.setParent(this);
		}
	}

    /**
     * {@inheritDoc}
     */
    @Override
	public Iterator<GrammarElement> iterator() {
		return children.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean replace(GrammarElement element, GrammarElement by) {
		boolean result = false;
		for (int i = 0; i < children.size(); i++) {
			if (children.get(i).equals(element)) {
				children.set(i, by);
				result = true;
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void visit(AbstractVisitor visitor) {
		visitor.visitNode(this);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " {" +
				"childrenCount=" + children.size() +
				"}";
	}
}
