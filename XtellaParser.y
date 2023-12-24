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

integer          : DIGIT
                  | INTEGER DIGIT

floating_point    : DIGIT DOT DIGIT
                  | DIGIT DOT DIGIT FLOATING_POINT_SUFFIX

hex_num          : HEX_PREFIX HEX_DIGIT
                 | HEX_PREFIX HEX_DIGIT hex_num_suffix

hex_num_suffix   : HEX_DIGIT
                  | hex_num_suffix HEX_DIGIT

oct_num          : OCT_PREFIX OCT_DIGIT
                  | OCT_PREFIX OCT_DIGIT oct_num_suffix

oct_num_suffix   : OCT_DIGIT
                  | oct_num_suffix OCT_DIGIT

bin_num          : BIN_PREFIX BIN_DIGIT
                  | BIN_PREFIX BIN_DIGIT bin_num_suffix

bin_num_suffix   : BIN_DIGIT
                  | bin_num_suffix BIN_DIGIT

multi_ln_quoted_string : quote_openers character quote_closers

quote_closers    : SLASH
                  | GT
                  | RPAREN
                  | RBRACE
                  | PERCENT
                  | RSQUARE

quote_openers    : Q_SLASH
                  | Q_LT
                  | Q_LPAREN
                  | Q_LBRACE
                  | Q_PERCENT
                  | Q_LSQUARE

multi_ln_string  : BACKTICK multi_ln_string_body BACKTICK

multi_ln_string_body : character
                  | multi_ln_string_body character
                  | multi_ln_string_body DOLLAR expression RPAREN

quoted_string    : SQUOTE character SQUOTE

string           : DQUOTE string_body DQUOTE

string_body      : character
                  | string_body character
                  | string_body DOLLAR expression RPAREN

character        : LETTER
                  | c_escapes

letter           : LOWERCASE
                  | UPPERCASE

digit            : DIGIT

hex_digit        : HEX_DIGIT

oct_digit        : OCT_DIGIT

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
                  | DIGIT
                  | hex_escape
                  | oct_escape

hex_escape       : BACKSLASH LOWERCASE_X hex_digit

oct_escape       : BACKSLASH oct_digit

