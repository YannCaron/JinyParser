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

/**
 * The DefaultTerminal class definition.<br>
 * -
 */
public class DefaultTerminal<V> extends Terminal {

    private final V value;

    public DefaultTerminal(Lexem lexem, V value) {
        super(lexem);
        this.value = value;
    }

    public final V getValue() {
        return value;
    }

    public static <V> ParsemBuilder BUILDER(V defaultValue) {
        return new ParsemBuilder() {
            @Override
            public ParsemElement buildParsem(ParsemBuildable context) {
                Lexem lexem = context.getCurrentLexem();
                return new DefaultTerminal(lexem, lexem.getTerm());
            }
        };
    }


    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "'" + getValue() + "'";
    }

}
