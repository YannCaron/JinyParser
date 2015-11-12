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
import fr.cyann.jinyparser.parsetree.NonTerminal;
import fr.cyann.jinyparser.utils.MultilingualMessage;

/**
 * The TerminalProduction class definition.<br>
 */
public class NonTerminalProduction<P extends NonTerminal> extends GrammarProduction<P> {

	/**
	 * {@inheritDoc}
	 */
	public NonTerminalProduction(String name, Class<P> clazz, GrammarElement decorated) {
		super(name, clazz, decorated);
	}

	NonTerminal createParsem() {

		try {

			// createTerminal non terminal
			NonTerminal parsem = getParsemClass().newInstance();
			parsem.setVisitor(getVisitor());

			return parsem;

		} catch (Exception e) {
			throw new JinyException(MultilingualMessage.create(e.toString()));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean parse(GrammarContext context) {

		context.setCurrentCreator(this);

		boolean ret = decorated.parse(context);

		context.backToPreviousCreator();

		return ret;
	}

}
