package fr.cyann.jinyparser.unit;

import fr.cyann.jinyparser.utils.CharLookaheadIterator;
import junit.framework.TestCase;

/**
 * Copyright (C) 07/09/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/
public class CharLookaheadIteratorTest extends TestCase {

	public void test1() {
		String source = "hi !";
		CharLookaheadIterator it = new CharLookaheadIterator(source);

		assertTrue(it.hasNext());
		assertFalse(it.hasPrevious());
	}

}