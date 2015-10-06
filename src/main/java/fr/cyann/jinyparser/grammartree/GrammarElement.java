package fr.cyann.jinyparser.grammartree;

/**
 * Copyright (C) 04/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import java.util.HashSet;
import java.util.Set;

/**
 * The GrammarElement class. Then top abstract class of all grammar elements.<br>
 * Based on Interpreter / Composite GoF design pattern. <br>
 * Give the ability to declare (declarative programming) the language grammar by nesting grammars elements together.
 */
public abstract class GrammarElement {

	/**
	 * The lookahead searching method. Used to find if following term / grammar is valid without consuming the lexemes.
	 *
	 * @param context the parsing context that contains all necessary resources to the parsing (iterators, flags and so on).
	 * @return true if lookahead succeed, false otherwise.
	 */
	protected  abstract boolean lookahead(GrammarContext context);

	/**
	 * The parsing method. Used to parse the source code passed in context.
	 *
	 * @param context the parsing context that contains all necessary resources to the parsing (iterators, flags and so on).
	 * @return true if parsing succeed, false otherwise.
	 */
	public abstract boolean parse(GrammarContext context);

	/**
	 * Build the string representation of the object hierarchy abstract method.<br>
	 * Written on the top of Template method GoF design pattern.
	 *
	 * @param alreadyBuilt the set of object already already built (avoid stack overflow).
	 * @param sb           the string builder to append on it.
	 */
	public abstract void abstractBuildString(Set<GrammarElement> alreadyBuilt, StringBuilder sb);

	/**
	 * Build the string representation of the object hierarchy main method.<br>
	 * Manage the grammar cyclic references.
	 *
	 * @param alreadyBuilt the set of object already already built (avoid stack overflow).
	 * @param sb           the string builder to append on it.
	 */
	public StringBuilder buildString(Set<GrammarElement> alreadyBuilt, StringBuilder sb) {
		if (alreadyBuilt.contains(this)) {
			sb.append('<');
			sb.append(this.getClass().getSimpleName());
			sb.append('>');
			return sb;
		}
		alreadyBuilt.add(this);

		abstractBuildString(alreadyBuilt, sb);
		return sb;
	}

	/**
	 * Give the BNF representation of the grammar expression.<br>
	 * Use abstractBuildString method to construct the tree toString representation.
	 *
	 * @return the BNF representation.
	 */
	@Override
	public String toString() {
		return buildString(new HashSet<GrammarElement>(), new StringBuilder()).toString();
	}

}
