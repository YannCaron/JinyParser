package fr.cyann.jinyparser.utils;/**
 * Copyright (C) 28/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import fr.cyann.jinyparser.grammartree.GrammarElement;

import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

/**
 * The RailroadDiagram definition.
 */
public class RailroadDiagram {

	private RailroadDiagram() {
		throw new RuntimeException("Cannot instantiate static class");
	}

	public static void Browse(GrammarElement root) {
		String urlFormat = "http://bottlecaps.de/rr/ui?ebnf=%s";
		String url = null;
		try {
			url = String.format(urlFormat, URLEncoder.encode(root.toString(), "UTF-8"));

			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.browse(new URI(url));
				} catch (IOException e) {
					// TODO : Exception
					e.printStackTrace();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			} else {
				Runtime runtime = Runtime.getRuntime();
				try {
					runtime.exec("xdg-open " + url);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
