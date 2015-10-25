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
        String source = "a";

        ParsemCreator<DefaultTerminal> p = createTerminal(lexem(word("a")));

        assertNull(getPrivateField(p, "visitor"));

        ParsemVisitor<DefaultTerminal, VisitorContext> visitor = new ParsemVisitor<DefaultTerminal, VisitorContext>() {
            @Override
            public void visit(DefaultTerminal parsem, VisitorContext context) {

            }
        };
        p.setVisitor(visitor);

        assertEquals(visitor, getPrivateField(p, "visitor"));

    }

    public void testLookahead() throws Exception {

    }

    public void testParse() throws Exception {

    }
}