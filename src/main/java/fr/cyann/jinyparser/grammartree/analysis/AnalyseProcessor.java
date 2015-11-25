package fr.cyann.jinyparser.grammartree.analysis;/**
 * Copyright (C) 25/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.grammartree.GrammarElement;

/**
 * The AnalyseProcessor definition, a generic interface for all analyser processors.
 */
public interface AnalyseProcessor {

	GrammarElement analyse(GrammarElement root);

}
