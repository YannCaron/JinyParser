package fr.cyann.jinyparser.grammartree.analysis;/**
 * Copyright (C) 04/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.grammartree.GrammarElement;

import java.util.ArrayList;
import java.util.List;

/**
 * The Analyser definition.
 */
public class Analyser {

	/**
	 * The default grammar production name (for entry point).
	 */
	public static final String DEFAULT_GRAMMAR_NAME = "Grammar";
	public static final String COUNTERED_GRAMMAR = "%1$s%2$d";
	public static final String LR_SUBSTITUTE_NAME = "%s";

	private Analyser() {
		throw new RuntimeException("Static class cannot be instantiated.");
	}

	public static GrammarElement analyse(GrammarElement root) {

		// TODO : Epsilon production elimination
		// TODO : Cycles (A ++> A) elimination

		List<GrammarTreeAnalyser> analysers = new ArrayList<GrammarTreeAnalyser>();
		analysers.add(new BnfAnalyser());
		analysers.add(new DirectLeftRecursionAnalyser());
		analysers.add(new UniqueNameAnalyser());

		GrammarElement result = root;
		for (GrammarTreeAnalyser element : analysers) {
			result = element.analyse(result);
		}

		return result;
	}

}
