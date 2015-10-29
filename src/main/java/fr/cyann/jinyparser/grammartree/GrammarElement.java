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

import java.util.*;

/**
 * The GrammarElement class. Then top abstract class of all grammar elements.<br>
 * Based on Interpreter / Composite GoF design pattern. <br>
 * Give the ability to declare (declarative programming) the language grammar by nesting grammars elements together.
 */
@SuppressWarnings("WeakerAccess")
public abstract class GrammarElement {

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
	 * The parsing entry method.
	 *
	 * @param source the source code to parse.
	 * @return the grammar context.
	 */
/*	public GrammarContext parse(String source) {
        GrammarContext context = new GrammarContext(source);
		parse(context);

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
*/
    public BuiltGrammar build() {
        return new BuiltGrammar(this);
    }

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

	//abstract void buildProductions(BnfContext context); TODO : ????

    public static class BuiltGrammar {

        private final GrammarElement root;

        private BuiltGrammar(GrammarElement root) {
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

	protected static class BnfContext {

		public static final String ROOT_NAME = "grammar";
		private final Map<String, String> productions;
		private final List<String> productionNames;


		private final StringBuilder sb;

		protected BnfContext() {
			productions = new HashMap<String, String>();
			sb = new StringBuilder();
			productionNames = new ArrayList<String>();
		}

		protected void append(String string) {
			sb.append(string);
		}

		private void clear() {
			sb.delete(0, sb.length());
		}

		private void addProduction(String name, String bnf) {
			productionNames.add(0, name);
			productions.put(name, bnf);
		}

		protected void newProduction(String name, GrammarElement grammar) {

			if (productions.containsKey(name)) {
				sb.append(name);
			} else {
				productions.put(name, "");
				int begin = sb.length();

				sb.append(name);
				sb.append("::=");

				if (grammar != null) grammar.buildBnf(this);

				addProduction(name, sb.substring(begin));

				sb.delete(begin, sb.length());

				sb.append(name);

			}
		}

		@Override
		public String toString() {

			if (sb.length() != 0) {
				sb.insert(0, "::=");
				sb.insert(0, ROOT_NAME);
				addProduction(ROOT_NAME, sb.toString());
				clear();
			}

			boolean first = true;
			for (String key : productionNames) {
				if (!first) sb.append("\n");
				first = false;
				sb.append(productions.get(key));
			}

			return sb.toString();
		}
	}
}
