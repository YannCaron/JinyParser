package fr.cyann.jinyparser.grammartree;

/**
 * Copyright (C) 04/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.exceptions.JinyException;
import fr.cyann.jinyparser.utils.MultilingualMessage;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * The GrammarElement class. Then top abstract class of all grammar elements.<br>
 * Based on Interpreter / Composite GoF design pattern. <br>
 * Give the ability to declare (declarative programming) the language grammar by nesting grammars elements together.
 */
@SuppressWarnings("WeakerAccess")
public abstract class GrammarElement {

	private GrammarElement parent;

	/**
	 * The parent getter.
	 *
	 * @return the parent of the node.
	 */
	public GrammarElement getParent() {
		return parent;
	}

	/**
	 * the parent setter.
	 *
	 * @param parent the parent of the node.
	 */
	public void setParent(GrammarElement parent) {
		this.parent = parent;
	}

	/**
	 * Get if element has parent.
	 *
	 * @return true if element has parent.
	 */
	public boolean hasParent() {
		return (parent != null);
	}

	/**
	 * The lookahead searching method. Used to find if following term / grammar is valid without consuming the lexemes.
	 *
	 * @param context the parsing context that contains all necessary resources to the parsing (iterators, flags and so on).
	 * @return true if lookahead succeed, false otherwise.
	 */
	protected abstract boolean lookahead(GrammarContext context);

	/**
	 * The parsing method. Used to parse the source code passed in context.
	 *
	 * @param context the parsing context that contains all necessary resources to the parsing (iterators, flags and so on).
	 * @return true if parsing succeed, false otherwise.
	 */
	protected abstract boolean parse(GrammarContext context);

	/**
	 * Launch the lookahead search from a parsing method.
	 *
	 * @param context the parsing context that contains all necessary resources to the parsing (iterators, flags and so on).
	 * @param element the element where to launch the lookahead
	 * @return true if lookahead succeed, false otherwise.
	 */
	protected boolean launchLookahead(GrammarContext context, GrammarElement element) {
		context.markChar();
		boolean lookaheadResult = element.lookahead(context);
		context.rollbackChar();

		return lookaheadResult;
	}

	/**
	 * Process the grammar to prepare parsing.<br>
	 * Make analysis for :<br>
	 * - Determine top level productions (for bnf expression).<br>
	 * - Verify semantics like production consistency.
	 *
	 * @return the processed grammar object.
	 */
	public ProcessedGrammar process() {
		return new ProcessedGrammar(this);
	}

	/**
	 * Build the bnf expression of the grammar.
	 *
	 * @param context the bnf context to build on.
	 */
	abstract void buildBnf(BnfContext context);

	/**
	 * Build the BNF expression of the grammar hierarchy.
	 *
	 * @return the string expression.
	 */
	@Override
	public String toString() {
		BnfContext context = new BnfContext();
		//buildProductions(context);
		buildBnf(context);
		return context.toString();
	}

	// TODO a voir
	protected abstract void visit(Visitor visitor);

	protected void ascendingChain(SimpleVisitor visitor) {
		visitor.visitBefore(this);
		if (hasParent()) {
			getParent().ascendingChain(visitor);
		}
		visitor.visitAfter(this);
	}

	// TODO javadoc
	protected interface SimpleVisitor {
		void visitBefore(GrammarElement grammar);

		void visitAfter(GrammarElement grammar);
	}

	protected interface Visitor {
		void visitLeaf(GrammarLeaf grammar);

		void visitRecursiveBefore(ParsemProduction grammar);

		void visitRecursiveAfter(ParsemProduction grammar);

		void visitDecoratorBefore(GrammarDecorator grammar);

		void visitDecoratorAfter(GrammarDecorator grammar);

		void visitNodeBefore(GrammarNode grammar);

		void visitNodeAfter(GrammarNode grammar);
	}

	/**
	 * The BNF context useful to build BNF.
	 */
	protected static class BnfContext {

		/**
		 * The name of default root grammar node.
		 */
		public static final String ROOT_NAME = "Grammar";
		public static final String PRODUCTION_SYMBOL = " ::= ";
		private final Map<String, String> productions;

		private final StringBuilder sb;

		/**
		 * Default constructor.
		 */
		protected BnfContext() {
			productions = new LinkedHashMap<String, String>();
			sb = new StringBuilder();
		}

		/**
		 * Append string to the current stringbuilder.
		 *
		 * @param string the string to append.
		 */
		protected void append(String string) {
			sb.append(string);
		}

		private void clear() {
			sb.delete(0, sb.length());
		}

		/**
		 * Create a new production and consume the current grammar built.
		 *
		 * @param name    the grammar name.
		 * @param grammar the grammar element.
		 */
		protected void newProduction(String name, GrammarElement grammar) {

			if (productions.containsKey(name)) {
				sb.append(name);
			} else {
				productions.put(name, "");
				int begin = sb.length();

				sb.append(name);
				sb.append(PRODUCTION_SYMBOL);

				if (grammar != null) grammar.buildBnf(this);

				productions.put(name, sb.substring(begin));

				sb.delete(begin, sb.length());

				sb.append(name);

			}
		}

		@Override
		public String toString() {

			if (sb.length() != 0) {
				sb.insert(0, PRODUCTION_SYMBOL);
				sb.insert(0, ROOT_NAME);
				productions.put(ROOT_NAME, sb.toString());
				clear();
			}

			boolean first = true;
			for (String production : productions.values()) {
				if (!first) sb.append("\n");
				first = false;
				sb.append(production);
			}

			return sb.toString();
		}
	}

	public static class ProcessedGrammar {

		private final GrammarElement root;

		private ProcessedGrammar(GrammarElement root) {
			this.root = root;
		}

		/**
		 * The parsing entry method.
		 *
		 * @param source the source code to parse.
		 * @return the grammar context.
		 */
		public GrammarContext parse(String source) {
			GrammarContext context = new GrammarContext(source);
			root.parse(context);

			// check error
			if (!context.isTerminated()) {
				System.out.println(context.toString());
				System.out.println("ERROR");
				throw new JinyException(
						MultilingualMessage.create("Error at position [%s], unknown symbol \"%s\"!")
								.translate(Locale.FRENCH, "Erreur à la position [%s], symbol \"%s\" inconnu!")
								.setArgs(context.getPositionToString(), context.currentChar()));
			}

			return context;
		}

	}

	protected abstract class AbstractSimpleVisitor implements SimpleVisitor {
		@Override
		public void visitBefore(GrammarElement grammar) {

		}

		@Override
		public void visitAfter(GrammarElement grammar) {

		}
	}

	protected abstract class AbstractVisitor implements Visitor {
		@Override
		public void visitLeaf(GrammarLeaf grammar) {
		}

		@Override
		public void visitRecursiveBefore(ParsemProduction grammar) {

		}

		@Override
		public void visitRecursiveAfter(ParsemProduction grammar) {

		}

		@Override
		public void visitDecoratorBefore(GrammarDecorator grammar) {

		}

		@Override
		public void visitDecoratorAfter(GrammarDecorator grammar) {

		}

		@Override
		public void visitNodeBefore(GrammarNode grammar) {

		}

		@Override
		public void visitNodeAfter(GrammarNode grammar) {

		}
	}
}
