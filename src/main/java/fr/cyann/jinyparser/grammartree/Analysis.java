package fr.cyann.jinyparser.grammartree;/**
 * Copyright (C) 04/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import java.util.HashMap;
import java.util.Map;

/**
 * The Analysis definition.
 */
class Analysis {

	// TODO : Check that Production grammar never null

	public static final String DEFAULT_GRAMMAR_NAME = "Grammar";
	public static final String DEFAULT_GRAMMAR_FORMAT = DEFAULT_GRAMMAR_NAME + "_%d";

	private Analysis() {
		throw new RuntimeException("Static class cannot be instantiated.");
	}

	static GrammarElement findUsage(GrammarElement root) {

		Map<GrammarElement, Integer> usedElements = new HashMap<GrammarElement, Integer>();
		Map<GrammarElement, String> nodeNames = new HashMap<GrammarElement, String>();

		// count the usage of nodes
		for (GrammarElement element : root.depthFirstTraversal()) {

			// is node then count
			if (element instanceof GrammarNode) {
				Integer used = usedElements.get(element);
				if (used == null) used = 0;
				usedElements.put(element, used + 1);
			}
		}

		// TODO :
		// find node names
		for (GrammarElement element : root.depthFirstTraversal()) {
			if (element instanceof TerminalCreator) {

				String name = ((TerminalCreator) element).getName();

				boolean found = false;
				for (GrammarElement parent : element.ascendingTraversal()) {

					if (parent instanceof NonTerminalAggregator) {
						found = true;
					} else if (!found && parent instanceof GrammarNode) {
						nodeNames.put(parent, name);
						found = true;
					}
				}
			}
		}

		// interpose recursive to node used multi time.
		int num = 0;

		for (GrammarElement node : usedElements.keySet()) {
			int usage = usedElements.get(node);

			if (usage > 1) {

				String name = nodeNames.get(node);
				if (name == null) name = String.format(DEFAULT_GRAMMAR_FORMAT, num++);
				GrammarElement newNode = new GrammarName(name, node);

				// all elements
				for (GrammarElement element : root.depthFirstTraversal()) {
					element.replace(node, newNode);
				}

				// and the root
				if (root == node) root = newNode;
			}
		}

		// naming of the root node
		if (root instanceof Recursive || root instanceof GrammarName) {
			return root;
		}
		return new GrammarName(DEFAULT_GRAMMAR_NAME, root);

	}

	// region inner class
	static class GrammarName extends Recursive {

		public GrammarName(String name, GrammarElement decorated) {
			super(name);
			super.grammar = decorated;
		}

		@Override
		protected boolean lookahead(GrammarContext context) {
			return super.grammar.lookahead(context);
		}

		@Override
		protected boolean parse(GrammarContext context) {
			return super.grammar.parse(context);
		}

		@Override
		public String toString() {
			return "GrammarName {" +
					"name='" + name + '\'' +
					'}';
		}
	}

	// end region

}
