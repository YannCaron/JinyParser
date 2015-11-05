package fr.cyann.jinyparser.utils;/**
 * Copyright (C) 28/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static fr.cyann.jinyparser.grammartree.GrammarElement.ProcessedGrammar;

/**
 * The RailroadDiagram definition.
 */
public class RailroadDiagram {

    public static final String BOTTLE_CAPS_URL_FORMAT = "http://bottlecaps.de/rr/ui?ebnf=%s";

    private RailroadDiagram() {
        throw new RuntimeException("Cannot instantiate static class");
    }

	public static void Browse(ProcessedGrammar root) {
		String urlFormat = BOTTLE_CAPS_URL_FORMAT;
        String url = null;
        try {
	        url = String.format(urlFormat, URLEncoder.encode(root.toBnf(), "UTF-8"));
	        Internet.Browse(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

}
