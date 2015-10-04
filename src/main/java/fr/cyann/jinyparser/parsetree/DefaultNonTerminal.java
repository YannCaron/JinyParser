package fr.cyann.jinyparser.parsetree;
/**
 * Copyright (C) 03/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/


import fr.cyann.jinyparser.token.Lexem;

import java.util.ArrayList;
import java.util.List;

/**
 * The DefaultNonTerminal class definition.<br>
 * -
 */
public class DefaultNonTerminal extends NonTerminal {

    private final List<ParsemElement> children;

    public static ParsemBuilder BUILDER(final int length) {
        return new ParsemBuilder() {
            @Override
            public ParsemElement buildParsem(ParsemBuildable context) {
                List<ParsemElement> children = new ArrayList<ParsemElement>();

                for (int i = length - 1; i>=0; i--) {
                    children.add(0, context.popParsem());
                }

                return new DefaultNonTerminal(children.get(0).getLexem(), children.get(length - 1).getLexem(), children);
            }
        };
    }

    public void append(int length, ParsemBuildable context) {

        int last = children.size();

        for (int i = length - 1; i>=0; i--) {
            children.add(last - 1, context.popParsem());
        }
    }

    private DefaultNonTerminal(Lexem lexemBegin, Lexem lexemEnd, List<ParsemElement> children) {
        super(lexemBegin, lexemEnd);
        this.children = children;
    }

    int size() {
        return children.size();
    }

    ParsemElement getItem(int index) {
        return children.get(index);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (ParsemElement child : children) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(child);
        }

        sb.insert(0, '(');
        sb.append(')');

        return sb.toString();
    }
}
