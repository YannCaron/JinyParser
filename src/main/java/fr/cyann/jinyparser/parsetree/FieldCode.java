package fr.cyann.jinyparser.parsetree;/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

/**
 * The LexemType class  definition.<br>
 * An extensible Enum of token types.<br>
 * SEPARATOR and SYMBOL are default ones.
 */
public class FieldCode {

	/**
	 * Default field code.
	 */
	public static final FieldCode DEFAULT = new FieldCode("DEFAULT");

	private static int typeCounter;
	private final int code;
	private final String name;

	/**
	 * Default constructor.
	 * @param name the type name.
	 */
	public FieldCode(String name) {
		this.name = name;
		code = typeCounter++;
	}

	/**
	 * Test if object is equal to this.
	 * @param o the object to test.
	 * @return true if are equals.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FieldCode lexemType = (FieldCode) o;

		return code == lexemType.code;

	}

	/**
	 * Calculate the hash code of the object.
	 * @return the hash code.
	 */
	@Override
	public int hashCode() {
		return code;
	}

	/**
	 * The code accessor.
	 * @return the code.
	 */
	public int getCode() {
		return code;
	}

	/**
	 * The name accessor.
	 *
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Give the string representation of the object.
	 * @return the string representation.
	 */
	@Override
	public String toString() {
		return name;
	}
}
