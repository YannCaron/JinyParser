package fr.cyann.jinyparser.grammartree;/**
 * Copyright (C) 16/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The NamedGrammar definition.
 */
public interface NamedGrammar {

	/**
	 * Get if named production is hidden or not.<br>
	 * Useful to avoid Recursive and Production nesting.
	 *
	 * @return true if hidden.
	 */
	boolean isHidden();

	/**
	 * Get the recursive name.
	 *
	 * @return the recursive name.
	 */
	String getName();

	/**
	 * Set the name attribute.
	 *
	 * @param name the value to set.
	 */

	void setName(String name);

}
