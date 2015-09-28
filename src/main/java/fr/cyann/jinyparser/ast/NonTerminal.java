package fr.cyann.jinyparser.ast;/**
 * Copyright (C) 28/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The NonTerminal definition.
 */
public abstract class NonTerminal extends Ast {

	public abstract int childSize();

	public abstract Ast getChild(int index);

	public abstract void setChild(int index, Ast child);

	@Override
	public void buildAst(AstStack stack) {
		for (int i = childSize() - 1; i >= 0; i--) {
			Ast child = stack.popAst();
			setChild(i, child);
		}
		stack.pushAst(this);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName());
		sb.append("[");
		for (int i = 0; i < childSize(); i++) {
			if (i > 0) sb.append(", ");
			sb.append(getChild(i));
		}
		sb.append("]");
		return sb.toString();
	}
}
