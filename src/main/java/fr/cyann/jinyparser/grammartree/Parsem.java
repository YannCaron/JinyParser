package fr.cyann.jinyparser.grammartree;
/**
 * Copyright (C) 14/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/


import fr.cyann.jinyparser.exceptions.JinyException;
import fr.cyann.jinyparser.lexem.Lexem;
import fr.cyann.jinyparser.parsetree.NonTerminal;
import fr.cyann.jinyparser.parsetree.ParsemBuildable;
import fr.cyann.jinyparser.parsetree.ParsemElement;
import fr.cyann.jinyparser.utils.MultilingualMessage;

import java.lang.reflect.Constructor;

/**
 * The Parsem class definition.<br>
 */
public class Parsem extends GrammarDecorator {

    private final Class<? extends ParsemElement> clazz;

    /**
     * Default constructor.
     *
     * @param clazz the grammar element class to create.
     * @param decorated the decorated grammar element.
     */
    public Parsem(Class<? extends ParsemElement> clazz, GrammarElement decorated) {
        super(decorated);
        this.clazz = clazz;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean lookahead(GrammarContext context) {
        return decorated.lookahead(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean parse(GrammarContext context) {
        context.resetTerm();

        boolean res = decorated.parse(context);

        if (res) {

            try {
                Constructor<? extends ParsemElement> constructor = clazz.getConstructor(Lexem.class);
                constructor.setAccessible(true);
                ParsemElement parsem = constructor.newInstance(context.getCurrentLexem());

                if (parsem instanceof NonTerminal) {
                    ((NonTerminal)parsem).build(context);
                }

                context.pushParsem(parsem);
            } catch (Exception e) {
                throw new JinyException(MultilingualMessage.create(e.getMessage()));
            }

        }

        return res;
    }
}
