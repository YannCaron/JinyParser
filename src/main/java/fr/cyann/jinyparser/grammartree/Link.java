package fr.cyann.jinyparser.grammartree;
/**
 * Copyright (C) 21/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/


/**
 * The Link class definition.<br>
 * -
 */
public class Link extends GrammarDecorator<Recursive> {

    public Link(Recursive recursive, GrammarElement parent) {
        super(recursive);
        this.setParent(parent);
    }

    public void setRecursive(Recursive recursive) {
        super.setDecorated(recursive);
    }

    @Override
    protected boolean lookahead(GrammarContext context) {
        return decorated.lookahead(context);
    }

    @Override
    protected boolean parse(GrammarContext context) {
        return decorated.parse(context);
    }

    @Override
    public String toString() {
        return "Link {" +
                "recursive='" + decorated.getName() + '\'' +
                '}';
    }
}
