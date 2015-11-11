package fr.cyann.jinyparser.parsetree;/**
 * Copyright (C) 11/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.grammartree.GrammarContext;
import fr.cyann.jinyparser.lexem.Lexem;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * The ParsemAccumulator definition.
 */
public class ParsemAccumulator extends ParsemElement {

	private final NonTerminal nonTerminal;
	private final Queue<ParsemElement> children;
	private boolean isConsistent;

	public ParsemAccumulator(Lexem lexem, NonTerminal nonTerminal) {
		super(lexem);
		this.nonTerminal = nonTerminal;
		children = new ArrayDeque<ParsemElement>();
		isConsistent = false;
	}

	public void setConsistent() {
		this.isConsistent = true;
	}

	@Override
	public <C extends VisitorContext> void injectVisitor(VisitorInjector<C> injector) {
		nonTerminal.injectVisitor(injector);
	}

	@Override
	public void aggregate(String fieldName, ParsemElement element) {
		nonTerminal.aggregate(fieldName, element);
		children.add(element);
	}

	public void build(GrammarContext context) {
		if (isConsistent) {
			context.pushParsem(nonTerminal);
		} else {
			while (!children.isEmpty()) {
				context.pushParsem(children.poll());
			}
		}
	}

}
