package fr.cyann.jinyparser.testUtils;
/**
 * Copyright (C) 25/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/


import java.lang.reflect.Field;

/**
 * The Reflexion class definition.<br>
 * -
 */
public class Reflexion {

    private Reflexion() {
        throw new RuntimeException("Cannot instantiate static class !");
    }

	public static <T> T getPrivateFieldValue(Object o, String fieldName) throws IllegalAccessException {
		Class<?> clazz = o.getClass();

		Field field = null;

		while (field == null && clazz.getSuperclass() != null) {
			try {
				field = clazz.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			}
		}

		if (field != null) {
			field.setAccessible(true);
			return (T) field.get(o);
		}

		return null;
	}


}
