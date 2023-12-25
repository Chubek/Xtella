%token IDENTIFIER NUMBER STRING
%token IF ELSE WHILE FOR RETURN OPEN FILE DO END
%token PLUS MINUS MULTIPLY DIVIDE MODULO
%token EQ NEQ LT LTE GT GTE
%token AND OR NOT
%token LPAREN RPAREN LBRACE RBRACE SEMICOLON COMMA COLON
%token LET IMPORT FUNC TYPE

%%

program           : statement_list
		  ;

statement_list    : statement
                   | statement_list statement
		   ;

statement         : assign_stmt
                   | control_flow_stmt
                   | function_stmt
                   | file_stmt
		   ;

assign_stmt       : IDENTIFIER BECOMES expression
		  | IDENTIFIER EQUALS expression
		  | LET IDENTIFIER EQUALS expression
		  ;

control_flow_stmt : if_stmt
                   | while_stmt
                   | for_stmt
                   | return_stmt
                   | match_stmt
                   | unless_stmt
		   ;

rw_stmt           : READ FILEHANDLE INTO IDENTIFIER
		   | WRITE expression INTO FILEHANDLE
		   | APPEND expression INTO FILEHANDLE
		   | PRINT expression_list
		   | PRINTF string_const COMMA expression_list
		   | PRINT expression_list redirection
		   | PRINTF string_const COMMA expression_list redirection
		   ;

redirection       : GT expression
		   | DOUBLE_GT expression
		   | LT expression
		   | DOUBLE_LT expression
		   ;

expression_list   : expression
		   | expression COMMA expression_list
		   ;

open_file_stmt    : OPEN string_const FOR rw_mode
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

block             : LBRACE statement_list RBRACE
		  ;


expression       : compound_expr | UNDERLINE | regex_match | regex_nonmatch
		 ;

compound_expr    : primary_expr
		  | unary_expr
		  | LPAREN compound_expr RPAREN
                  | primary_expr OR compund_expr
                  | primary_expr AND compound_expr
		  | primary_expr BIT_AND compound_expr
		  | primary_expr BIT_OR compound_expr
		  | primary_expr BIT_XOR compound_expr
	          | primary_expr EQ  compound_expr
                  | primary_expr NEQ compound_expr
                  | primary_expr LT compound_expr
                  | primary_expr LTE compound_expr
                  | primary_expr GT compound_expr
                  | primary_expr GTE compound_expr
                  | primary_expr PLUS compound_expr
                  | primary_expr MINUS compound_expr
                  | primary_expr MULTIPLY compound_expr
                  | primary_expr DIVIDE compound_expr
                  | primary_expr MODULO compound_expr
		  | primary_expr POW compound_expr
	  	  ;

unary_expr        : primary_expr
                  | PLUS unary_expr
                  | MINUS unary_expr
                  | NOT unary_expr
		  | BIT_NOT unary_expr
                  | DOLLAR unary_expr
		  ;

primary_expr      : IDENTIFIER
                  | LPAREN expression RPAREN
		  | const_value
                  | function_call
                  | ternary_expr
                  | lambda_expr
                  | argument_back_ref
		  ;

function_call     : IDENTIFIER LPAREN argument_list RPAREN
		  ;

argument_list     : expression
                  | argument_list COMMA expression
		  ;

regex_match      : expression REG_MATCH regex_const
		 ;
regex_nonmath    : expression REG_MATCH regex_const
		 ;

const_value      : array | hashmap | exec_command | number | string_consst
		 ;

array            : LBRACE array_elements RBRACE
		 ;

array_elements    : expression
                  | array_elements COMMA expression
		  ;

hashmap          : LBRACE key_values RBRACE
		 ;

key_values        : expression COLON expression
                  | key_values COMMA expression COLON expression
		  ;

exec_command      : EXEC_PIPE expression SEMICOLON
		  ;

condition        : expression
		 ;


number           : INTEGER
                  | FLOATING_POINT
		  ;

float            : FLOAT_NUM	
		  | SCI_NUM	
		  ;

integer          : DECIMAL_NUM			
		 | BIN_NUM_PREFIX BIN_NUM	
		 | HEX_NUM_PREFIX HEX_NUM	
		 | OCT_NUM_PREFIX OCT_NUM	
		 ;
                  
string_const     : multi_ln_quoted_string
		  | multi_ln_strng
		  | quoted_string
		  | string
		  ;

regex_const      : regex_opener regex_body regex_closer
		 ;

regex_body       : /* empty */
		  | character
		  | regex_body character
		  ;

regex_closer     : SLASH
		  | GT
		  | LBRACE
		  | LPAREN
		  | PERCENT
		  | LSQUARE
		  ;

regex_opener     : M_SLASH
		  | M_LT
		  | M_LBRACE
		  | M_LPAREN
		  | M_PERCENT
		  | M_LSQUARE
		  ;

multi_ln_quoted_string : quote_openers multi_ln_quoted_str_body quote_closers
		       ;

multi_ln_quoted_str_body  : character		      
		 | NEWLINE			      
		 | quoted_str_body character
		 ;


quote_closers    : SLASH        
                  | GT		
                  | RPAREN      
                  | RBRACE	
                  | PERCENT	
                  | RSQUARE	
		  ;

quote_openers    : Q_SLASH      
                  | Q_LT	
                  | Q_LPAREN    
                  | Q_LBRACE	
                  | Q_PERCENT   
                  | Q_LSQUARE	
		  ;

multi_ln_string  : BACKTICK multi_ln_string_body BACKTICK	   
		 ;

multi_ln_string_body : character				   
		  | NEWLINE					   
                  | multi_ln_string_body character		   
                  | multi_ln_string_body DOLLAR expression RPAREN  
		  ;

quoted_string    : SQUOTE quoted_str_body SQUOTE	  
		 ;

quoted_str_body  : character				  
		 | quoted_str_body character		  
		 ;

string           : DQUOTE string_body DQUOTE		  
		 ;

string_body      : character				  
                  | string_body character		  
                  | string_body DOLLAR expression RPAREN  
		  ;

character        : LETTER				
                  | c_escapes				
		  ;

c_escapes        : SQUOTE				
                  | DQUOTE				
                  | QUESTION				
                  | BACKSLASH				
                  | LOWERCASE_A				
                  | LOWERCASE_B				
                  | LOWERCASE_F				
                  | LOWERCASE_N				
                  | LOWERCASE_R				
                  | LOWERCASE_T				
                  | LOWERCASE_V				
                  | uni_escape				
		  | hex_escape				
                  | oct_escape				
		  ;

uni_escape	 : BACKSLACK UPLUS HEX_NUM		
	    	 ;

hex_escape       : BACKSLASH LOWERCASE_X HEX_NUM	
		 ;

oct_escape       : BACKSLASH OCT_NUM			
		 ;


