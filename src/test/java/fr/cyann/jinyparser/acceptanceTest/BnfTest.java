package fr.cyann.jinyparser.acceptanceTest;/**
 * Copyright (C) 04/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.grammartree.GrammarElement;
import junit.framework.TestCase;

/**
 * The VisitorTest definition.
 */
@SuppressWarnings("ALL")
public class BnfTest extends TestCase {

	public void testOperatorLevelWithParenthesisParser() {

		// grammar
		GrammarElement.ProcessedGrammar grammar = Grammars.arithmeticWithLevelAndParenthesis(null, null, null);

		// to BNF
		System.out.println("Grammar tree:\n" + grammar.toBnf());
		System.out.println();

		//RailroadDiagram.Browse(grammar);

	}

	public void testIfParser() {

		// grammar
		GrammarElement.ProcessedGrammar grammar = Grammars.dummyIf();

		// to BNF
		System.out.println("Grammar tree:\n" + grammar.toBnf());
		System.out.println();

		//RailroadDiagram.Browse(grammar);

	}

}
