package fr.cyann.jinyparser.ast;/**
 * Copyright (C) 28/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import java.util.ArrayList;
import java.util.List;

/**
 * The DefaultNonTerminal definition.
 */
public class DefaultNonTerminal extends NonTerminal {

	private final List<Ast> children;

	public DefaultNonTerminal() {
		this.children = new ArrayList<Ast>();
	}

	@Override
	public int childSize() {
		return children.size();
	}

	@Override
	public Ast getChild(int index) {
		return children.get(index);
	}

	@Override
	public void setChild(int index, Ast child) {
		children.set(index, child);
	}
}
