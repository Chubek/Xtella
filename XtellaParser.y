%token IDENTIFIER NUMBER STRING
%token IF ELSE WHILE FOR RETURN OPEN FILE DO END
%token PLUS MINUS MULTIPLY DIVIDE MODULO
%token EQ NEQ LT LTE GT GTE
%token AND OR NOT
%token LPAREN RPAREN LBRACE RBRACE SEMICOLON COMMA COLON
%token LET IMPORT FUNC TYPE

%%

program           : statement_list

statement_list    : statement
                  | statement_list statement

statement         : assign_stmt
                  | control_flow_stmt
                  | function_stmt
                  | file_stmt

assign_stmt       : LET IDENTIFIER '=' expression SEMICOLON

control_flow_stmt : if_stmt
                  | while_stmt
                  | for_stmt
                  | return_stmt
                  | match_stmt
                  | unless_stmt

if_stmt           : IF LPAREN condition RPAREN DO block ELSE block END

while_stmt        : WHILE LPAREN condition RPAREN DO block END

for_stmt          : FOR expression IN expression DO block END

return_stmt       : RETURN expression SEMICOLON

match_stmt        : MATCH expression WITH match_case_list END

match_case_list   : match_case
                  | match_case_list COMMA match_case

match_case        : '|' expression ARROW block COMMA

unless_stmt       : UNLESS LPAREN condition RPAREN DO block END

block             : LBRACE statement_list RBRACE

expression        : logical_or_expr
                  | UNDERSCORE

logical_or_expr   : logical_and_expr
                  | logical_or_expr OR logical_and_expr

logical_and_expr  : equality_expr
                  | logical_and_expr AND equality_expr

equality_expr     : relational_expr
                  | equality_expr EQ relational_expr
                  | equality_expr NEQ relational_expr

relational_expr   : additive_expr
                  | relational_expr LT additive_expr
                  | relational_expr LTE additive_expr
                  | relational_expr GT additive_expr
                  | relational_expr GTE additive_expr

additive_expr     : multiplicative_expr
                  | additive_expr PLUS multiplicative_expr
                  | additive_expr MINUS multiplicative_expr

multiplicative_expr: unary_expr
                  | multiplicative_expr MULTIPLY unary_expr
                  | multiplicative_expr DIVIDE unary_expr
                  | multiplicative_expr MODULO unary_expr

unary_expr        : primary_expr
                  | PLUS unary_expr
                  | MINUS unary_expr
                  | NOT unary_expr
                  | DOLLAR unary_expr

primary_expr      : IDENTIFIER
                  | NUMBER
                  | STRING
                  | LPAREN expression RPAREN
                  | function_call
                  | array
                  | hashmap
                  | regex_match
                  | exec_command
                  | ternary_expr
                  | lambda_expr
                  | argument_back_ref

function_call     : IDENTIFIER LPAREN argument_list RPAREN

argument_list     : expression
                  | argument_list COMMA expression

array            : LBRACE array_elements RBRACE

array_elements    : expression
                  | array_elements COMMA expression

hashmap          : LBRACE key_values RBRACE

key_values        : expression COLON expression
                  | key_values COMMA expression COLON expression

regex_match       : expression TILDE expression

exec_command      : EXEC_PIPE expression SEMICOLON

condition        : expression

identifier       : LETTER
                  | LETTER identifier_tail

identifier_tail  : LETTER
                  | DIGIT
                  | UNDERSCORE
                  | identifier_tail LETTER
                  | identifier_tail DIGIT
                  | identifier_tail UNDERSCORE

number           : INTEGER
                  | FLOATING_POINT

float            : FLOAT_NUM	{ $$ = $1; }
		  | SCI_NUM	{ $$ = $1; }

integer          : DECIMAL_NUM			{ $$ = $1; }
		 | BIN_NUM_PREFIX BIN_NUM	{ $$ = $2; }
		 | HEX_NUM_PREFIX HEX_NUM	{ $$ = $2; }
		 | OCT_NUM_PREFIX OCT_NUM	{ $$ = $2; }
                  


multi_ln_quoted_string : quote_openers multi_ln_quoted_str_body quote_closers 
		       {
		          if ($1 != $3) { // handle error }
			  $$ = $2;
		       }

multi_ln_quoted_str_body  : character		      { $$ = $1.toString(); }
		 | NEWLINE			      { $$ = "\n";	    }
		 | quoted_str_body character	      { $$ = $1 + $2;	    }


quote_closers    : SLASH        { $$ = QUOTE_DELIM_SLASH;   }
                  | GT		{ $$ = QUOTE_DELIM_ANGLE;   }
                  | RPAREN      { $$ = QUOTE_DELIM_PAREN;   }
                  | RBRACE	{ $$ = QUOTE_DELIM_BRACE;   }
                  | PERCENT	{ $$ = QUOTE_DELIM_PERCENT; }
                  | RSQUARE	{ $$ = QUOTE_DELIM_SQUARE;  }

quote_openers    : Q_SLASH      { $$ = QUOTE_DELIM_SLASH;   }
                  | Q_LT	{ $$ = QUOTE_DELIM_ANGLE;   }
                  | Q_LPAREN    { $$ = QUOTE_DELIM_PAREN;   }
                  | Q_LBRACE	{ $$ = QUOTE_DELIM_BRACE;   }
                  | Q_PERCENT   { $$ = QUOTE_DELIM_PERCENT; }
                  | Q_LSQUARE	{ $$ = QUOTE_DELIM_SQUARE;  }

multi_ln_string  : BACKTICK multi_ln_string_body BACKTICK	   { $$ = $2; }

multi_ln_string_body : character				   { $$ = $1.toString(); }
		  | NEWLINE					   { $$ = "\n";		 }
                  | multi_ln_string_body character		   { $$ = $1 + $2;       }
                  | multi_ln_string_body DOLLAR expression RPAREN  { $$ = $3.toString(); }

quoted_string    : SQUOTE quoted_str_body SQUOTE	  { $$ = $2; }

quoted_str_body  : character				  { $$ = $1.toString(); }
		 | quoted_str_body character		  { $$ = $1 + $2;	}

string           : DQUOTE string_body DQUOTE		  { $$ = $2; }

string_body      : character				  { $$ = $1.toString(); }
                  | string_body character		  { $$ = $1 + $2;	}
                  | string_body DOLLAR expression RPAREN  { $$ = $3.toString(); }

character        : LETTER				{ $$ = $1; }
                  | c_escapes				{ $$ = $1; }

c_escapes        : SQUOTE				{ $$ = '\'';}
                  | DQUOTE				{ $$ = '\"';}
                  | QUESTION				{ $$ = '\?';}
                  | BACKSLASH				{ $$ = '\\';}
                  | LOWERCASE_A				{ $$ = '\a';}
                  | LOWERCASE_B				{ $$ = '\b';}
                  | LOWERCASE_F				{ $$ = '\f';}
                  | LOWERCASE_N				{ $$ = '\n';}
                  | LOWERCASE_R				{ $$ = '\r';}
                  | LOWERCASE_T				{ $$ = '\t';}
                  | LOWERCASE_V				{ $$ = '\v';}
                  | uni_escape				{ $$ = $1; }
		  | hex_escape				{ $$ = $1; }
                  | oct_escape				{ $$ = $1; }

uni_escape	 : BACKSLACK UPLUS HEX_NUM		{ $$ = $3; }

hex_escape       : BACKSLASH LOWERCASE_X HEX_NUM	{ $$ = $3; }

oct_escape       : BACKSLASH OCT_NUM			{ $$ = $2; }


