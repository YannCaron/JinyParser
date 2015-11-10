package fr.cyann.jinyparser.grammartree;

import fr.cyann.jinyparser.parsetree.DefaultTerminal;
import fr.cyann.jinyparser.parsetree.ParsemVisitor;
import fr.cyann.jinyparser.parsetree.VisitorContext;
import junit.framework.TestCase;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.*;
import static fr.cyann.jinyparser.testUtils.Reflexion.getPrivateField;

/**
 * Copyright (C) 25/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

public class ParsemCreatorTest extends TestCase {

	public void testSetVisitor() throws Exception {
        ParsemCreator<DefaultTerminal> p = createTerminal("TODO:NAME", lexem(word("a")));

		ParsemVisitor<DefaultTerminal, VisitorContext> visitorField = getPrivateField(p, "visitor");
		assertNull(visitorField);

		ParsemVisitor<DefaultTerminal, VisitorContext> visitor = new ParsemVisitor<DefaultTerminal, VisitorContext>() {
			@Override
			public void visit(DefaultTerminal parsem, VisitorContext context) {
				// do nothing
			}
		};
		p.setVisitor(visitor);

		assertEquals(visitor, visitorField);

	}

	public void testLookahead() throws Exception {
		String source = "a";

        ParsemCreator<DefaultTerminal> p = createTerminal("TODO:NAME", lexem(word("a")));

		GrammarContext context = new GrammarContext(source);

		assertNull(context.getParseTree());

		p.lookahead(context);

		assertNull(context.getParseTree());
	}

	public void testParse() throws Exception {

		String source = "a";

        ParsemCreator<DefaultTerminal> p = createTerminal("TODO:NAME", lexem(word("a")));

		GrammarContext context = new GrammarContext(source);

		assertNull(context.getParseTree());

		p.parse(context);

		assertNotNull(context.getParseTree());
		assertEquals("a", context.getParseTree().getLexem().getTerm());
		assertTrue(context.getParseTree() instanceof DefaultTerminal);

	}
}