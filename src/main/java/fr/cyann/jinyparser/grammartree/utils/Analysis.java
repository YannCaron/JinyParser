package fr.cyann.jinyparser.grammartree.utils;/**
 * Copyright (C) 04/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.grammartree.GrammarElement;
import fr.cyann.jinyparser.grammartree.GrammarProduction;
import fr.cyann.jinyparser.grammartree.Recursive;

/**
 * The Analysis definition.
 */
public class Analysis {

	/**
	 * The default grammar production name (for entry point).
	 */
	public static final String DEFAULT_GRAMMAR_NAME = "Grammar";

	// TODO : Check that Production grammar is never null

	private Analysis() {
		throw new RuntimeException("Static class cannot be instantiated.");
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


}
