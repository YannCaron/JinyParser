package fr.cyann.jinyparser.grammartree;

/**
 * Copyright (C) 04/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import java.util.*;

/**
 * The GrammarElement class. Then top abstract class of all grammar elements.<br>
 * Based on Interpreter / Composite GoF design pattern. <br>
 * Give the ability to declare (declarative programming) the language grammar by nesting grammars elements together.
 */
public abstract class GrammarElement {

	private final boolean named;
	private final String name;

	public GrammarElement() {
		this.named = false;
		this.name = this.getClass().getSimpleName();
	}

	public GrammarElement(String name) {
		this.named = true;
		this.name = name;
	}

	/**
	 * Give the grammar name.
	 *
	 * @return the grammar name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return if the name was given (and not by default).
	 *
	 * @return if the name was given.
	 */
	public boolean isNamed() {
		return named;
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
	public abstract boolean parse(GrammarContext context);

	private void buildNewBNF(BuildEBNFContext context, String name) {
		StringBuilder sbGrammar = new StringBuilder();

		context.createGrammar(name);

		sbGrammar.append(name);
		sbGrammar.append(" := ");

		toEBNFAbstract(context, sbGrammar);
		context.bufferToGrammar(name, sbGrammar);
	}

	/**
	 * Build the EBNF representation of the grammar tree.<br>
	 * Method is abstract and must be provided by the grammar element.<br>
	 * Written on the top of Template method GoF design pattern.
	 *
	 * @param context the context needed to store grammar in the order of using.
	 * @param buffer  the string builder to append on it.
	 */
	protected abstract void toEBNFAbstract(BuildEBNFContext context, StringBuilder buffer);

	/**
	 * Build the EBNF representation of the grammar tree main method.<br>
	 * Written on the top of Template method GoF design pattern.
	 *
	 * @param context the context needed to store grammar in the order of using.
	 * @param buffer the string builder to append on it.
	 */
	public void buildBNF(BuildEBNFContext context, StringBuilder buffer) {
		if (isNamed() && !context.containsGrammar(name)) {
			buildNewBNF(context, name);
		}

		if (context.containsGrammar(name)) {
			buffer.append('<');
			buffer.append(this.name);
			buffer.append('>');
			context.useOf(this.name);
		} else {
			toEBNFAbstract(context, buffer);
		}

	}

	/**
	 * Give the BNF representation of the grammar expression.<br>
	 * Use abstractBuildString method to construct the tree toString representation.
	 *
	 * @return the BNF representation.
	 */
	@Override
	public String toString() {
		BuildEBNFContext context = new BuildEBNFContext();
		StringBuilder sb = new StringBuilder();

		if (!named) {
			buildNewBNF(context, "grammar");
		} else {
			buildBNF(context, sb);
		}

		StringBuilder result = new StringBuilder();
		for (String grammar : context.getSortedGrammars()) {
			if (result.length() > 0) result.append('\n');
			result.append(grammar);
		}
		return result.toString();

		//return buildString(new HashSet<GrammarElement>(), new StringBuilder()).toString();
	}

	/**
	 * The BuildEBNFContext definition.<br>
	 * Used to build EBNF representation of the grammar tree.
	 */
	public class BuildEBNFContext {

		private final Map<String, String> grammars;
		private final Map<String, Integer> using;

		/**
		 * Default constructor.
		 */
		public BuildEBNFContext() {
			grammars = new HashMap<String, String>();
			using = new HashMap<String, Integer>();
		}

		/**
		 * Create a new grammar.
		 *
		 * @param name the grammar name.
		 */
		public void createGrammar(String name) {
			grammars.put(name, "");
			using.put(name, 0);
		}

		/**
		 * Return if the grammar already exists.
		 *
		 * @param name the grammar name.
		 * @return true if grammar already exists.
		 */
		public boolean containsGrammar(String name) {
			return grammars.containsKey(name);
		}

		/**
		 * Put the entire buffer on the grammar.
		 *
		 * @param name   the grammar name.
		 * @param buffer the buffer.
		 */
		public void bufferToGrammar(String name, StringBuilder buffer) {
			grammars.put(name, buffer.toString());
		}

		/**
		 * Increment the utility of grammar.
		 *
		 * @param name the grammar name.
		 */
		public void useOf(String name) {
			using.put(name, using.get(name) + 1);
		}

		/**
		 * Return a sorted (by utility descending) of the grammar.
		 *
		 * @return the grammar EBNF representations.
		 */
		public List<String> getSortedGrammars() {
			List<String> sortedKeys = new ArrayList<String>(grammars.keySet());
			Collections.sort(sortedKeys, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return -using.get(o1).compareTo(using.get(o2));
				}
			});

			List<String> sortedGrammars = new ArrayList<String>();
			for (String key : sortedKeys) {
				sortedGrammars.add(grammars.get(key));
			}

			return sortedGrammars;
		}
	}

}
