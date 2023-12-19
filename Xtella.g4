grammar Xtella;

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

functionDeclaration
    : ( LET | LETREC | LETSEQ ) identifier '(' paramList ')' ':=' 
    (
       expression 'in' expression 'end;;'
       | expression
    )+
    ;

matchExpression
    : MATCH expression 'with' matchCaseList 'end'
    ;

matchCaseList
    : matchCase ('|' matchCase )*
    ;

matchCase
    : shellPattern '=>' expression
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
    : 'ident' | 'tyy' | 'block' | 'expr' | 'const' | 'var'
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
     : 'eval!' '{{' expression '}}'
     ;

primaryExpression
    : ioIdentifier
    | typeName
    | access
    | hashLiteral
    | listLiteral
    | regexLiteral
    | string
    | shellCommand
    | lambda
    | integerLiteral
    | floatLiteral
    | matchExpression
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
    | unaryExpression '>>' unaryExpression
    | unaryExpression '<<' unaryExpression
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

identifier
    :  IDENT
    ;

startRule
    : expression EOF 
    ;

INT: [0-9];


ALL: . | [\n\r];

ESC_SEQ: '\\'([abfnrtv'"?\\]|([0-7]{1,3})|('x'[0-9a-fA-F]+)|('u'[0-9a-fA-F]{4}));

CHAR: [a-zA-Z0-9_!@#$%^&*()[\]{}\\"':?<>,~`];
IDENT: [a-zA-Z_][a-zA-Z0-9_]*;

HEX_DIGIT: [0-9a-fA-F];
BIN_DIGIT: [0-1];
OCT_DIGIT: [0-7];

LET: 'let';
LETREC: 'letrect';
LETSEQ: 'let*';

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

ALL_CAPS: [A-F];

MATCH: 'match';

COMMENT: '#' .*? '\r'? '\n' -> skip;
WS: [ \t\r\n]+ -> skip;

