package fr.cyann.jinyparser.parsetree;
/**
 * Copyright (C) 03/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/


/**
 * The ParsemBuilder Interface definition.<br>
 * Give the ability to produce certain parse element
 */
public interface ParsemBuilder {

    /**
     * Build a new instance of parse tree and put it on the top of parser stack.
     * @param context the grammar context.
     */
    ParsemElement buildParsem(ParsemBuildable context);

}
