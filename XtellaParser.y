%token IDENTIFIER LET FILEHANDLE
%token EQUALS BECOMES
%token PERCENT DOLLAR SQUOTE DQUOTE BACKTICK PIPE SLASH NEWLINE LETTER DOLLAR_LCURLY TRIPLE_QUOTE
%token CHARSEQ_WITH_NEWLINE CHARSEQ_WITHOUT_NEWLINE
%token OCT_NUM HEX_NUM BIN_NUM
%token BIT_XOR BIT_AND BIT_NOT BIT_OR UNDERLINE U_PLUS U_MINUS NEG
%token ARROW WITH IN MATCH 
%token READ WRITE INTO FROM
%token READING WRITING APPENDING READ_AND_WRITE
%token APPEND DOUBLE_GT DOUBLE_LT
%token OPEN CLOSE PRINT EXEC AS
%token ELSE DO END FOR IF WHILE RETURN UNLESS DEFUN 
%token LPAREN RPAREN
%token LCURLY RCURLY
%token OR AND
%token EQ NEQ
%token PIPES_LEFT PIPES_RIGHT
%token LT LTE GT GTE
%token PLUS MINUS
%token MULTIPLY DIVIDE MODULO POW
%token FLOAT_NUM SCI_NUM
%token BIN_NUM_PREFIX HEX_NUM_PREFIX OCT_NUM_PREFIX DECIMAL_NUM
%token REGEX_MATCH REGEX_NOT_MATCH
%token COMMA
%token QMARK COLON
%token Q_LT Q_LPAREN Q_LCURLY Q_PERCENT Q_LBRACK
%token M_LT M_LCURLY M_LPAREN M_PERCENT M_LBRACK
%token C_ESCAPES UNICODE_ESCAPES HEX_ESCAPES OCT_ESCAPES

%left OR
%left AND
%left BIT_OR
%left BIT_XOR
%left BIT_AND
%left EQ NEQ LT LTE GT GTE
%left PLUS MINUS
%left MULTIPLY DIVIDE MODULO
%left POW
%left QMARK COLON
%left PIPES_LEFT
%right PIPES_RIGHT
%right U_PLUS U_MINUS NEG BIT_NOT DOLLAR
%left INDEX
%right SLASH_
%left M_SLASH M_LT M_LCURLY M_LPAREN M_PERCENT M_LBRACK
%left Q_SLASH Q_LT Q_LCURLY Q_LPAREN Q_PERCENT Q_LBRACK

%nonassoc REG_MATCHES REG_NOT_MATCHES
%nonassoc ELSE
%nonassoc DO
%nonassoc SEMICOLON
%nonassoc COMMA
%nonassoc ARROW
%nonassoc RETURN
%nonassoc MATCH
%nonassoc UNLESS
%nonassoc FOR
%nonassoc WHILE
%nonassoc IF
%nonassoc LBRACK RBRACK
%nonassoc LBRACE RBRACE
%nonassoc LPAREN RPAREN
%nonassoc LCURLY RCURLY

%start program


%%

program           : statement_list
		  ;

statement_list    : statement
                   | statement_list statement
		   ;

statement         : assign_stmt
                   | control_flow_stmt
                   | function_stmt
                   | rw_stmt
		   | exec_stmt
		   | open_stmt
		   | close_stmt
		   ;

assign_stmt       : IDENTIFIER BECOMES expression
                   | IDENTIFIER EQUALS expression
		   | IDENTIFIER LBRACK expression RBRACK EQUALS expression
		   | LET IDENTIFIER EQUALS expression
		   ;


control_flow_stmt : if_stmt
                   | while_stmt
                   | for_stmt
                   | return_stmt
                   | match_stmt
                   | unless_stmt
		   ;

exec_stmt         : EXEC DOUBLE_LT CHARSEQ_WITH_NEWLINE DOUBLE_GT
		   ;

rw_stmt           : READ FILEHANDLE INTO IDENTIFIER
		   | READ string_const INTO IDENTIFIER
		   | WRITE expression FILEHANDLE FILEHANDLE
		   | APPEND expression INTO FILEHANDLE
		   ;

close_stmt        : CLOSE FILEHANDLE
  		   ;
  
open_stmt         : OPEN string_const FOR rw_mode
		   | OPEN string_const FOR rw_mode AS FILEHANDLE
		   ;

rw_mode		  : READING
	           | WRITING
		   | APPENDING
		   | READ_AND_WRITE
		   ;

if_stmt           : IF LPAREN condition RPAREN DO block ELSE block END
		   ;

while_stmt        : WHILE LPAREN condition RPAREN DO block END
		   ;

for_stmt          : FOR expression IN expression DO block END
		   ;

return_stmt       : RETURN expression SEMICOLON
		   ;

match_stmt        : MATCH expression WITH match_case_list END
		   ;

match_case_list   : match_case
                  | match_case_list COMMA match_case
		   ;

match_case        : PIPE expression ARROW block COMMA
		   ;

unless_stmt       : UNLESS LPAREN condition RPAREN DO block END
		   ;

function_stmt     : DEFUN IDENTIFIER LPAREN identifier_list LPAREN block

identifier_list   : /* empty */
		   | IDENTIFIER
		   | identifier_list COMMA IDENTIFIER
		   ;

block             : LCURLY statement_list RCURLY
		   ;


expression       : compound_expr 
		  | UNDERLINE 
		  | regex_match 
		  | regex_nonmatch 
		  | ternary_expr
		  ;

compound_expr    : unary_expr
		  | LPAREN compound_expr RPAREN
                  | unary_expr OR compound_expr
                  | unary_expr AND compound_expr
		  | unary_expr BIT_AND compound_expr
		  | unary_expr BIT_OR compound_expr
		  | unary_expr BIT_XOR compound_expr
	          | unary_expr EQ  compound_expr
                  | unary_expr NEQ compound_expr
                  | unary_expr LT compound_expr
                  | unary_expr LTE compound_expr
                  | unary_expr GT compound_expr
                  | unary_expr GTE compound_expr
                  | unary_expr PLUS compound_expr
                  | unary_expr MINUS compound_expr
                  | unary_expr MULTIPLY compound_expr
                  | unary_expr DIVIDE compound_expr
                  | unary_expr MODULO compound_expr
		  | unary_expr POW compound_expr
		  | unary_expr PIPES_LEFT compound_expr
		  | unary_expr PIPES_RIGHT compound_expr
	  	  ;

unary_expr        : primary_expr
                   | U_PLUS unary_expr
                   | U_MINUS unary_expr
                   | NEG unary_expr
                   | BIT_NOT unary_expr
                   | DOLLAR unary_expr
		   ;

primary_expr      : IDENTIFIER
		   | IDENTIFIER LBRACK expression RBRACK
		   | const_value
                   | function_call
                   | lambda_expr
		   ;


function_call     : IDENTIFIER LPAREN argument_list RPAREN
		   ;

ternary_expr      : expression QMARK expression COLON expression
		   ;

lambda_expr       : LBRACK argument_list RBRACK ARROW block SEMICOLON
		   ;

argument_list     : expression
                   | argument_list COMMA expression
		   ;

regex_match      : expression REG_MATCHES regex_const
		  ;
regex_nonmatch   : expression REG_NOT_MATCHES regex_const
		  ;

const_value      : array | hashmap | number | string_const
		  ;

array            : LCURLY array_elements RCURLY
		  ;

array_elements    : expression
                  | array_elements COMMA expression
		   ;

hashmap          : LCURLY key_values RCURLY
		  ;

key_values        : expression COLON expression
                  | key_values COMMA expression COLON expression
		   ;

condition        : expression
		  ;


number           : integer
                  | float
		  ;

float            : FLOAT_NUM	
		  | SCI_NUM	
		  ;

integer          : DECIMAL_NUM			
		  | BIN_NUM_PREFIX BIN_NUM	
		  | HEX_NUM_PREFIX HEX_NUM	
		  | OCT_NUM_PREFIX OCT_NUM	
		  ;
                  
string_const     : mln_qstring
		  | mln_fstring
		  | qstring
		  | fstring
		  ;

regex_const      : regex_opener CHARSEQ_WITHOUT_NEWLINE regex_closer
		  ;

regex_closer     : SLASH
		  | GT
		  | LCURLY
		  | LPAREN
		  | PERCENT
		  | LBRACK
		  ;

regex_opener     : M_SLASH
		  | M_LT
		  | M_LCURLY
		  | M_LPAREN
		  | M_PERCENT
		  | M_LBRACK
		  ;

mln_fstring       : TRIPLE_QUOTE mln_fstring_body TRIPLE_QUOTE
		   ;

mln_fstring_body  : CHARSEQ_WITH_NEWLINE
		   | DOLLAR_LCURLY expression RCURLY
		   ;

mln_qstring       : quote_openers CHARSEQ_WITH_NEWLINE quote_closers
		   ;


quote_closers    : SLASH        
                  | GT		
                  | RPAREN      
                  | RCURLY	
                  | PERCENT	
                  | RBRACK	
		  ;

quote_openers    : Q_SLASH      
                  | Q_LT	
                  | Q_LPAREN    
                  | Q_LCURLY	
                  | Q_PERCENT   
                  | Q_LBRACK	
		  ;

fstring          : DQUOTE fstring_body DQUOTE
		  ;

fstring_body     : CHARSEQ_WITHOUT_NEWLINE
		  | DOLLAR_LCURLY expression RCURLY
		  ;

qstring         : SQUOTE CHARSEQ_WITHOUT_NEWLINE SQUOTE
                 ;
