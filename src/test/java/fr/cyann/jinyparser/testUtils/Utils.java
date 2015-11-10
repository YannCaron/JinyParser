package fr.cyann.jinyparser.testUtils;
/**
 * Copyright (C) 18/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/


import fr.cyann.jinyparser.lexem.Lexem;

import java.util.ArrayList;
import java.util.List;

/**
 * The Utils class definition.<br>
 * -
 */
public class Utils {

    private Utils() {
        throw new RuntimeException("Cannot instantiate static class !");
    }

	public static List<String> lexerToTerms(Iterable<Lexem> lexer) {
		List<String> result = new ArrayList<String>();
        for (Lexem lexem : lexer) {
            result.add(lexem.getTerm());
        }

        return result;
    }

}
