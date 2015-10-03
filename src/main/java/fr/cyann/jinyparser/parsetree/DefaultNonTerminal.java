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

import java.util.Arrays;

/**
 * The DefaultNonTerminal class definition.<br>
 * -
 */
public class DefaultNonTerminal extends NonTerminal {

    private final ParsemElement[] children;

    public static ParsemBuilder BUILDER(final int length) {
        return new ParsemBuilder() {
            @Override
            public ParsemElement buildParsem(ParsemBuildable context) {
                ParsemElement[] children = new ParsemElement[length];

                for (int i = length - 1; i>=0; i--) {
                    children[i] = context.popParsem();
                }

                return new DefaultNonTerminal(children[0].getLexem(), children[length - 1].getLexem(), children);
            }
        };
    }

    public DefaultNonTerminal(Lexem lexemBegin, Lexem lexemEnd, ParsemElement[] children) {
        super(lexemBegin, lexemEnd);
        this.children = children;
    }

    int size() {
        return children.length;
    }

    ParsemElement getItem(int index) {
        return children[index];
    }

    @Override
    public String toString() {
        return Arrays.toString(children);
    }
}
