package fr.cyann.jinyparser.grammartree.analysis;/**
 * Copyright (C) 25/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.grammartree.GrammarElement;
import fr.cyann.jinyparser.grammartree.NamedGrammar;

import java.util.HashMap;
import java.util.Map;

/**
 * The UniqueNameProcessor definition. Process the grammar tree and evaluate there production names are twice. Increment a counter for them.
 */
public class UniqueNameProcessor implements AnalyseProcessor {

	@Override
	public GrammarElement analyse(GrammarElement root) {

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

					name = String.format(Analyser.COUNTERED_GRAMMAR, name, newCounter);
					production.setName(name);
				}

				elementNames.put(name, production);
			}
		}

		return root;
	}

}
