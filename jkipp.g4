grammar JKipp;

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

quotedString
    : '"' (~["\\"] | '\\' .)* '"'
    | '\'' (~["\\"] | '\\' .)* '\''
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
    : matchCase ('|' matchCase)*
    ;

matchCase
    : pattern '=>' expression
    ;

shellPattern: (patternElement | ESCAPE_CHAR .)+;

patternElement: QUESTION_MARK | ASTERISK | L_BRACKET bracketContent R_BRACKET | ANY_CHAR;

bracketContent: (bracketElement | ESCAPE_CHAR .)+;

bracketElement: CHAR_RANGE | CHAR;

subSeparator: '%%' | '%' | '#' | '##' | ':=' | ':?' : ':+' | ':-';

parameterSubstitution
    : '$' identifier
    | '${' identifier (supSeparator shellPattern)? '}'
    ;

stringFormatting
    : 'printf' '(' quotedString (',' expression)* ')'
    ;

quoteParamName: 'ident' | 'tyy' | 'block' | 'expr' | 'const' | 'var';

quoteParam: '.' identifier ':' quoteParamName;

quoteParams: quoteParam (',' quoteParam)*;

unquoteParam: '.' identifier ':' expression;

unquoteParams: unquoteParam (',' unquoteParam)*;

macro
    : 'quote' identifier '!' '{{' quoteParams '}}'; 
    | 'unquote' identifier '!' '{{' unquoteParams '}}';
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
    | recordDeclaration
    | functionDeclaration
    | matchExpression
    | parameterSubstitution
    | stringFormatting
    | macro
    | '(' expression ')'
    | expression binaryOperator expression
    ;

variableDeclaration
    : sigil identifier ':=' expression ';'
    ;

variableAssignment
    : sigil identifier '=' expression ';'

binaryOperator
    : '+' | '-' | '*' | '/' | '%' | '==' | '!=' | '<' | '>' | '<=' | '>=' 
    | '+='| '-='| '*='| '/='| '%='| 
    ;

identifier
    :  IDENT;

startRule
    : expression EOF ;


CHAR_RANGE: CHAR '-' CHAR;

QUESTION_MARK: '?';
ASTERISK: '*';
L_BRACKET: '[';
R_BRACKET: ']';
ANY_CHAR: '.';
ESCAPE_CHAR: '\\';

CHAR: [a-zA-Z0-9_];
IDENT: [a-zA-Z_] [a-zA-Z0-9_]*;


