package fr.cyann.jinyparser.grammartree;/**
 * Copyright (C) 01/10/15 Yann Caron aka cyann
 * <p/>
 * Cette œuvre est mise à disposition sous licence Attribution -
 * Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 France.
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/fr/
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 **/

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * The Choice class. A compound grammar node that choosing among its children nodes to determine the appropriate grammar.<br>
 * Run as an <b>or</b> operator (BNF:+ sign); check if this or this or this is the appropriate grammar.<br>
 * One of the most important node in the parser tree.<br>
 */
public class Choice extends GrammarNode {

    static Stack<Integer> identity = new Stack<Integer>();
    private final Map<Integer, Integer> packratMemoizer;
	private final Map<Integer, GrammarElement> packratMemoizer2;

	/**
	 * {@inheritDoc}
	 */
	public Choice(GrammarElement[] children) {
		super(children);
		this.packratMemoizer = new HashMap<Integer, Integer>();
		this.packratMemoizer2 = new HashMap<Integer, GrammarElement>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean lookahead(GrammarContext context) {

		//System.out.println(identity);

		int pos = context.currentPosition();
		//System.out.println(pos);

		/*if (packratMemoizer.containsKey(pos)) {
			//return packratMemoizer.get(pos).lookahead(context);
			Integer cache = packratMemoizer.get(pos);

			//if (cache == null) {
			//	return false;
			//} else {
			if (cache != 0) {
				for (int i = 0; i < cache; i++) context.nextCharLookahead();
				return true;
			}
			//}
		}*/

        int counter = 0;
        for (GrammarElement child : this) {

            identity.push(counter++);
            context.markChar();
			if (child.lookahead(context)) {
				// TODO: Packrat does not works properly.... difficulty to identify the position/choice. Conflict between choices at same position.
				//packratMemoizer.put(pos, context.currentPosition() - pos);
				//packratMemoizer2.put(pos, child);
				context.resumeChar();
                identity.pop();
                return true;
			}
			context.rollbackChar();

		}
		//packratMemoizer.put(pos, null);

        identity.pop();
        return false;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean parse(GrammarContext context) {
		int pos = context.currentPosition();

		if (packratMemoizer.containsKey(pos)) {
			GrammarElement cache = packratMemoizer2.get(pos);

			if (cache == null) {
				return false;
			} else {
				return cache.parse(context);
			}
		} else

			for (GrammarElement child : this) {
				boolean lookaheadResult = launchLookahead(context, child);
				if (lookaheadResult) {
					child.parse(context);
					return true;
				}

			}

		return false;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void buildBnf(BnfContext context) {
		boolean first = true;
		for (GrammarElement child : this) {
			if (!first) context.append(" | ");
			first = false;
			child.buildBnf(context);
		}
	}

}
