package fr.cyann.jinyparser.parsetree;/**
 * Copyright (C) 28/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.exceptions.JinyException;
import fr.cyann.jinyparser.lexem.Lexem;
import fr.cyann.jinyparser.utils.MultilingualMessage;

/**
 * The Terminal definition.
 */
@SuppressWarnings("WeakerAccess")
public abstract class Terminal extends ParsemElement {

	public Terminal(Lexem lexem) {
		super(lexem);
	}

	@Override
	public void injectVisitor(VisitorInjector injector) {
		setVisitor(injector.getVisitorFor(this));
	}

	public void aggregate(String fieldName, ParsemElement element) {
        throw new JinyException(MultilingualMessage.create("Illegal function call ! Cannot aggregate element [%s] into terminal [%s] !").setArgs(element, this));
    }

}
