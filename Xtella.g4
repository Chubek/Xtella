grammar Xtella;

identifier
    :  IDENT
    ;

typeName
    : 'string'
    | 'int'
    | 'float'
    | 'symbol'
    | identifier
    ;

sigil
    : SIGIL_VAR
    | SIGIL_ARR
    | SIGIL_HASH
    | SIGIL_CHAN
    ;

hashLiteral
    : '{' keyVal (',' keyVal)* '}'
    ;

keyVal
    : identifier ':' expression
    ;

listLiteral
    : '[' expression (',' expression)* ']'
    ;

tupleLiteral
    : '(' expression (',' expression)* ')'
    ;

accessArray
    : identifier '[' expression ']'
    ;

accessHash
    : identifier '{' expression '}'
    ;

accessRecord
    : identifier '::' identifier ( '::' identifier )*?
    ;

access
    : accessArray
    | accessHash
    | accessRecord
    | identifier
    ;

regexLiteral
    : '/' .*? '/'
    ;

character
    : '#\\' CHAR
    | '#\\u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    | '#\\U' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    | '#\\x' HEX_DIGIT HEX_DIGIT?
    | CHAR_NEWLINE
    | CHAR_SPACE
    | CHAR_RETURN
    | CHAR_BELL
    ;


string
    : '"' ( (~'"') .*? ) | ESC_SEQ '"'
    | '\''( (~'\'') .*? ) | ESC_SEQ '\''
    ;

multilineString
    : '"""' (~ '"""') (ESC_SEQ | ALL) '"""'
    | '```' (~ '```') (ESC_SEQ | ALL) '```'
    ;

shellCommand
    : '`' .*? '`'
    ;

ioIdentifier
    : STDIN
    | STDOUT
    | STDERR
    | '<DOCUMENT>'
    | '<' ALL_CAPS* '>'
    ;

datatypeDeclaration
    : 'datatype' typeName '=' variantList ';'
    ;

variantList
    : variant ('|' variant)*
    ;

variant
    : constructor ('of' typeName (',' typeName )*)?
    ;

constructor
    : identifier
    ;

lambda
    : 'fn' '(' paramList ')' '=>' expression
    ;

paramList
    : identifier (',' identifier)*
    ;

recordDeclaration
    : 'record' identifier '{' fieldList '}'
    ;

fieldList
    : field (',' field)*
    ;

field
    : identifier ':' typeName
    ;

shellPattern
    : ( patternElement | '\\' .)+
    ;

patternElement
    : '?' | '*' |'[' bracketContent ']' | '.'
    ;

bracketContent
    : (bracketElement | '\\' .)+
    ;

bracketElement
    : CHAR '-' CHAR | CHAR
    ;

subSeparator
    : '%%' 
    | '%' 
    | '#' 
    | '##' 
    | ':=' 
    | ':?' 
    | ':+' 
    | ':-'
    | '-'
    | '+'
    | '?'
    | '='
    ;

parameterSubstitution
    : '$' identifier
    | '${' identifier (subSeparator shellPattern)? '}'
    ;

stringFormatting
    : 'printf' '(' string (',' expression)* ')'
    ;

quoteParamName
    : 'ident' | 'tyy' | 'block' | 'expr' 
    | 'const' | 'var' | 'mac'   | 'chan'
    ;

quoteParam
    : '.' identifier ':' quoteParamName
    ;

quoteParams
    : quoteParam (',' quoteParam)*
    ;

unquoteParam
    : '.' identifier ':' expression
    ;

unquoteParams
    : unquoteParam (',' unquoteParam)*
    ;

macro
    : 'quote' identifier '!' '{{' quoteParams '}}'
    | 'unquote' identifier '!' '{{' unquoteParams '}}'
    ;

arguments
    : identifier (',' identifier )*
    ;

integerLiteral
    : INT+
    | ('0x' | '0X') HEX_DIGIT+
    | ('0b' | '0B') BIN_DIGIT+
    | ('0o' | '0O') OCT_DIGIT+
    ;

floatLiteral
    : INT+ ('.' INT+)?
    ;

channel
    : 'channel' identifier '!' '{{' expression '}}'
    | 'channel' '{{' expression '}}' identifier '!'
    ;

functionCall
    : '&' identifier '(' arguments? ')'
    ;

concurrentEval
    : '&eval!' '{{' expression '}}'
    ;

regexMatch
    : regexLiteral '=~' expression
    ;

regexUnmatch
    : regexLiteral '!=~' expression
    ;

lambdaEval
    : '(' lambda ')' '!' '{{' arguments* '}}'
    ;

printExpression
    : '>>>' ioIdentifier '!' '{{' expression '}}' 
    ;

primaryExpression
    : ioIdentifier
    | printExpression
    | typeName
    | access
    | hashLiteral
    | listLiteral
    | regexLiteral
    | regexMatch
    | regexUnmatch
    | character
    | string
    | shellCommand
    | shellPattern
    | lambda
    | lambdaEval
    | integerLiteral
    | floatLiteral
    | parameterSubstitution
    | stringFormatting
    | macro
    | functionCall
    | channel
    | concurrentEval
    | variableAssignment
    | variableDeclaration
    ;

unaryExpression
    : '~' primaryExpression
    | '-' primaryExpression
    | '+' primaryExpression
    | '(' primaryExpression ')'
    | primaryExpression
    ;

bitwiseExpression
    : unaryExpression '^' unaryExpression
    | unaryExpression '|' unaryExpression
    | unaryExpression '&' unaryExpression
    | unaryExpression '=>' unaryExpression
    | unaryExpression '=<' unaryExpression
    ;

multExpression
    : bitwiseExpression '*' bitwiseExpression
    | bitwiseExpression '%' bitwiseExpression
    | bitwiseExpression '/' bitwiseExpression
    | bitwiseExpression '//' bitwiseExpression
    | bitwiseExpression '**' bitwiseExpression
    ;

addExpression
    : multExpression '+' multExpression
    | multExpression '-' multExpression
    ;

compareExpression
    : addExpression '==' addExpression   
    | addExpression '>=' addExpression   
    | addExpression '<=' addExpression   
    | addExpression '>' addExpression   
    | addExpression '<' addExpression   
    | addExpression '!=' addExpression   
    ;

logicalExpression
    : compareExpression '&&' compareExpression
    | compareExpression '||' compareExpression
    | '(' compareExpression ')'
    | '!'  compareExpression
    ;

expression
    : logicalExpression (('|>' | '<|') logicalExpression)*?
    | '(' logicalExpression ')'
    ;

variableDeclaration
    : access ':=' expression ';;'
    ;

variableAssignment
    : sigil access '=' expression ';;'
    ;


ifStatement
    : 'if' expression 'then' statement+ ( 'else' statement+ )? 'end'
    ;

elifStatement
    : 'elif' expression 'then' statement+ 
    ;

forStatement
    : 'for' expression 'in' expression 'do' statement+ 'done'
    | 'for' (variableAssignment | variableDeclaration)* ';' expression ( ',' expression )*? ';' expression ( ',' expression )*? 'do' statement+ 'done'
    ;

whileStatement
    : 'while' expression 'do' statement+ 'done'
    | 'unless' expression 'do' statement+ 'done'
    ;

matchCase
    : expression '->' statement+ ';;'
    ;

matchStatement
    : 'match' expression 'with' matchCase ( '|' matchCase )*? 'end'
    ;

parameters
    : identifier ( ',' identifier )*?
    ;

functionDeclaration
    : 'let' identifier parameters? '='  statement+ ';;'
    ;

statement
    : expression
    | variableDeclaration
    | variableAssignment
    | forStatement
    | ifStatement
    | whileStatement
    | matchCase
    | functionDeclaration
    | recordDeclaration
    | datatypeDeclaration
    | '{{' statement+ '}}'
    ;

startRule
    : statement+ EOF 
    ;

INT: [0-9];


ALL: . | [\n\r];


ESC_SEQ: '\\'([abfnrtv'"?\\]|([0-7]{1,3})|('x'[0-9a-fA-F]+)|('u'[0-9a-fA-F]{4}));

CHAR: [a-zA-Z0-9_!@#$%^&*()[\]{}\\"':?<>,~`];
IDENT: [a-zA-Z_][a-zA-Z0-9_]*;

HEX_DIGIT: [0-9a-fA-F];
BIN_DIGIT: [0-1];
OCT_DIGIT: [0-7];

CHAR_NEWLINE: '#\\newline';
CHAR_TAB: '#\\tab';
CHAR_RETURN: '#\\return';
CHAR_BELL: '#\\bell';
CHAR_SPACE: '#\\space';

SIGIL_VAR: '$';
SIGIL_ARR: '@';
SIGIL_HASH: '%';
SIGIL_CHAN: '>';

STDIN: '<STDIN>';
STDOUT: '<STDOUT>';
STDERR: '<STDERR>';

ALL_CAPS: [A-Z];

MATCH: 'match';

COMMENT: '#' .*? '\r'? '\n' -> skip;
WS: [ \t\r\n]+ -> skip;

