package fr.cyann.jinyparser.tree;/**
 * Copyright (C) 02/12/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import java.util.HashSet;
import java.util.Set;

/**
 * The InfiniteLoopPruning definition.
 */
public class InfiniteLoopPruning<E extends TreeIterable<E>> implements PruningStrategy<E> {

	private final Set<E> iterated;

	public InfiniteLoopPruning() {
		this.iterated = new HashSet<E>();
	}

	@Override
	public boolean shouldContinue(E element) {
		if (iterated.contains(element)) return false;
		iterated.add(element);
		return true;
	}
}
