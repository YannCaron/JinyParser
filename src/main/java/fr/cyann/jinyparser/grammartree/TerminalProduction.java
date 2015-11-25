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
import fr.cyann.jinyparser.parsetree.ParsemElement;
import fr.cyann.jinyparser.utils.MultilingualMessage;

import java.lang.reflect.Constructor;

/**
 * The TerminalProduction class definition.<br>
 */
public class TerminalProduction<P extends ParsemElement> extends GrammarProduction<P> {

	/**
	 * {@inheritDoc}
	 */
	public TerminalProduction(String name, Class<P> clazz, LexemCreatorCore decorated) {
		super(name, clazz, decorated);
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

				Constructor<P> constructor = getParsemClass().getConstructor(Lexem.class);
				constructor.setAccessible(true);
				ParsemElement parsem = constructor.newInstance(context.getCurrentLexem());
				parsem.setVisitor(getVisitor());
				context.pushParsem(parsem);

			} catch (Exception e) {
				throw new JinyException(MultilingualMessage.create(e.toString()));
			}

		}

		return res;
	}

}
