package fr.cyann.jinyparser.parsetree;/**
 * Copyright (C) 26/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The VisitorInjector definition.
 */
public abstract class VisitorInjector<C extends VisitorContext> {

	public abstract <P extends ParsemElement> ParsemVisitor<P, C> getVisitorFor(P parsem);

}
