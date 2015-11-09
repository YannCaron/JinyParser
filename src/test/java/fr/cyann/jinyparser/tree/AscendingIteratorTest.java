package fr.cyann.jinyparser.tree;

import fr.cyann.jinyparser.grammartree.GrammarElement;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.*;

/**
 * Copyright (C) 09/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/
public class AscendingIteratorTest extends TestCase {

	public void testIterator() throws Exception {

		GrammarElement g3 = word("a");
		GrammarElement g2 = oneOrMore(g3);
		GrammarElement g1 = sequence(g2);
		GrammarElement g0 = recursive("a").setGrammar(g1);

		List<GrammarElement> result = new ArrayList<GrammarElement>();

		for (GrammarElement g : g3.ascendingTraversal()) {
			result.add(g);
		}

		System.out.println(result);
		assertEquals(Arrays.asList(g3, g2, g1, g0), result);

	}

}