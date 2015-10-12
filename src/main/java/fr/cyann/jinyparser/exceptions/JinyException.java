package fr.cyann.jinyparser.exceptions;/**
 * Copyright (C) 09/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.utils.MultilingualMessage;

/**
 * The JinyException class definition.<br>
 * The super class of all Jiny parsing exceptions.
 */
public class JinyException extends RuntimeException {

	/**
	 * Constructor.
	 * @param message the multilingual message.
	 */
	public JinyException(MultilingualMessage message) {
		super(message.toString());
	}

	/**
	 * Constructor.
	 * @param message the multilingual formatted message.
	 */
	public JinyException(MultilingualMessage.FormattedMessage message) {
		super(message.toString());
	}

}
