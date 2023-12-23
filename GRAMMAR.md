# Grammar for Xtella in eBNF form

Xtella is a language which considers itself to be in the lineage of AWK and Perl. This file (GRAMMAR.md) contains the EBNF syntax for Xtella. The syntax was generated using ChatGPT based on the Stack VM (XtellaVM.java). It was then reworked and refined by Chubak Bidpaa into its current fork.

As of this moment, Xtella's grammar is highly WIP.


```
program           ::= { statement }

statement         ::= assignStmt | controlFlowStmt | functionStmt | fileStmt | threadStmt

assignStmt        ::= "let" identifier "=" expression ";"
controlFlowStmt   ::= ifStmt | whileStmt | jumpStmt | returnStmt | matchStmt | unlessstmt | forStmt | elifStmt | doInStmt

ifStmt            ::= "if" "(" condition ")" "do" block [ "else" block ] "end-if"
elifStmt	  ::= "elif" "(" condition ")" "do" block 
whileStmt         ::= "while" "(" condition ")" "do" block "end-while"
unlessStmt        ::= "unless" "(" condition ")" "do" block "end-unless"
forStmt		  ::= "for" expression "in" expression "do" block "end-for"
matchStmt	  ::= "match" expression "with" matchCase "end-match"
matchCase	  ::= "|" expression "=>" block ","
doInStmt	  ::= "do" "(" expression ")" "in" expression "end-do"
jumpStmt          ::= "jump" expression ";"
returnStmt        ::= "return" { expression } ";"

functionStmt      ::= "function" identifier "(" [ parameters ] ")" block

parameters        ::= identifier { "," identifier }

fileStmt          ::= openFileStmt | closeFileStmt | readFileStmt | writeFileStmt | appendFileStmt

openFileStmt      ::= "open" "file" "for" ( "reading" | "writing" | "reading-and-writing" ) expression ";"
closeFileStmt     ::= "close" "file" expression ";"
readFileStmt      ::= "read" "file" "for" "reading" expression ";"
writeFileStmt     ::= "write" "file" expression "with" expression ";"
appendFileStmt    ::= "append" "file" expression "with" expression ";"

threadStmt        ::= "run" "thread" identifier { "and" "join" } ";"

block             ::= "{" { statement } "}"

expression        ::= logicalOrExpr | "_"

logicalOrExpr     ::= logicalAndExpr { "||" logicalAndExpr }
logicalAndExpr    ::= equalityExpr { "&&" equalityExpr }
equalityExpr      ::= relationalExpr { ( "==" | "!=" ) relationalExpr }
relationalExpr    ::= additiveExpr { ( "<" | "<=" | ">" | ">=" ) additiveExpr }
additiveExpr      ::= multiplicativeExpr { ( "+" | "-" ) multiplicativeExpr }
multiplicativeExpr ::= unaryExpr { ( "*" | "/" | "%" ) unaryExpr }

lambdaExpr        ::= "(" parameters ")" "=>" block { "(" ")" }

ternaryExpr 	  ::= condition "?" expression ":" expression

unaryExpr         ::= [ ( "+" | "-" | "!" | "$" ) ] primaryExpr

primaryExpr       ::= identifier
                  | number
                  | string
                  | "(" expression ")"
                  | functionCall
                  | array
                  | hashmap
                  | regexMatch
                  | execCommand
		  | ternaryExpr
		  | lambdaExpr

functionCall      ::= identifier "(" [ arguments ] ")"
arguments         ::= expression { "," expression }

array            ::= "[" [ arrayElements ] "]"
arrayElements    ::= expression { "," expression }

hashmap          ::= "{" [ keyValues ] "}"
keyValues        ::= expression ":" expression { "," expression ":" expression }

regexMatch       ::= expression "~" expression

execCommand      ::= "exec-pipe" expression ";"


condition        ::= expression

identifier       ::= letter { letter | digit | "_" }

number           ::= integer | floatingPoint
integer          ::= digit { digit }
floatingPoint    ::= digit { digit } "." digit { digit }

string           ::= "\"" { character } "\""
character        ::= any-unicode-character-except-double-quote

letter           ::= "a" | "b" | ... | "z" | "A" | "B" | ... | "Z"
digit            ::= "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"

any-unicode-character-except-double-quote ::= ...

```
