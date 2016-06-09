/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.cyann.jinyparser.benchmarkTest;

import fr.cyann.jinyparser.acceptanceTest.Grammars;
import fr.cyann.jinyparser.grammartree.GrammarContext;
import fr.cyann.jinyparser.grammartree.GrammarElement;
import junit.framework.TestCase;

import static fr.cyann.jinyparser.grammartree.GrammarFactory.*;
import fr.cyann.jinyparser.grammartree.LexemCreator;
import fr.cyann.jinyparser.grammartree.NonTerminalProduction;
import fr.cyann.jinyparser.grammartree.Recursive;
import fr.cyann.jinyparser.lexem.LexemType;
import fr.cyann.jinyparser.parsetree.NonTerminal;
import fr.cyann.jinyparser.parsetree.ParsemElement;
import fr.cyann.jinyparser.parsetree.VisitorContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author caronyn
 */
public class GaselTest extends TestCase {

    public static final LexemType NAME = new LexemType("name");

    public static class Airblock extends NonTerminal {

        private Header header;
        private List<Point> points = new ArrayList<>();

        int pos = 0;

        @Override
        public void aggregate(String fieldName, ParsemElement element) {
            if (pos == 0) {
                header = (Header) element;
            } else {
                points.add((Point) element);
            }
        }

        @Override
        public String toString() {
            return "Airblock{" + "header=" + header + ", points=" + points + '}';
        }

    }

    public static class Header extends NonTerminal {

        private String name;

        int pos = 0;

        @Override
        public void aggregate(String fieldName, ParsemElement element) {
            if (pos == 1) {
                name = element.getLexem().getTerm();
            }
            pos++;
        }

        @Override
        public String toString() {
            return "Header{" + "name=" + name + '}';
        }

    }

    public static class Point extends NonTerminal {

        private double x, y;

        int pos = 0;

        @Override
        public void aggregate(String fieldName, ParsemElement element) {
            if (pos == 1) {
                x = Double.parseDouble(element.getLexem().getTerm());
            } else if (pos == 2) {
                y = Double.parseDouble(element.getLexem().getTerm());
            }
            pos++;
        }

        @Override
        public String toString() {
            return "Point{" + "x=" + x + ", y=" + y + '}';
        }

    }

    public static <C extends VisitorContext> GrammarElement.ProcessedGrammar getGrammar() {

        GrammarElement alphaNum = charIn("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_");
        GrammarElement num = charIn('0', '9');

        // lexer
        LexemCreator semicolon = lexem(LexemType.SYMBOL, charIn(";"));
        LexemCreator lexemA = lexem(LexemType.SYMBOL, charIn("A"));
        LexemCreator lexemP = lexem(LexemType.SYMBOL, charIn("P"));

        LexemCreator lexemName = lexem(NAME, oneOrMore(alphaNum));
        LexemCreator lexemNumber = lexem(Grammars.NUMBER, sequence(zeroOrOne(charIn("-")), oneOrMore(num), zeroOrOne(sequence(charIn("."), oneOrMore(num)))));

        // terminal
        GrammarElement a = terminal("a", lexemA);
        GrammarElement p = terminal("p", lexemP);
        GrammarElement name = terminal("name", lexemName);
        GrammarElement coord = terminal("coord", lexemNumber);

        // non terminal
        GrammarElement comment = sequence(charIn("#"), semicolon, oneOrMore(alphaNum), semicolon, oneOrMore(num), semicolon, oneOrMore(num), semicolon, oneOrMore(num), semicolon, oneOrMore(num), semicolon, oneOrMore(num), semicolon, oneOrMore(alphaNum));
        // <airblock> := 'A' ';' <name> ';' <name>
        //GrammarElement airblock = nonTerminal("airblock", sequence(create(a), semicolon, aggregate(name), semicolon, aggregate(name)));
        NonTerminalProduction<Header> header = nonTerminal("header", Header.class, sequence(create(a), semicolon, aggregate(name), semicolon, aggregate(name)));
        // <point> := 'P' ';' <number> ';' <number>
        NonTerminalProduction<Point> point = nonTerminal("point", Point.class, sequence(create(p), semicolon, aggregate(coord), semicolon, aggregate(coord)));

        // recursive
        Recursive gar = recursive("gar");

        // non terminal
        gar.setGrammar(sequence(comment,
                oneOrMore(
                        //nonTerminal("airblock", Airblock.class, sequence(create(header), zeroOrMore(aggregate(point))))
                        sequence(header, zeroOrMore(point))
                )
        ));

        // process
        return gar.process();

    }

    public void testGarFile() throws IOException {

        GrammarElement.ProcessedGrammar grammar = getGrammar();

        // to BNF
        System.out.println("Grammar tree:\n" + grammar.toBnf());
        System.out.println();

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is = cl.getResourceAsStream("test.gar");

        String source = IOUtils.toString(is);
        /*String source = "#;AIRBLOCK;1;413;20160526;20160622;6763;EAR_P\n"
         + "A;000EG;74\n"
         + "P;57;-15";*/

        long time = System.currentTimeMillis();

        // parse
        GrammarContext c = grammar.parse(source);

        System.out.println(String.format("Time spent %d", System.currentTimeMillis() - time));

        System.out.println("Parse tree: " + c.getParseTree());
    }

}
