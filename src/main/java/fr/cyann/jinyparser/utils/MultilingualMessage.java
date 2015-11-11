package fr.cyann.jinyparser.utils;/**
 * Copyright (C) 09/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * The MultilingualMessage class definition. An object that contains a message in multilingual way.
 */
public class MultilingualMessage {

	private final String defaultEnglish;
	private final Map<String, String> langMessages;

	/**
	 * Default constructor. Create a message.
	 *
	 * @param defaultEnglish the default message in english. If locale is not found; this one is used.
	 */
	public MultilingualMessage(String defaultEnglish) {
		this.defaultEnglish = defaultEnglish;
		langMessages = new HashMap<String, String>();
	}

	/**
	 * Factory to createTerminal a message.
	 *
	 * @param defaultEnglish the default message in english.
	 * @return the message object.
	 */
	public static MultilingualMessage create(@SuppressWarnings("SameParameterValue") String defaultEnglish) {
		return new MultilingualMessage(defaultEnglish);
	}

	/**
	 * Add a translation of the message according the language.
	 *
	 * @param locale  the language locale.
	 * @param message the translated message.
	 * @return this.
	 */
	public MultilingualMessage translate(@SuppressWarnings("SameParameterValue") Locale locale, String message) {
		langMessages.put(locale.getLanguage(), message);
		return this;
	}

	/**
	 * Set arguments to format the message.
	 *
	 * @param args argument to format the message
	 * @return the message in formatted way.
	 */
	public FormattedMessage setArgs(Object... args) {
		return new FormattedMessage(args);
	}

	/**
	 * return the message according default locale.
	 *
	 * @return the formatted message.
	 */
	@Override
	public String toString() {
		if (langMessages.containsKey(Locale.getDefault().getLanguage())) {
			return langMessages.get(Locale.getDefault().getLanguage());
		}
		return defaultEnglish;
	}

	/**
	 * An object representing the message in formatted way (by String.format method).
	 */
	public class FormattedMessage {
		private final Object[] args;

		private FormattedMessage(Object[] args) {
			this.args = args;
		}

		/**
		 * Format the message according locale and return it.
		 * @return the formatted message.
		 */
		@Override
		public String toString() {
			return String.format(MultilingualMessage.this.toString(), args);
		}

	}

}
