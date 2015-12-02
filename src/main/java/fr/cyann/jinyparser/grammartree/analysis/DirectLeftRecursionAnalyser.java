package fr.cyann.jinyparser.grammartree.analysis;/**
 * Copyright (C) 25/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.exceptions.JinyException;
import fr.cyann.jinyparser.grammartree.*;
import fr.cyann.jinyparser.utils.MultilingualMessage;
import org.apache.log4j.Logger;

import java.util.*;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.*;

/**
 * The DirectLeftRecursionAnalyser definition. Detect when LR grammar style is written and transform it to PEG compatible grammar.
 */
public class DirectLeftRecursionAnalyser implements GrammarTreeAnalyser {

	final static Logger logger = Logger.getLogger(DirectLeftRecursionAnalyser.class);

	// region inner class

	/**
	 * Gives all the unique infinite cycles; e.g. all the cycles present in the graph that do not produce any lexem (in other words, all the cycles that do not contains any GrammarLeaf element like "Word", "CharIn" etc.).<br>
	 * Inspired from the Tarjan's Algorithms. But backtrack when a sub-rule that create lexem was found.<br>
	 * For uniqueness purpose, it needs a tree dictionary algorithm (see oldBuildCyclesContext).
	 *
	 * @param context The build cycle context.
	 * @param element the element to explore.
	 */
	private static List<GrammarElement> findCycles(BuildCyclesContext context, GrammarElement element) {

		if (element instanceof Recursive) {
			if (context.wasExplored((Recursive) element)) {
				List<GrammarElement> cycle = context.buildCycle(element);
				return cycle;
			}
		}

		context.pushToPath(element);

		try {

			if (element instanceof GrammarNode) {

				for (GrammarElement child : ((GrammarNode) element)) {

					List<GrammarElement> cycle = findCycles(context, child);
					if (element instanceof Sequence || cycle != null) return cycle;

				}

			} else if (element instanceof GrammarDecorator) {
				return findCycles(context, ((GrammarDecorator) element).getDecorated());
			}

		} finally {
			context.removeLastFromPath();
		}

		return null;
	}

	// endregion

	// region utils

	private static List<GrammarElement> findCycles(GrammarElement element) {
		return findCycles(new BuildCyclesContext(), element);
	}

	private static boolean hasCycle(GrammarElement element, Recursive find) {
		if (element instanceof GrammarNode) {

			for (GrammarElement child : ((GrammarNode) element)) {

				boolean result = hasCycle(child, find);
				if (element instanceof Sequence || result == true) return result;

			}

		} else if (element instanceof Link) {
			return ((Link) element).getRecursive() == find;
		} else if (element instanceof GrammarDecorator) {
			return hasCycle(((GrammarDecorator) element).getDecorated(), find);
		}

		return false;
	}

	private static <E extends GrammarElement> void forFirstChildOfType(GrammarElement parent, Class<E> type, GrammarApplier<E> function) {
		for (GrammarElement element : parent.depthFirstTraversal(new LinkPruning())) {
			if (element.getClass().isAssignableFrom(type)) {
				function.apply((E) element);
				return;
			}

		}
	}

	private static <E extends GrammarElement> void forEachChildOfType(GrammarElement parent, Class<E> type, GrammarApplier<E> function) {
		for (GrammarElement element : parent.depthFirstTraversal(new LinkPruning())) {
			if (element.getClass().isAssignableFrom(type)) {
				function.apply((E) element);
			}

		}
	}

	private static int findIdFirstOfType(List<GrammarElement> list, Class<? extends GrammarElement> type, int start) {
		for (int i = start; i < list.size(); i++) {
			if (type.isAssignableFrom(list.get(i).getClass())) {
				return i;
			}
		}
		return -1;
	}

	private static <E extends GrammarElement> E getElementOfType(List<GrammarElement> list, Class<E> type, int index) {
		if (index == -1)
			throw new JinyException(
					MultilingualMessage
							.create("Infinite loop detected in recursive grammar [%s]")
							.setArgs(list.get(0).toString()));
		return (E) list.get(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GrammarElement analyse(GrammarElement root) {

		Map<Recursive, Recursive> lastLink = new HashMap<Recursive, Recursive>();

		List<GrammarElement> cycle;
		while ((cycle = findCycles(root)) != null) {

			// logging
			logger.warn(String.format("Cycles detected: %s", cycle));

			// find choice & sequence
			final Recursive recursive = (Recursive) cycle.get(0);
			int choiceIndex = findIdFirstOfType(cycle, Choice.class, 0);
			Choice choice = getElementOfType(cycle, Choice.class, choiceIndex);

			// create association if not exists.
			if (!lastLink.containsKey(recursive)) lastLink.put(recursive, recursive);

			// remove the cycleGrammar from the choice
			GrammarElement cycleGrammar = cycle.get(choiceIndex + 1);
			choice.getChildren().remove(cycleGrammar);

			// create the new recursive and switch grammars
			final Recursive newRecursive = recursive(recursive.getName() + "X");
			newRecursive.setGrammar(choice);

			for (GrammarElement element : choice) {

				if (hasCycle(element, recursive)) {

					forEachChildOfType(element, Link.class, new GrammarApplier<Link>() {
						@Override
						public void apply(Link element) {
							if (element.getRecursive() == recursive) {
								element.setRecursive(newRecursive);
							}
						}
					});
				}

			}

			// replace sub links in sequence
			int sequenceIndex = findIdFirstOfType(cycle, Sequence.class, choiceIndex);
			Sequence sequence = getElementOfType(cycle, Sequence.class, sequenceIndex);

			// create head of the new grammar
			GrammarElement head = sequence.getChildren().get(0);
			forFirstChildOfType(head, Link.class, new GrammarApplier<Link>() {
				@Override
				public void apply(Link element) {
					element.setRecursive(newRecursive);
				}
			});
			forEachChildOfType(head, NonTerminalAggregator.class, new GrammarApplier<NonTerminalAggregator>() {
				@Override
				public void apply(NonTerminalAggregator element) {
					element.setCreate(false);
				}
			});

			// create optional tail of the new grammar
			GrammarElement[] tailElements = new GrammarElement[sequence.getChildren().size() - 1];
			for (int i = 1; i < sequence.getChildren().size(); i++) {
				tailElements[i - 1] = sequence.getChildren().get(i);
			}

			GrammarElement tail = zeroOrOne(sequence(tailElements));
			forFirstChildOfType(tail, NonTerminalAggregator.class, new GrammarApplier<NonTerminalAggregator>() {
				@Override
				public void apply(NonTerminalAggregator element) {
					element.setCreate(true);
				}
			});
			sequence.getParent().replace(sequence, sequence(head, tail));
			recursive.setGrammar(cycleGrammar);

			// switch names
			if (cycleGrammar instanceof NonTerminalProduction) {
				NonTerminalProduction production = (NonTerminalProduction) cycleGrammar;
				String name = recursive.getName();
				recursive.setName(production.getName());
				recursive.setHide(true);
				newRecursive.setName(name);
			}

		}

		return root;
	}

	private interface GrammarApplier<E extends GrammarElement> {
		void apply(E element);
	}

	// endregion

	private static class BuildCyclesContext {
		private final Set<Recursive> recursiveSet;
		private final Stack<GrammarElement> paths;

		public BuildCyclesContext() {
			recursiveSet = new HashSet<Recursive>();
			paths = new Stack<GrammarElement>();
		}

		public boolean wasExplored(Recursive element) {
			return recursiveSet.contains(element);
		}

		public void pushToPath(GrammarElement element) {
			paths.push(element);

			if (element instanceof Recursive) {
				recursiveSet.add((Recursive) element);
			}
		}

		public void removeLastFromPath() {
			GrammarElement element = paths.pop();

			if (element instanceof Recursive) {
				recursiveSet.remove(element);
			}

		}

		public List<GrammarElement> buildCycle(GrammarElement startPoint) {
			int start = paths.indexOf(startPoint);
			List<GrammarElement> list = new ArrayList<GrammarElement>();
			for (int i = start; i < paths.size(); i++) {
				list.add(paths.get(i));
			}
			return list;
		}
	}

}
