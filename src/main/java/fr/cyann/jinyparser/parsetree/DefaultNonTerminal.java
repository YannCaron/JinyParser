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

import java.util.ArrayList;
import java.util.List;

/**
 * The DefaultNonTerminal class definition.<br>
 * Aggregate its sub element with SUB_NODE_IDENTITY identity.
 * -
 */
public class DefaultNonTerminal extends NonTerminal {

    public static final String SUB_NODE_IDENTITY = "subNodes";

    @AggregateField(identity = SUB_NODE_IDENTITY)
    private final List<ParsemElement> children;

    public DefaultNonTerminal(Lexem lexemBegin) {
        super(lexemBegin);
        this.children = new ArrayList<ParsemElement>();
    }

    int size() {
        return children.size();
    }

	public ParsemElement getItem(int index) {
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
