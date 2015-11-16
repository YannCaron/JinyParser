package fr.cyann.jinyparser.grammartree.utils;/**
 * Copyright (C) 04/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.exceptions.JinyException;
import fr.cyann.jinyparser.grammartree.GrammarElement;
import fr.cyann.jinyparser.grammartree.GrammarProduction;
import fr.cyann.jinyparser.grammartree.NamedGrammar;
import fr.cyann.jinyparser.grammartree.Recursive;
import fr.cyann.jinyparser.utils.MultilingualMessage;

import java.util.HashMap;
import java.util.Map;

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

	// TODO : Check that Production grammar is never null

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

		if (root instanceof Recursive || root instanceof GrammarProduction) {
			return root;
		} else {
			return new Recursive(DEFAULT_GRAMMAR_NAME).setGrammar(root);
		}

	}

	/**
	 * Process the grammar tree and evaluate there production names are twice. Increment a counter for them.
	 *
	 * @param root the root of the grammar tree.
	 */
	public static void enhanceProductionNames(GrammarElement root) {

		Map<String, NamedGrammar> elementNames = new HashMap<String, NamedGrammar>();
		Map<String, Integer> counters = new HashMap<String, Integer>();

		for (GrammarElement element : root.depthFirstTraversal()) {
			if (element instanceof NamedGrammar) {

				NamedGrammar production = (NamedGrammar) element;
				String name = production.getName();

				NamedGrammar found = elementNames.get(name);

				if (found != null && production != found) { // check object reference are not equals ! keep the ==
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

	public static GrammarElement enhanceLRGrammar(GrammarElement root) {

		//System.out.println("Result:\n" + context.sb);

		return root;
	}

	public static class AnalysisException extends JinyException {
		/**
		 * Constructor.
		 *
		 * @param message the multilingual message.
		 */
		public AnalysisException(MultilingualMessage message) {
			super(message);
		}

		/**
		 * Constructor.
		 *
		 * @param message the multilingual formatted message.
		 */
		public AnalysisException(MultilingualMessage.FormattedMessage message) {
			super(message);
		}
	}


}
