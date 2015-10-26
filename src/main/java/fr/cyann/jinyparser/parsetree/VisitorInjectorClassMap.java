package fr.cyann.jinyparser.parsetree;/**
 * Copyright (C) 26/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.exceptions.JinyException;
import fr.cyann.jinyparser.utils.MultilingualMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * The VisitorInjectorClassMap definition.
 */
public abstract class VisitorInjectorClassMap<C extends VisitorContext> extends VisitorInjector<C> {

	private Map<Class<? extends ParsemElement>, ParsemVisitor<? extends ParsemElement, C>> map;

	public VisitorInjectorClassMap() {
		map = new HashMap<Class<? extends ParsemElement>, ParsemVisitor<? extends ParsemElement, C>>();

		buildVisitors();
	}

	public <P extends ParsemElement> void addVisitor(Class<P> clazz, ParsemVisitor<P, C> visitor) {
		map.put(clazz, visitor);

	}

	public abstract void buildVisitors();

	@Override
	public <P extends ParsemElement> ParsemVisitor<P, C> getVisitorFor(P parsem) {
		ParsemVisitor<P, C> visitor = (ParsemVisitor<P, C>) map.get(parsem.getClass());
		if (visitor == null && !(parsem instanceof DefaultTerminal)) {
			throw new JinyException(MultilingualMessage.create("Cannot inject visitor for class [%s]. Class is not known by map.").setArgs(parsem.getClass().getName()));
		}
		return visitor;
	}

}
