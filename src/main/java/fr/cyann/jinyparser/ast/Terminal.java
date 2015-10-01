package fr.cyann.jinyparser.ast;/**
 * Copyright (C) 28/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The Terminal definition.
 */
public abstract class Terminal<V> extends Ast {

	private final Token token;
	private final V value;

	public Terminal(Token token, V value) {
		this.token = token;
		this.value = value;
	}

	@Override
	public Token getToken() {
		return token;
	}

	public final V getValue() {
		return value;
	}

	@Override
	public void buildAst(AstStack stack) {
		stack.pushAst(this);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + getValue() + "]";
	}

}
