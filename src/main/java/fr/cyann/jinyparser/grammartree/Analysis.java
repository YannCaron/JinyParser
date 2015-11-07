package fr.cyann.jinyparser.grammartree;/**
 * Copyright (C) 04/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.parsetree.Terminal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static fr.cyann.jinyparser.grammartree.GrammarElement.AbstractVisitor;

/**
 * The Analysis definition.
 */
class Analysis {

    public static final String DEFAULT_GRAMMAR_NAME = "Grammar";

    private Analysis() {
        throw new RuntimeException("Static class cannot be instantiated.");
    }

    static GrammarElement findUsage(GrammarElement root) {

        final Map<GrammarElement, Integer> usage = new HashMap<GrammarElement, Integer>();
        final Set<GrammarNode> repeatedNode = new HashSet<GrammarNode>();

        // evaluate usage
        root.visit(new AbstractVisitor() {
            int num = 0;

            @Override
            public void visitLeaf(GrammarLeaf grammar) {
                super.visitLeaf(grammar);
            }

            @Override
            public void visitRecursive(Recursive grammar) {
                appendToUsage(grammar);
                super.visitRecursive(grammar);
            }

            @Override
            public void visitDecorator(GrammarDecorator grammar) {

                // TODO
                super.visitDecorator(grammar);
                if (grammar instanceof ParsemCreator) {
                    ParsemCreator creator = (ParsemCreator) grammar;
                    if (Terminal.class.isAssignableFrom(creator.getParsemClass())) {
                        GrammarName grammarName = new GrammarName(creator.getName(), creator);
                        if (grammar.getParent() != null)
                            grammar.getParent().replace(grammar, grammarName);
                    }
                }

            }

            @Override
            public void visitNode(GrammarNode grammar) {
                boolean found = usage.get(grammar) != null;
                appendToUsage(grammar);

                if (!found) {
                    super.visitNode(grammar);
                } else {
                    repeatedNode.add(grammar);
                }
            }

            public void appendToUsage(GrammarElement element) {
                Integer used = usage.get(element);
                if (used == null) used = 0;
                usage.put(element, used + 1);
            }
        });

        // interpose recursive to node used multi time.
        int num = 0;

        for (final GrammarNode node : repeatedNode) {
            final GrammarElement newNode = new GrammarName("G" + num++, node);

            root.visit(new AbstractVisitor() {
                @Override
                public void visitLeaf(GrammarLeaf grammar) {
                    super.visitLeaf(grammar);
                }

                @Override
                public void visitRecursive(Recursive grammar) {
                    super.visitRecursive(grammar);
                }

                @Override
                public void visitDecorator(GrammarDecorator grammar) {
                    super.visitDecorator(grammar);
                    grammar.replace(node, newNode);
                }

                @Override
                public void visitNode(GrammarNode grammar) {
                    super.visitNode(grammar);
                    grammar.replace(node, newNode);
                }
            });

            if (root == node) root = newNode;
        }

        // naming of the root node
        if (root.getClass().isAssignableFrom(Recursive.class)) {
            return root;
        }
        return new GrammarName(DEFAULT_GRAMMAR_NAME, root);

    }

    // region inner class

    static class GrammarName extends Recursive {

        public GrammarName(String name, GrammarElement decorated) {
            super(name);
            super.grammar = decorated;
        }

        @Override
        protected boolean lookahead(GrammarContext context) {
            return super.grammar.lookahead(context);
        }

        @Override
        protected boolean parse(GrammarContext context) {
            return super.grammar.parse(context);
        }

        @Override
        public String toString() {
            return "GrammarName {" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    // end region

}
