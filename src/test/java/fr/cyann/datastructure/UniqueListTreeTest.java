package fr.cyann.datastructure;

import junit.framework.TestCase;

/**
 * Copyright (C) 27/11/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

public class UniqueListTreeTest extends TestCase {

    public void testGet() throws Exception {
        UniqueListTree<String> root = new UniqueListTree<String>("root");

        root.addLeaf("1");
        root.addLeaf("2");
        root.addLeaf("1");

        root.get(0).addLeaf("1.1");
        root.get(1).addLeaf("2.1");
        root.get(1).addLeaf("2.2");
        root.get(1).addLeaf("2.1");

        assertEquals(2, root.size());
        assertEquals(1, root.get(0).size());
        assertEquals(2, root.get(1).size());

    }


    public void testToTable() throws Exception {

        UniqueListTree<String> root = new UniqueListTree<String>("root");

        root.addLeaf("1");
        root.addLeaf("2");

        root.get(0).addLeaf("1.1");
        root.get(1).addLeaf("2.1");
        root.get(1).addLeaf("2.2");
        root.get(1).get(1).addLeaf("2.2.1");
        root.get(1).get(1).addLeaf("2.2.2");

        assertEquals("[[root, 1, 1.1], [root, 2, 2.1], [root, 2, 2.2, 2.2.1], [root, 2, 2.2, 2.2.2]]", root.toTable().toString());
        assertEquals("[[1, 1.1], [2, 2.1], [2, 2.2, 2.2.1], [2, 2.2, 2.2.2]]", root.toTableNoRoot().toString());

    }
}