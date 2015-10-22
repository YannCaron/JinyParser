package fr.cyann.jinyparser.parsetree;
/**
 * Copyright (C) 21/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The ParsemVisitor definition.
 */
public abstract class ParsemVisitor<P extends ParsemElement, C extends VisitorContext> {

    public abstract void visit(P parsem, C context);

}
