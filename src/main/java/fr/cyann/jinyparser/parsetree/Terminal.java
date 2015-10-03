package fr.cyann.jinyparser.parsetree;/**
 * Copyright (C) 28/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.token.Lexem;

/**
 * The Terminal definition.
 */
public abstract class Terminal<V> extends Ast {

	private final Lexem lexem;
	private final V value;

	public Terminal(Lexem lexem, V value) {
		this.lexem = lexem;
		this.value = value;
	}

	@Override
	public Lexem getLexem() {
		return lexem;
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
