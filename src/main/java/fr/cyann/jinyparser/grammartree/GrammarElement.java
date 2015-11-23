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
import fr.cyann.jinyparser.tree.AscendingIterator;
import fr.cyann.jinyparser.tree.BreadthFirstIterator;
import fr.cyann.jinyparser.tree.DepthFirstIterator;
import fr.cyann.jinyparser.tree.TreeIterable;
import fr.cyann.jinyparser.utils.MultilingualMessage;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * The GrammarElement class. Then top abstract class of all grammar elements.<br>
 * Based on Interpreter / Composite GoF design pattern. <br>
 * Give the ability to declare (declarative programming) the language grammar by nesting grammars elements together.
 */
@SuppressWarnings("WeakerAccess")
public abstract class GrammarElement implements TreeIterable<GrammarElement> {

	private GrammarElement parent;

	// region properties

	/**
	 * Get if element has parent.
	 *
	 * @return true if element has parent.
	 */
	public boolean hasParent() {
		return (parent != null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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
	// endregion

	// region parsing

	/**
	 * The lookahead searching method. Used to find if following term / grammar is valid without consuming the lexemes.
	 *
	 * @param context the parsing context that contains all necessary resources to the parsing (iterators, flags and so on).
	 * @return true if lookahead succeed, false otherwise.
	 */
	protected abstract boolean lookahead(GrammarContext context);

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
	 * The parsing method. Used to parse the source code passed in context.
	 *
	 * @param context the parsing context that contains all necessary resources to the parsing (iterators, flags and so on).
	 * @return true if parsing succeed, false otherwise.
	 */
	protected abstract boolean parse(GrammarContext context);

	// endregion

	// region iterator

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<GrammarElement> ascendingTraversal() {
		return new Iterable<GrammarElement>() {
			@Override
			public Iterator<GrammarElement> iterator() {
				return new AscendingIterator<GrammarElement>(GrammarElement.this);
			}
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<GrammarElement> depthFirstTraversal() {
		return new Iterable<GrammarElement>() {
			@Override
			public Iterator<GrammarElement> iterator() {
				return new DepthFirstIterator<GrammarElement>(GrammarElement.this);
			}
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<GrammarElement> breadthFirstTraversal() {
		return new Iterable<GrammarElement>() {
			@Override
			public Iterator<GrammarElement> iterator() {
				return new BreadthFirstIterator<GrammarElement>(GrammarElement.this);
			}
		};
	}

	// endregion

	/**
	 * Replace the child element by another.
	 *
	 * @param element the element to be replaced in the list.
	 * @param by      the element to replace with.
	 */
	@SuppressWarnings("UnusedReturnValue")
	public abstract boolean replace(GrammarElement element, GrammarElement by);

	/**
	 * Process the grammar to prepare parsing.<br>
	 * Make analysis for :<br>
	 * - Determine top level productions (for bnf expression).<br>
	 * - Verify semantics like recursive consistency.
	 *
	 * @return the processed grammar object.
	 */
	public ProcessedGrammar process() {
		GrammarElement root = Analysis.analyse(this);
		return new ProcessedGrammar(root);
	}

	/**
	 * Build the bnf expression of the grammar.
	 *
	 * @param context the bnf context to build on.
	 */
	abstract void buildBnf(BnfContext context);

	/**
	 * Represent a processed grammar. e.g an analysed grammar.
	 */
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
			boolean result = root.parse(context);

			// check error
			if (!context.isTerminated() || !result) {
				System.out.println(context.toString());
				System.out.println("ERROR");
				throw new JinyException(
						MultilingualMessage.create("Error at position [%s], unknown symbol \"%s\"!")
								.translate(Locale.FRENCH, "Erreur à la position [%s], symbol \"%s\" inconnu!")
								.setArgs(context.getPositionToString(), context.currentChar()));
			}

			return context;
		}

		/**
		 * Express the grammar to bnf form.
		 *
		 * @return the bnf representation of the grammar.
		 */
		public String toBnf() {
			BnfContext context = new BnfContext();
			root.buildBnf(context);
			return context.toString();
		}
	}

	/**
	 * The BNF context useful to build BNF.
	 */
	protected static class BnfContext {

		/**
		 * The name of default root grammar node.
		 */
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
		 * Append string to the current stringBuilder.
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
		 * Create a new recursive and consume the current grammar built.
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


		/**
		 * Gives the string representation of the object.
		 *
		 * @return the string representation.
		 */
		@Override
		public String toString() {

			clear();

			boolean first = true;
			for (String production : productions.values()) {
				if (!first) sb.append("\n");
				first = false;
				sb.append(production);
			}

			return sb.toString();
		}
	}

}
