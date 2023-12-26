%token IDENTIFIER LET PRINT READ WRITE APPEND DOUBLE_GT LT DOUBLE_LT OPEN WRITING APPENDING READ_AND_WRITE LPAREN PLUS MINUS NOT BIT_NOT DOLLAR SCI_NUM BIN_NUM_PREFIX HEX_NUM_PREFIX OCT_NUM_PREFIX GT LBRACE PERCENT LSQUARE M_LT M_LBRACE M_LPAREN M_PERCENT M_LSQUARE NEWLINE RPAREN RBRACE RSQUARE Q_LT Q_LPAREN Q_LBRACE Q_PERCENT Q_LSQUARE DQUOTE QUESTION BACKSLASH LOWERCASE_A LOWERCASE_B LOWERCASE_F LOWERCASE_N LOWERCASE_R LOWERCASE_T LOWERCASE_V DQUOTE SQUOTE BACKTICK Q_SLASH M_SLASH SLASH OCT_NUM HEX_NUM BIN_NUM REG_MATCHES REG_NOT_MATCHES COLON MUTIPLY GTE LTE BIT_XOR BIT_AND UNDERLINE ARROW WITH SEMICOLON ELSE DO READING AS FOR FILEHANDLE EQUALS BECOMES QMARK POW MODULO MULTIPLY DIVIDE NEQ EQ BIT_XOR BIT_AND BIT_OR AND OR UNLESS PIPE COMMA REG_MATCHES REG_NOT_MATCHES RETURN IN WHILE END CLOSE IF INTO EXEC LBRACK RBRACK DEFUN FLOAT_NUM M_LBRACK Q_LBRACK Q_LBRACE LETTER BACKSLASH U_PLUS LOWERCASE_X BACKSLASH_U_PLUS BACKSLASH_X DECIMAL_NUM



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
		   | io_stmt
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

exec_stmt         : EXEC DOUBLE_LT string_body DOUBLE_GT
		   ;

io_stmt           : PRINT argument_list
		   | PRINT argument_list redirection
		   ;

rw_stmt           : READ FILEHANDLE INTO IDENTIFIER
		   | READ string_const INTO IDENTIFIER
		   | WRITE expression FILEHANDLE FILEHANDLE
		   | APPEND expression INTO FILEHANDLE
		   ;

redirection       : GT expression
		   | DOUBLE_GT expression
		   | LT expression
		   | DOUBLE_LT expression
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

block             : LBRACE statement_list RBRACE
		   ;


expression       : compound_expr | UNDERLINE | regex_match | regex_nonmatch
		  ;

compound_expr    : primary_expr
		  | unary_expr
		  | LPAREN compound_expr RPAREN
                  | primary_expr OR compound_expr
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
		   | IDENTIFIER LBRACK expression RBRACK
		   | const_value
                   | function_call
                   | ternary_expr
                   | lambda_expr
                   | argument_back_ref
		   ;

argument_back_ref : DOLLAR DECIMAL_NUM

function_call     : IDENTIFIER LPAREN argument_list RPAREN
		   ;

ternary_expr      : expression QMARK expression COLON expression
		   ;

lambda_expr       : LPAREN argument_list RPAREN ARROW LBRACE statement_list RBRACE
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
                  
string_const     : multi_ln_quoted_string
		  | multi_ln_string
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
		  | LBRACK
		  ;

regex_opener     : M_SLASH
		  | M_LT
		  | M_LBRACE
		  | M_LPAREN
		  | M_PERCENT
		  | M_LBRACK
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
                  | Q_LBRACK	
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

uni_escape	 : BACKSLASH_U_PLUS HEX_NUM		
	    	  ;

hex_escape       : BACKSLASH_LOWERCASE_X HEX_NUM	
		  ;

oct_escape       : BACKSLASH OCT_NUM			
		  ;


