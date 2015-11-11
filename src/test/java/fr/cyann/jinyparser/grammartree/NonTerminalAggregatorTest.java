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
public class NonTerminalAggregatorTest extends TestCase {

	public void testLookahead() throws Exception {

		String source = "ab";

        GrammarElement g = sequence(createNonTerminal("TODO:NAME", lexem(word("a"))), dropperDefault(createTerminal("TODO:NAME", lexem(word("b")))));

		GrammarContext context = new GrammarContext(source);

		assertNull(context.getParseTree());

		g.lookahead(context);

		assertNull(context.getParseTree());

	}

	public void testParse() throws Exception {

		String source = "ab";

        GrammarElement g = sequence(createNonTerminal("TODO:NAME", lexem(word("a"))), dropperDefault(createTerminal("TODO:NAME", lexem(word("b")))));

		GrammarContext context = new GrammarContext(source);

		assertNull(context.getParseTree());

		g.parse(context);

		DefaultNonTerminal parsem = ((DefaultNonTerminal) context.getParseTree());

		assertNotNull(context.getParseTree());
		assertTrue(context.getParseTree() instanceof DefaultNonTerminal);

		assertEquals("a", parsem.getLexem().getTerm());
		assertEquals("b", parsem.getItem(0).getLexem().getTerm());

	}
}