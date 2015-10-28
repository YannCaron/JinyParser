package fr.cyann.jinyparser.grammartree;

import fr.cyann.jinyparser.parsetree.DefaultNonTerminal;
import junit.framework.TestCase;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.*;

/**
 * Copyright (C) 28/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/
public class ParsemCatcherTest extends TestCase {

	public void testLookahead() throws Exception {

		String source = "ab";

		GrammarElement g = sequence(createTerminal(lexem(word("a"))), catcherDefault(createNonTerminal(lexem(word("b"))), 1));

		GrammarContext context = new GrammarContext(source);

		assertNull(context.getParseTree());

		g.lookahead(context);

		assertNull(context.getParseTree());

	}

	public void testParse() throws Exception {

		String source = "ab";

		GrammarElement g = sequence(createTerminal(lexem(word("a"))), catcherDefault(createNonTerminal(lexem(word("b"))), 1));

		GrammarContext context = new GrammarContext(source);

		assertNull(context.getParseTree());

		g.parse(context);

		DefaultNonTerminal parsem = ((DefaultNonTerminal) context.getParseTree());

		assertNotNull(context.getParseTree());
		assertTrue(context.getParseTree() instanceof DefaultNonTerminal);

		assertEquals("b", parsem.getLexem().getTerm());
		assertEquals("a", parsem.getItem(0).getLexem().getTerm());

	}
}