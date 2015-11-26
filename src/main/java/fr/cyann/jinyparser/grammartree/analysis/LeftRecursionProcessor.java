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
 * The LeftRecursionProcessor definition. Detect when LR grammar style is written and transform it to PEG compatible grammar.
 */
public class LeftRecursionProcessor implements AnalyseProcessor {

	final static Logger logger = Logger.getLogger(LeftRecursionProcessor.class);

	// region inner class

	/**
	 * Gives the infinite cycles; e.g. all the cycles present in the graph that do not produce any lexem (in other words, all the cycles that do not contains any GrammarLeaf element like "Word", "CharIn" etc.).<br>
	 * Inspired from the Tarjan's Algorithms. But backtrack when a sub-rule that create lexem was found.
	 *
	 * @param context The build cycle context.
	 * @param element the element to explore.
	 */
	private static void findCycles(BuildCyclesContext context, GrammarElement element) {

		if (element instanceof Recursive) {
			if (context.wasExplored((Recursive) element)) {
				context.buildCycle(element);
				context.setBroken(false);
				return;
			}
		}

		context.pushToPath(element);

		try {

			if (element instanceof GrammarNode) {

				for (GrammarElement child : ((GrammarNode) element)) {

					findCycles(context, child);
					if (context.isBroken()) {
						if (element instanceof Sequence)
							break;
						else
							context.setBroken(false);
					}

				}

			} else if (element instanceof GrammarDecorator) {
				findCycles(context, ((GrammarDecorator) element).getDecorated());
			} else {
				context.setBroken(true);
			}

		} finally {
			context.removeLastFromPath();
		}

	}

	// endregion

	// region utils

	private static NonTerminalAggregator findSubAggregator(GrammarElement parent, boolean create) {
		if (parent instanceof NonTerminalAggregator) {
			NonTerminalAggregator aggregator = (NonTerminalAggregator) parent;
			if (aggregator.isCreate() == create) return aggregator;
		}

		for (GrammarElement child : parent.depthFirstTraversal()) {
			if (child instanceof NonTerminalAggregator) {
				NonTerminalAggregator aggregator = (NonTerminalAggregator) child;
				if (aggregator.isCreate() == create) return aggregator;
			}
		}
		return null;
	}

	private static int findIdFirstOfType(List<GrammarElement> list, Class<? extends GrammarElement> type, int start) {
		for (int i = start; i < list.size(); i++) {
			if (type.isAssignableFrom(list.get(i).getClass())) {
				return i;
			}
		}
		return -1;
	}

	private static <T extends GrammarElement> T getElementOfType(List<GrammarElement> list, Class<T> type, int index) {
		if (index == -1)
			throw new JinyException(
					MultilingualMessage
							.create("Infinite loop detected in recursive grammar [%s]")
							.setArgs(list.get(0).toString()));
		return (T) list.get(index);
	}

	@Override
	public GrammarElement analyse(GrammarElement root) {
		BuildCyclesContext context = new BuildCyclesContext();
		findCycles(context, root);

		for (int i = context.getCycles().size() - 1; i >= 0; i--) {
			List<GrammarElement> cycle = context.getCycles().get(i);

			// logging
			logger.warn(String.format("Cycles detected: %s", cycle));

			// find choice & sequence
			Recursive recursive = (Recursive) cycle.get(0);
			int choiceIndex = findIdFirstOfType(cycle, Choice.class, 0);
			Choice choice = getElementOfType(cycle, Choice.class, choiceIndex);

			// find the next child after the child that link to recursive
			int nextChoiceChildIndex = choice.getChildren().indexOf(cycle.get(choiceIndex + 1)) + 1;
			GrammarElement nextChoiceChild = choice.getChildren().get(nextChoiceChildIndex);

			// construct the new recursive
			String name = String.format(Analyser.LR_SUBSTITUTE_NAME, recursive.getName());
			Recursive subRecursive = recursive(name).setGrammar(nextChoiceChild);

			// replacement
			choice.getChildren().set(nextChoiceChildIndex, subRecursive);
			Link link = (Link) cycle.get(cycle.size() - 1);
			link.setRecursive(subRecursive);

			// build oneOrMore
			int sequenceIndex = findIdFirstOfType(cycle, Sequence.class, choiceIndex);
			Sequence sequence = getElementOfType(cycle, Sequence.class, sequenceIndex);
			GrammarElement sequenceChild = cycle.get(sequenceIndex + 1);
			sequence.getChildren().remove(sequenceChild);
			sequence.getParent().replace(sequence, sequence(sequenceChild, zeroOrOne(sequence)));

			// switch production create / aggregate
			NonTerminalAggregator headAggregator = findSubAggregator(sequenceChild, true);
			if (headAggregator != null) {
				NonTerminalAggregator tailFirstAggregator = findSubAggregator(sequence, false);
				headAggregator.setCreate(false);
				if (tailFirstAggregator != null)
					tailFirstAggregator.setCreate(true);
			}

		}

		return root;
	}

	// endregion

	private static class BuildCyclesContext {
		private final Set<Recursive> recursiveSet;
		private final Stack<GrammarElement> paths;
		private final List<List<GrammarElement>> cycles;
		private boolean broken;

		public BuildCyclesContext() {
			recursiveSet = new HashSet<Recursive>();
			paths = new Stack<GrammarElement>();
			cycles = new ArrayList<List<GrammarElement>>();
			broken = false;
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

		public void buildCycle(GrammarElement startPoint) {
			List<GrammarElement> list = new ArrayList<GrammarElement>();
			int start = paths.indexOf(startPoint);
			for (int i = start; i < paths.size(); i++) {
				list.add(paths.get(i)); // shift to get from first
			}

			cycles.add(list);
		}

		public List<List<GrammarElement>> getCycles() {
			return cycles;
		}

		public boolean isBroken() {
			return broken;
		}

		public void setBroken(boolean broken) {
			this.broken = broken;
		}
	}

}