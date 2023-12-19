grammar Xtella;

type
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

regexLiteral
    : '/' ~SPACE '/'
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
    : '"' .* '"'
    | '\'' .* '\''
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
    : constructor ('of' type (',' type)*)?
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
    : identifier ':' type
    ;

functionDeclaration
    : ( LET | LETREC | LETSEQ ) identifier '(' paramList ')' ':=' 
    (
       expression 'in' expression 'end;;'
       | expression*
    )+
    ;

matchExpression
    : MATCH expression 'with' matchCaseList 'end'
    ;

matchCaseList
    : matchCase ('|' matchCase )*
    ;

matchCase
    : pattern '=>' expression
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
    : 'printf' '(' quotedString (',' expression)* ')'
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

binaryOperation
    : expression (binaryOperator expression)?
    ;

concurrentEval
     : 'eval!' '{{' expression '}}'
     ;

expression
    : ioIdentifier
    | type
    | variableDeclaration
    | variableAssignment
    | hashLiteral
    | listLiteral
    | regexLiteral
    | quotedString
    | shellCommand
    | datatypeDeclaration
    | lambda
    | integerLiteral
    | floatLiteral
    | recordDeclaration
    | functionDeclaration
    | matchExpression
    | parameterSubstitution
    | stringFormatting
    | macro
    | '(' expression ')'
    | functionCall
    | channel
    | binaryOperation
    | concurrentEval
    ;

variableDeclaration
    : identifier ':=' expression ';;'
    ;

variableAssignment
    : sigil identifier '=' expression ';;'
    ;

binaryOperator
    : '+' | '-' | '*' 
    | '/' | '%' | '==' 
    | '!='| '<' | '>' 
    | '<='| '>='| '+='
    | '-='| '*='| '/='
    | '%='| '@' | '^'
    | '||'| '&&'| '|'
    | '&' | '**'| '@'
    ;

identifier
    :  IDENT
    ;

startRule
    : expression EOF 
    ;

INT: [0-9];

CHAR: [a-zA-Z0-9_];
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

SPACE: [\x20];

MATCH: 'match';

COMMENT: '#' .*? '\r'? '\n' -> skip;
WS: [\x20\t\r\n]+ -> skip;
