package fr.cyann.jinyparser.parsetree;
/**
 * Copyright (C) 03/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/


import fr.cyann.jinyparser.lexem.Lexem;

/**
 * The ParsemBuildable class definition.<br>
 */
public interface ParsemBuildable {

    /**
     * Get the current constructed token.
     * @return the current token.
     */
    Lexem getCurrentLexem();

    /**
     * Pop (get and remove) ast element from the top of parser stack. Useful to drop elements together to build the tree.
     * @return the ast element at the top.
     */
    ParsemElement popParsem();
}
