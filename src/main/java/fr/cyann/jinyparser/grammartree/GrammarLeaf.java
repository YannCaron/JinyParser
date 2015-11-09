package fr.cyann.jinyparser.grammartree;/**
 * Copyright (C) 07/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import java.util.Queue;
import java.util.Stack;

/**
 * The GrammarLeaf definition.
 */
abstract class GrammarLeaf extends GrammarElement {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean replace(GrammarElement element, GrammarElement by) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void depthFirstPush(Stack<GrammarElement> stack) {
		// do nothing
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void breadthFirstAdd(Queue<GrammarElement> queue) {
		// do nothing
	}
}
