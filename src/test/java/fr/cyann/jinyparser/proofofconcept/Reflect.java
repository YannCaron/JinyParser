package fr.cyann.jinyparser.proofofconcept;
/**
 * Copyright (C) 12/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/


import junit.framework.TestCase;

import java.lang.reflect.Field;

/**
 * The Reflect class definition.<br>
 * -
 */
public class Reflect extends TestCase {

    public static class Test
    {
        int a, b, c;

        Test d;//Your type will be System here (System dieselEngine)

        public static void main(String args[])
        {
            for(Field f : Test.class.getDeclaredFields())
            {
                if(f.getType() == Test.class)//Here you would retrieve the variable name when the type is dieselEngine.
                    System.out.println(f.getName());
            }

        }
    }

    public void testGetInstanceName() {
        new Test().main(null);
    }
}
