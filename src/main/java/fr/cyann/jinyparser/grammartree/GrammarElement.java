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

import java.util.Locale;

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
	public GrammarContext parse(String source) {
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

}
