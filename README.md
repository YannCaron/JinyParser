# WORK IN PROGRESS - PARSER DOES NOT WORKS YET - COMING SOON (I HOPE)

# JinyParser

A Packrat parser (optimized PEG) on the top of a combinatoric framework and able to generated JVM compiled parser (fast).

## Why a new Parser in Java ?

Because **JinyParser** was build on the idea that architecture lead to flexibility, elegance and extensibility. In fact, on the top of a combinatoric framework (no need of Backus-Naur Form or enhanced **BNF** that are external domain specific languages and so not compiled).
Because **JinyParser** is able to compile the grammar into JVM bytecode, definitely the fastest way to implement a program in Java.

## Combinatoric framework ?
JinyParser is based on a **Combinatoric Framework (CF)** because I observed an evident fact; BNF is a language, and a language, when a computer parse it, create an AST. AST is a tree structure. Why not directly creating the tree structure by hand ? The **ParseNode** object was born on the top of a dual design patter (GoF **Interpreter** and **Decorator**).

### Simple
You know how to build a swing application ? By a combination of visual components and sometime decorators (like slide bars), you will easily be able to create a new **Jiny Grammar** !
ParserNode is a composite (GoF Interpreter to be more precise).
Non terminal nodes are Sequence and Assumption
Non terminal decorators are Optional and Repeat
Terminal nodes are for example Symbol or Word

### Fast & Safe
**Combinatoric framework** do not need an "old school" **interpreted** language like BNF, eBNF and so on.
So it is faster in this way. And Safer too. Because it do not need to parse and external language without errors checking. CF is compiled into Java and pass through Type checker and is auto-completed by your favourite IDE as well as any Java class.

### Scalable & flexible
Need a new **GrammarElement** for specific purpose ? Why not design it as a Context Sensitive Grammar extension ! Don't worry ! You always be able to inherit from the ParserNode abstract class and create you own nodes. I proof it was possible to enhance JinyParser to create a context sensitive parser. *Tutorial coming soon !*

## Fast ?
**JinyParser** was created with another idea in head; Because a parser is a state machine (in a recursive descent way). Why not compiling it into JVM bytecode and avoiding the java call stack by using a Goto bytecode statement instead ?
It is faster and not limited by the stack overflow problem of recursive calling. Thanks to [Evgeny Mandrikov](https://github.com/Godin) to reveal the problem ;-)

### .parse() or .toJar()
**JP** Is able to runs in two ways:
As a classic parser, that interpret the Source code in a **Recursive Descent** way (**Packrat** optimized).
Or build a Jar, for several reasons:
  * less footprint in your project
  * parsing execution fastness
  * avoid stack overflow problem of recursive descent parsing way
