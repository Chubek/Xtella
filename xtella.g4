grammar Xtella;

type
    : 'string'
    | 'int'
    | 'float'
    | 'symbol'
    | identifier
    ;

sigil
    : '$'
    | '@'
    | '%'
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

regexLiteral
    : /\/.*\//
    ;

escapeSequence
    : '\\' (simpleEscape | octalEscape | hexEscape | unicodeEscape);

simpleEscape
    : [abfnrtv'"\?\\];

octalEscape
    : '\\' [0-7] [0-7]? [0-7]?;

hexEscape
    : '\\' 'x' HEX_DIGIT HEX_DIGIT;

unicodeEscape
    : '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT;

quotedString
    : '"' (~["\\"] | escapeSequence | '\\' .)* '"'
    | '\'' (~["\\"] | escapeSequence | '\\' .)* '\''
    ;

shellCommand
    : '`' .*? '`'
    ;

ioIdentifier
    : '<STDIN>'
    | '<STDOUT>'
    | '<STDERR>'
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
    : 'let' identifier '(' paramList ')' ':=' expression 'in' expression 'end'
    ;

matchExpression
    : 'match' expression 'with' matchCaseList 'end'
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
    : [0-9]+
    | [0xX] HEX_DIGIT+
    | [0bB] [0-1]+
    | [0oO] [0-7]+
    ;

floatLiteral
    : [0-9]+ ('.' [0-9]+)?;

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
    | '&' identifier '(' arguments? ')'
    | expression (binaryOperator expression)?
    ;

variableDeclaration
    : sigil identifier ':=' expression ';'
    ;

variableAssignment
    : sigil identifier '=' expression ';'
    ;

binaryOperator
    : '+' | '-' | '*' 
    | '/' | '%' | '==' 
    | '!='| '<' | '>' 
    | '<='| '>='| '+='
    | '-='| '*='| '/='
    | '%='| '@' | '^' 
    ;

identifier
    :  IDENT;

startRule
    : expression EOF ;


CHAR: [a-zA-Z0-9_];
IDENT: [a-zA-Z_] [a-zA-Z0-9_]*;

WS: [ \t\r\n]+ -> skip;

HEX_DIGIT: [0-9a-fA-F]+;

COMMENT: '#' .*? '\r'? '\n' -> skip;

