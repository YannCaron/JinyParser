package fr.cyann.jinyparser.grammartree;/**
 * Copyright (C) 04/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static fr.cyann.jinyparser.grammartree.GrammarElement.AbstractVisitor;

/**
 * The Analysis definition.
 */
class Analysis {

	private Analysis() {
		throw new RuntimeException("Static class cannot be instantiated.");
	}

	static GrammarElement findUsage(GrammarElement root) {

		final Map<GrammarElement, Integer> usage = new HashMap<GrammarElement, Integer>();
		final Set<GrammarNode> repeatedNode = new HashSet<GrammarNode>();

		// evaluate usage
		root.visit(new AbstractVisitor() {
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
				super.visitDecorator(grammar);
			}

			@Override
			public void visitNode(GrammarNode grammar) {
				boolean found = usage.get(grammar) != null;
				appendToUsage(grammar);

				if (!found)
					super.visitNode(grammar);
				else
					repeatedNode.add(grammar);
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
			//node.getParent().replace(node, new Recursive("G" + num++).setGrammar(node));

			final GrammarElement newNode = new Recursive("G" + num++).setGrammar(node);

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
					try {
						grammar.replace(node, newNode);
					} catch (Exception e) {
					}
				}

				@Override
				public void visitNode(GrammarNode grammar) {
					super.visitNode(grammar);
					grammar.replace(node, newNode);
				}
			});
		}

		//
		if (root instanceof Recursive) {
			return root;
		}
		return new Recursive("Grammar").setGrammar(root);

	}

}
