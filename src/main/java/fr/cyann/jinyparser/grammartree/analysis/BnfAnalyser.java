package fr.cyann.jinyparser.grammartree.analysis;/**
 * Copyright (C) 25/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.grammartree.GrammarElement;
import fr.cyann.jinyparser.grammartree.GrammarProduction;
import fr.cyann.jinyparser.grammartree.Recursive;
import fr.cyann.jinyparser.tree.InfiniteLoopPruning;

/**
 * The BnfAnalyser definition. Process the grammar tree and prepare it for BNF generation.
 */
public class BnfAnalyser implements GrammarTreeAnalyser {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GrammarElement analyse(GrammarElement root) {
		// hide nested recursive and productions
		for (GrammarElement element : root.depthFirstTraversal(new InfiniteLoopPruning<GrammarElement>())) {
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
			return new Recursive(Analyser.DEFAULT_GRAMMAR_NAME).setGrammar(root);
		}
	}

}
