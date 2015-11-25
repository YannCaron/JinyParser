package fr.cyann.datastructure;

import junit.framework.TestCase;

/**
 * Copyright (C) 18/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/
public class ArrayTreeTest extends TestCase {

	public void testGet() throws Exception {
		ArrayTree<String> root = new ArrayTree<String>("root");

		root.addLeaf("1");
		root.addLeaf("2");
		root.addLeaf("3");

		root.get(0).addLeaf("1.1");
		root.get(1).addLeaf("2.1");

		assertEquals("2", root.get(1).getHead());
		assertEquals("2.1", root.get(1).get(0).getHead());

		try {
			assertEquals("2.1.1", root.get(1).get(0).get(0).getHead());
			fail("Should throw an exception !");
		} catch (Exception e) {
			System.out.println("OK");
		}

		root.get(1).get(0).addLeaf("2.1.1");

		assertEquals("2.1.1", root.get(1).get(0).get(0).getHead());

	}

	public void testArrayTree() throws Exception {

		ArrayTree<String> root = new ArrayTree<String>("root");

		root.addLeaf("1");
		root.addLeaf("2");
		root.addLeaf("3");

		root.get(0).addLeaf("1.1");
		root.get(1).addLeaf("2.1");
		root.get(2).addLeaf("3.1");
		root.get(2).addLeaf("3.2");

		String result = "root\n" +
				"  1\n" +
				"    1.1\n" +
				"  2\n" +
				"    2.1\n" +
				"  3\n" +
				"    3.1\n" +
				"    3.2";

		assertEquals(result, root.toString());

	}
}