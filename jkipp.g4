grammar JKipp;

options {
    output=AST;
    ASTLabelType=CommonTree;
}

root: program EOF;

program: ^( statement | functionDeclaration )+;

statement: ^( variableAssignment
            | assignment
            | ifStatement
            | foreachStatement
            | forStatement
            | whileStatement
            | unlessStatement
            | functionCall
            | datatypeDeclaration
            | deleteStatement
            );

sigil: '$' | '%' | '@';

pipeSeparatedStatement: '|' statement+ ('|' statement+ )*;

paramsDeclaration: identifier (' ' identifier)*;

functionDeclaration: ^(LET identifier (paramsDeclaration)? 'begin' pipeSeparatedStatement+ ('==>' expr)? 'end;;' );

variableDeclaration: ^(identifier (':=' expression ';')?);

variableAssignment: ^('=' sigil identifier expression ';');

indexedAssignment: ^('=' '&' identifier '[' expression ']' expression ';' );

assignment: ^(variableAssignment | indexedAssignment);

deleteStatement: ^(DELETE sigil identifier);

ifStatement: ^('if' expression 'then' program ('else' program)? 'end');

forStatement: ^('for' (variableDeclaration | assignment)
            expression
            (variableDeclaration | assignment)
            'do' program 'end');

whileStatement: ^('while' expression 'do' program 'end');

unlessStatement: ^('unless' expression 'do' program 'end');

foreachStatement: ^('foreach' identifier 'in' iterable 'do' program 'end');

functionCall: ^('&' identifier '(' argumentList? ')' ';');

expression: ^(binaryOperator primaryExpression primaryExpression)?;

primaryExpression: ^(identifier | constant | functionCall | '(' expression ')');

binaryOperator: ^('+' | '-' | '*' | '/' | '%' | '==' | '!=' | '<' | '>' | '<=' | '>=');

iterable: ^('[' expression* ']');

argumentList: ^(expression expression*);

itemMapping: ^('=>' identifier expression);

hashLiteral: ^('(' itemMapping* ')');

listLiteral: ^('{' expression* '}');

constant: ^(INTEGER | STRING | hashLiteral | listLiteral);

datatypeDeclaration: ^(DATATYPE identifier constructorList);

constructorList: ^(constructor (constructor)*);

constructor: ^('of' identifierList);

identifierList: ^(identifier (identifier)*);

identifier: [a-zA-Z_][a-zA-Z0-9_]*;

INTEGER: [0-9]+;

STRING: '"' .*? '"';

DATATYPE: 'datatype';

DELETE: 'delete';

LET: 'let';

WS: [ \t\r\n]+ -> skip;

COMMENT: '#' .*? '\r'? '\n' -> skip;

