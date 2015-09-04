# JinyParser

A Packrat parser (optimized PEG) on the top of a combinatoric framework (elegant) and able to generated JVM compiled parser (fast).

## Why a new Parser in Java ?

Because **JinyParser** was architectured on an elegant way, on the top of a combinatoric framework (no need of Backus-Naur Form or enhenced **BNF** that are external domain specific languages and not compiled).
Because **JinyParser** was built to be the fastest Java parser.

## Combinatoric framework ?
JinyParser is based on a **Combinatoric Framework (CF)** because I observed an evident fact. BNF is a language, and a language, when a computer parse it, create an AST. AST is a tree structure. Why not directly creating the tree structure by hand ? The **ParseNode** object was born on the top of a dual design patter (GoF **Interpreter** and **Decorator**).

### Simple
You know how to build a swing application ? By a combination of visual components and sometime decorators (like slide bars), you will easily be able to create a new **Jiny Grammar** !
ParserNode is a composite (GoF Interpreter to be more precise).
Non terminal nodes are Sequence and Assumption
Non terminal decorators are Optional and Repeat
Terminal nodes are for example Symbol or Word

### Fast & Safe
**Combinatoric framework** do not need an "old scool" **interpreted** language like BNF, eBNF and so on.
So it is faster in this way. And Safer too. Because it do not need to parse and external language without errors cheking. CF is compiled into Java and pass through Type checker and is auto-completed by your favourit IDE as well as any Java class.

### Scalable & flexible
Need a new parer node for specific purpose ? Don't worry ! You always be able to inherit from the ParserNode abstract class and create you own nodes. I proof it was possible to enhance JinyParser to create a context sensitive parser. *Tutorial comming soon !*

## Fast ?
**JinyParser** was created with another idea in head. Because a parser is a state machine (in a recursive descent way). Why not compiling it into JVM bytecode and avoiding the java call stack by using a Goto bytecode statement instead ?
It is faster and not constraint by the stack overflow problem. Thanks to [Evgeny Mandrikov
](https://github.com/Godin) to reveal the problem ;-)

### .parse() or .toJar()
**JP** Is able to runs in two ways:
As a classic parser, that interpret the Source code in a **Recursive Descent** way (**Packrat** optimized).
Or build a Jar, for several reasons:
  * less footprint
  * parsing execution fastness
  * avoid stack overflow problem of recursive descent parsing way



