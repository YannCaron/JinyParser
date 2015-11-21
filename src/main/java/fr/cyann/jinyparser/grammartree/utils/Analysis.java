package fr.cyann.jinyparser.grammartree.utils;/**
 * Copyright (C) 04/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.exceptions.JinyException;
import fr.cyann.jinyparser.grammartree.*;
import fr.cyann.jinyparser.utils.MultilingualMessage;

import java.util.*;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.*;

/**
 * The Analysis definition.
 */
public class Analysis {

	/**
	 * The default grammar production name (for entry point).
	 */
    private static final String DEFAULT_GRAMMAR_NAME = "Grammar";
    private static final String COUNTERED_GRAMMAR = "%1$s%2$d";
    private static final String LR_SUBSTITUTE_NAME = "%s";

	private Analysis() {
		throw new RuntimeException("Static class cannot be instantiated.");
	}

	public static GrammarElement analyse(GrammarElement root) {

		GrammarElement result = root;

		// add root production name
		result = processBnf(result);

		// enhance LR
		result = enhanceLRGrammar(result);

		// check names
		enhanceProductionNames(result);

		return result;
	}

	/**
	 * Process the grammar tree and prepare it for BNF generation.
	 *
	 * @param root the root of the grammar tree.
	 * @return the root of the grammar tree.
	 */
	public static GrammarElement processBnf(GrammarElement root) {

		// hide nested recursive and productions
		for (GrammarElement element : root.depthFirstTraversal()) {
			if (element instanceof Recursive) {
				Recursive recursive = (Recursive) element;
				if (recursive.getGrammar() instanceof GrammarProduction) {
					recursive.setHide(true);
				}
			}
		}

		// determine if name is useful for the root element.
		if (root instanceof Recursive || root instanceof GrammarProduction) {
			return root;
		} else {
			return new Recursive(DEFAULT_GRAMMAR_NAME).setGrammar(root);
		}

	}

	/**
	 * Process the grammar tree and evaluate there production names are twice. Increment a counter for them.
	 *
	 * @param root the root element of the grammar tree.
	 */
	public static void enhanceProductionNames(GrammarElement root) {

		Map<String, NamedGrammar> elementNames = new HashMap<String, NamedGrammar>();
		Map<String, Integer> counters = new HashMap<String, Integer>();

		for (GrammarElement element : root.depthFirstTraversal()) {
			if (element instanceof NamedGrammar) {

				NamedGrammar production = (NamedGrammar) element;
				String name = production.getName();

				NamedGrammar found = elementNames.get(name);

				if (found != null && production != found && !production.isHidden() && !found.isHidden()) { // check object reference are not equals ! keep the ==
					int newCounter = counters.containsKey(name) ? counters.get(name) + 1 : 1;
					counters.put(name, newCounter);

					name = String.format(COUNTERED_GRAMMAR, name, newCounter);
					production.setName(name);
				}

				elementNames.put(name, production);
			}
		}

	}

	/**
	 * Detect when LR grammar style is written and transform it to LL grammar.
	 *
	 * @param root the root element of the grammar tree.
	 * @return the new root.
	 */
	public static GrammarElement enhanceLRGrammar(GrammarElement root) {

		BuildCyclesContext context = new BuildCyclesContext();
		buildCyclesRecursive(context, root);
		System.out.println("Cycles detected: ");
		for (int i = context.getCycles().size() - 1; i >= 0; i--) {
			List<GrammarElement> cycle = context.getCycles().get(i);
			System.out.println(" - cycle: " + cycle);

            // find choice & sequence
            Recursive recursive = (Recursive) cycle.get(0);
            int choiceIndex = firstByType(cycle, Choice.class, 0);
            if (choiceIndex == -1) // TODO
                throw new JinyException(MultilingualMessage.create("Infinite loop detected in recursive grammar [%s]").setArgs(cycle.get(0).toString()));
			Choice choice = (Choice) cycle.get(choiceIndex);

            int sequenceIndex = firstByType(cycle, Sequence.class, choiceIndex);
            if (sequenceIndex == -1) // TODO
                throw new JinyException(MultilingualMessage.create("Infinite loop detected in recursive grammar [%s]").setArgs(cycle.get(0).toString()));
            Sequence sequence = (Sequence) cycle.get(sequenceIndex);

            // create sub expression
            GrammarElement child = cycle.get(choiceIndex + 1);
            GrammarElement nextChild = choice.getNextOf(child);
            String name = String.format(LR_SUBSTITUTE_NAME, recursive.getName());
            Recursive subRecursive = recursive(name).setGrammar(nextChild);
            choice.replace(nextChild, subRecursive);

			// replace the reference
            GrammarDecorator link = (GrammarDecorator) cycle.get(cycle.size() - 1);
            link.setDecorated(subRecursive);

            // build oneOrMore
            sequence.remove(link);
            sequence.getParent().replace(sequence, sequence(link, zeroOrOne(sequence)));



		}

		return root;
	}

	private static int firstByType(List<GrammarElement> list, Class<? extends GrammarElement> type, int start) {
		for (int i = start; i < list.size(); i++) {
			if (type.isAssignableFrom(list.get(i).getClass())) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gives the infinite cycles; e.g. all the cycles present in the graph that do not produce any lexem (in other words, all the cycles that do not contains any GrammarLeaf element like "Word", "CharIn" etc.).<br>
	 * Inspired from the Tarjan's Algorithms. But backtrack when a sub-rule that create lexem was found.
	 *
	 * @param context The build cycle context.
	 * @param element the element to explore.
	 */
	private static void buildCyclesRecursive(BuildCyclesContext context, GrammarElement element) {

		context.pushToPath(element);

		try {
			if (element instanceof Recursive) {
				if (context.wasExplored(element)) {
					context.buildCycle(element);
					context.setBroken(false);
					return;
				}
			}

			if (element instanceof GrammarNode) {

				for (GrammarElement child : ((GrammarNode) element)) {

					buildCyclesRecursive(context, child);
					if (context.isBroken()) {
						if (element instanceof Sequence)
							break;
						else
							context.setBroken(false);
					}

				}

			} else if (element instanceof GrammarDecorator) {
				buildCyclesRecursive(context, ((GrammarDecorator) element).getDecorated());
			} else {
				context.setBroken(true);
				return;
			}

		} finally {
			context.removeLastFromPath();
		}

	}

	private static class BuildCyclesContext {
		private final Set<GrammarElement> elementExplored;
		private final Stack<GrammarElement> paths;
		private final List<List<GrammarElement>> cycles;
		private boolean broken;

		public BuildCyclesContext() {
			elementExplored = new HashSet<GrammarElement>();
			paths = new Stack<GrammarElement>();
			cycles = new ArrayList<List<GrammarElement>>();
			broken = false;
		}

		public boolean wasExplored(GrammarElement element) {
			boolean result = elementExplored.contains(element);
			elementExplored.add(element);
			return result;
		}

		public void pushToPath(GrammarElement element) {
			paths.push(element);
		}

		public void removeLastFromPath() {
			paths.pop();
		}

		public void buildCycle(GrammarElement startPoint) {
			List<GrammarElement> list = new ArrayList<GrammarElement>();
			int start = paths.search(startPoint);
			for (int i = start; i < paths.size(); i++) {
				list.add(paths.get(i - 1)); // shift to get from first
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


/*
	private static List<GrammarElement> buildPath(RecursiveContext context, GrammarElement element) {

		List<GrammarElement> chain = new ArrayList<GrammarElement>();

		int index = context.paths.search(element);

		for (int i = index; i < context.paths.size(); i++) {
			GrammarElement pathElement = context.paths.get(i);
			chain.add(pathElement);
		}
		System.out.println("Loop detected ! " + chain);

		return chain;
	}

	private static int addRecursively(RecursiveContext context, GrammarElement element) {

		int count = 1;
		context.paths.add(element);
		for (GrammarElement child : element.depthFirstTraversal()) {
			count++;
			context.paths.add(child);
		}

		return count;
	}

	private static void recursive(RecursiveContext context, GrammarElement element, Tree<String> tree) {

		context.paths.push(element);

		if (element instanceof Recursive) {
			if (context.wasExplored(element)) {
				tree.addLeaf(((NamedGrammar) element).getName());

				buildPath(context, element);

				context.paths.pop();

				return;
			} else {
				if (tree != tree.getRoot()) {
					tree.addLeaf(((NamedGrammar) element).getName());
				}
				tree = tree.getRoot().addLeaf(((NamedGrammar) element).getName());
			}
		}

		if (element instanceof GrammarNode) {

			boolean first = true;

			int count = 0;
			for (GrammarElement child : ((GrammarNode) element)) {

				if (element instanceof Choice && !first) {
					tree = tree.getRoot().addLeaf(tree.getHead());
				}
				first = false;

				//tree.addLeaf(child.toString());
				recursive(context, child, tree);

				if (element instanceof Sequence) {
					if (child instanceof Recursive) {
						count++;
						context.paths.add(child);
					} else {
						count += addRecursively(context, child);
					}
				}

			}

			for (int i = 0; i < count; i++) context.paths.pop();


		} else if (element instanceof GrammarDecorator) {
			recursive(context, ((GrammarDecorator) element).getDecorated(), tree);
		} else if (element instanceof Recursive) {
			recursive(context, ((Recursive) element).getGrammar(), tree);
		} else {
			tree.addLeaf("inv");
		}

		context.paths.pop();

	}

	private static class RecursiveContext {
		private final Set<GrammarElement> elementExplored = new HashSet<GrammarElement>();
		public final Stack<GrammarElement> paths = new Stack<GrammarElement>();

		boolean wasExplored(GrammarElement element) {
			boolean result = elementExplored.contains(element);
			elementExplored.add(element);
			return result;
		}


	}*/

}
