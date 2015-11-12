package fr.cyann.jinyparser.grammartree;

import fr.cyann.jinyparser.parsetree.DefaultTerminal;
import fr.cyann.jinyparser.parsetree.ParsemVisitor;
import fr.cyann.jinyparser.parsetree.VisitorContext;
import junit.framework.TestCase;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.lexem;
import static fr.cyann.jinyparser.grammartree.GrammarFactory.terminal;
import static fr.cyann.jinyparser.testUtils.Reflexion.getPrivateFieldValue;

/**
 * Copyright (C) 25/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

public class TerminalProductionTest extends TestCase {

	private static TerminalProduction getGrammar() {
		LexemCreator lexA = lexem(new Word("a"));
		return terminal("A", lexA);
	}

	public void testSetVisitor() throws Exception {
		TerminalProduction grammar = getGrammar();

		ParsemVisitor<DefaultTerminal, VisitorContext> visitorFieldValue = getPrivateFieldValue(grammar, "visitor");
		assertNull(visitorFieldValue);

		ParsemVisitor<DefaultTerminal, VisitorContext> visitor = new ParsemVisitor<DefaultTerminal, VisitorContext>() {
			@Override
			public void visit(DefaultTerminal parsem, VisitorContext context) {
				// do nothing
			}
		};
		grammar.setVisitor(visitor);

		visitorFieldValue = getPrivateFieldValue(grammar, "visitor");
		assertEquals(visitor, visitorFieldValue);

	}

	public void testLookahead() throws Exception {
		String source = "a";

		GrammarContext context = new GrammarContext(source);

		assertNull(context.getParseTree());

		getGrammar().lookahead(context);

		assertNull(context.getParseTree());
	}

	public void testParse() throws Exception {

		String source = "a";

		GrammarContext context = new GrammarContext(source);

		assertNull(context.getParseTree());

		getGrammar().parse(context);

		assertNotNull(context.getParseTree());
		assertEquals("a", context.getParseTree().getLexem().getTerm());
		assertTrue(context.getParseTree() instanceof DefaultTerminal);

	}
}