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

	public static final String DEFAULT_GRAMMAR_NAME = "Grammar";

	private Analysis() {
		throw new RuntimeException("Static class cannot be instantiated.");
	}

	static GrammarElement findUsage(GrammarElement root) {

		Map<GrammarElement, Integer> usedElements = new HashMap<GrammarElement, Integer>();
		Map<GrammarElement, String> nodeNames = new HashMap<GrammarElement, String>();

		// count the usage of nodes
		for (GrammarElement element : root.depthFirstSearch()) {

			// is node then count
			if (element instanceof GrammarNode) {
				Integer used = usedElements.get(element);
				if (used == null) used = 0;
				usedElements.put(element, used + 1);
			}
		}

		// find node names
		/*GrammarNode currentNode = null;
		for (GrammarElement element : root.breadthFirstSearch()) {
			System.out.println(element);

			if (element instanceof GrammarNode) {
				currentNode = (GrammarNode)element;
			} else if (element instanceof ParsemCreator) {
				nodeNames.put(currentNode, ((ParsemCreator)element).getName());
			}

		}*/

		// interpose recursive to node used multi time.
		int num = 0;

		for (GrammarElement node : usedElements.keySet()) {
			int usage = usedElements.get(node);

			if (usage > 1) {

				//String name = nodeNames.get(node);
				String name = "G" + num++;
				GrammarElement newNode = new GrammarName(name, node);

				// all elements
				for (GrammarElement element : root.depthFirstSearch()) {
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
