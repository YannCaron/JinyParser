package fr.cyann.jinyparser.utils;/**
 * Copyright (C) 09/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import junit.framework.TestCase;

import java.util.Locale;

/**
 * The MultilingualMessageTest definition.
 */
public class MultilingualMessageTest extends TestCase {

	public void testDefault() {
		MultilingualMessage msg = new MultilingualMessage("Hi JINY !");
		assertEquals("Hi JINY !", msg.toString());
	}

	public void testDefaultArgs() {
		MultilingualMessage msg = new MultilingualMessage("Hi %s !");
		assertEquals("Hi JINY !", msg.setArgs("JINY").toString());
	}

	public void testDefaultAndFrench() {
		Locale.setDefault(Locale.ENGLISH);

		MultilingualMessage msg = new MultilingualMessage("Hi JINY !");
		assertEquals("Hi JINY !", msg.translate(Locale.FRENCH, "Salut JINY !").toString());
	}

	public void testDefaultAndFrenchArgs() {
		Locale.setDefault(Locale.ENGLISH);

		MultilingualMessage msg = new MultilingualMessage("Hi %s !");
		assertEquals("Hi JINY !", msg.translate(Locale.FRENCH, "Salut %s !").setArgs("JINY").toString());
	}


	public void testFrench() {
		Locale.setDefault(Locale.FRENCH);

		MultilingualMessage msg = new MultilingualMessage("Hi JINY !");
		assertEquals("Salut JINY !", msg.translate(Locale.FRENCH, "Salut JINY !").toString());
	}

	public void testFrenchArgs() {
		Locale.setDefault(Locale.FRENCH);

		MultilingualMessage msg = new MultilingualMessage("Hi %s !");
		assertEquals("Salut JINY !", msg.translate(Locale.FRENCH, "Salut %s !").setArgs("JINY").toString());
	}

	public void testFrance() {
		Locale.setDefault(Locale.FRANCE);

		MultilingualMessage msg = new MultilingualMessage("Hi JINY !");
		assertEquals("Salut JINY !", msg.translate(Locale.FRENCH, "Salut JINY !").toString());
	}

	public void testFranceArgs() {
		Locale.setDefault(Locale.FRANCE);

		MultilingualMessage msg = new MultilingualMessage("Hi %s !");
		assertEquals("Salut JINY !", msg.translate(Locale.FRENCH, "Salut %s !").setArgs("JINY").toString());
	}
}
