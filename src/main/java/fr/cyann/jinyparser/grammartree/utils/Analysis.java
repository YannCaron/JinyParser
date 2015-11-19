package fr.cyann.jinyparser.grammartree.utils;/**
 * Copyright (C) 04/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.datastructure.ArrayTree;
import fr.cyann.datastructure.Tree;
import fr.cyann.jinyparser.grammartree.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The Analysis definition.
 */
public class Analysis {

	/**
	 * The default grammar production name (for entry point).
	 */
	public static final String DEFAULT_GRAMMAR_NAME = "Grammar";
	public static final String COUNTERED_GRAMMAR = "%1$s%2$d";

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

					//found.setName(String.format(COUNTERED_GRAMMAR, name, 0));
					name = String.format(COUNTERED_GRAMMAR, name, newCounter);
					production.setName(name);
					//throw new AnalysisException(MultilingualMessage.create("Production names [%s] cannot be twice !").setArgs(production.getName()));
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

		RecursiveContext context = new RecursiveContext();
		Tree<String> tree = new ArrayTree<String>("root");
		recursive(context, root, tree);

		System.out.println(tree.toString());
		//System.out.println("Result:\n" + context.sb);

		return root;
	}

	private static void recursive(RecursiveContext context, GrammarElement element, Tree<String> tree) {

		if (element instanceof Recursive) {
			if (context.wasExplored(element)) {
				tree.addLeaf(((NamedGrammar) element).getName());
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
			for (GrammarElement child : ((GrammarNode) element)) {

				if (element instanceof Choice && !first) {
					tree = tree.getRoot().addLeaf(tree.getHead());
				}
				first = false;

				//tree.addLeaf(child.toString());
				recursive(context, child, tree);

			}

		} else if (element instanceof GrammarDecorator) {
			recursive(context, ((GrammarDecorator) element).getDecorated(), tree);
		} else if (element instanceof Recursive) {
			recursive(context, ((Recursive) element).getGrammar(), tree);
		} else {
			tree.addLeaf("inv");
		}
	}

	private static class RecursiveContext {
		private Set<GrammarElement> elementExplored = new HashSet<GrammarElement>();

		boolean wasExplored(GrammarElement element) {
			boolean result = elementExplored.contains(element);
			elementExplored.add(element);
			return result;
		}


	}

}
