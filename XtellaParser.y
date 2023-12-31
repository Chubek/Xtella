%{
  import java.io.*;
  import java.util.*;
%}


%token <String> IDENTIFIER_HASHMAP IDENTIFIER_SCALAR IDENTIFIER_ARRAY IDENTIFIER_FUNCTION IDENTIFIER_VARIANT IO_IDENTIFIER IDENTIFIER
%token <String> IDENTIFIER_INDEXED END_INDEX
%token <long> BIN_NUM HEX_NUM OCT_NUM DECIMAL_NUM
%token <double> FLOAT_NUM
%token <String> CHARSEQ_WITH_NEWLINE CHARSEQ_WITHOUT_NEWLINE

%token LET IO_IDENTIFIER
%token EQUALS BECOMES
%token PERCENT DOLLAR SQUOTE DQUOTE BACKTICK 
%token PIPE SLASH NEWLINE LETTER DOLLAR_LCURLY TRIPLE_QUOTE DOUBLE_COLON
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
%token BIN_NUM_PREFIX HEX_NUM_PREFIX OCT_NUM_PREFIX
%token REGEX_MATCH REGEX_NOT_MATCH
%token COMMA
%token QMARK COLON
%token Q_LT Q_LPAREN Q_LCURLY Q_PERCENT Q_LBRACK
%token M_LT M_LCURLY M_LPAREN M_PERCENT M_LBRACK

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

%type <AbsynNode> program statement_list statement assign_stmt control_flow_stmt exec_stmt rw_stmt close_stmt open_stmt rw_mode
%type <AbsynNode> if_stmt while_stmt for_stmt return_stmt match_stmt unless_stmt function_stmt identifier_list block expression
%type <AbsynNode> compound_expr unary_expr primary_expr indexed_ident non_indexed_ident function_call ternary_expr lambda_expr
%type <AbsynNode> argument_list regex_match regex_nonmatch const_value array array_elements hashmap key_values condition
%type <AbsynNode> number float integer string_const regex_const regex_closer regex_opener mln_qstring quote_closers
%type <AbsynNode> quote_openers fstring fstring_body qstring


%start program


%%

program           : statement_list                    { $$ = new ProgramNode($1); }

statement_list    : statement                        { $$ = new StatementListNode(Collections.singletonList($1)); }
                   | statement_list statement        { $1.add($2); $$ = $1; }

statement         : assign_stmt                      { $$ = $1; }
                   | control_flow_stmt               { $$ = $1; }
                   | function_stmt                    { $$ = $1; }
                   | rw_stmt                         { $$ = $1; }
                   | exec_stmt                       { $$ = $1; }
                  4 | open_stmt                       { $$ = $1; }
                   | close_stmt                      { $$ = $1; }

assign_stmt       : non_indexed_ident BECOMES expression
                    { $$ = new AssignStmtNode($1, $3); }
                  | indexed_ident BECOMES expression
                    { $$ = new AssignStmtNode($1, $3); }

control_flow_stmt : if_stmt                          { $$ = $1; }
                   | while_stmt                       { $$ = $1; }
                   | for_stmt                        { $$ = $1; }
                   | return_stmt                     { $$ = $1; }
                   | match_stmt                      { $$ = $1; }
                   | unless_stmt                     { $$ = $1; }

exec_stmt         : EXEC DOUBLE_LT CHARSEQ_WITH_NEWLINE DOUBLE_GT
                    { $$ = new ExecStmtNode($3); }

rw_stmt           : READ IO_IDENTIFIER INTO IDENTIFIER
                    { 
		    	$$ = new RWStmtNode(RWMode.READING, 
					new IdentifierNode($2),
					new IdentifierNode($4)); 
		    }
                  | READ string_const INTO IO_IDENTIFIER
                    { 
		        $$ = new RWStmtNode(RWMode.READING, 
					$2,
					new IdentifierNode($4)); 
		    }
                  | WRITE expression INTO IO_IDENTIFIER
                    { 
		        $$ = new RWStmtNode(RWMode.WRITING, 
					$2, 
					new IdentifierNode($4)); 
		    }
                  | APPEND expression INTO IO_IDENTIFIER
                    { 
		        $$ = new RWStmtNode(RWMode.APPENDING, 
					$2,
					new IdentifierNode($4)); 
		    }

close_stmt        : CLOSE IO_IDENTIFIER
                    { $$ = new CloseStmtNode($2); }

open_stmt         : OPEN string_const FOR rw_mode
                    { $$ = new OpenStmtNode($2, $4, null); }
                  | OPEN string_const FOR rw_mode AS IO_IDENTIFIER
                    { $$ = new OpenStmtNode($2, $4, $6); }

rw_mode           : READING                          { $$ = RWMode.READING; }
                  | WRITING                          { $$ = RWMode.WRITING; }
                  | APPENDING                        { $$ = RWMode.APPENDING; }
                  | READ_AND_WRITE                   { $$ = RWMode.READ_AND_WRITE; }

if_stmt           : IF LPAREN condition RPAREN DO block ELSE block END
                    { $$ = new IfStmtNode($3, $6, $8); }

while_stmt        : WHILE LPAREN condition RPAREN DO block END
                    { $$ = new WhileStmtNode($3, $6); }

for_stmt          : FOR expression IN expression DO block END
                    { $$ = new ForStmtNode($3, $5, $7); }

return_stmt       : RETURN { $$ = new ReturnStmtNode(null); }
		   | RETURN expression SEMICOLON
                    { $$ = new ReturnStmtNode($2); }

match_stmt        : MATCH expression WITH match_case_list END
                    { $$ = new MatchStmtNode($2, $4); }

match_case_list   : match_case                       { $$ = AbsynListNode($1); }
                  | match_case_list COMMA match_case { $1.addToList($3); $$ = $1; }

match_case        : PIPE expression ARROW block COMMA
                    { $$ = new MatchCaseNode($2, $4); }

unless_stmt       : UNLESS LPAREN condition RPAREN DO block END
                    { $$ = new UnlessStmtNode($3, $6); }

function_stmt     : DEFUN IDENTIFIER LPAREN identifier_list LPAREN block
                    { $$ = new FunctionStmtNode($2, $4, $6); }

identifier_list   : /* empty */                       { $$ = Collections.emptyList(); }
                  | IDENTIFIER                       { $$ = Collections.singletonList($1); }
                  | identifier_list COMMA IDENTIFIER { $1.add($3); $$ = $1; }

block             : LCRLY statement_list RCURLY
                    { $$ = new BlockNode($2); }

expression        : compound_expr                    { $$ = $1; }
                  | UNDERLINE                        { $$ = new PlaceholderNode(); }
                  | regex_match                      { $$ = $1; }
                  | regex_nonmatch                   { $$ = $1; }
                  | ternary_expr                     { $$ = $1; }

compound_expr     : unary_expr                       { $$ = $1; }
                  | LPAREN compound_expr RPAREN      { $$ = $2; }
                  | unary_expr OR compound_expr      { $$ = new CompoundExprNode($1, $3, Operator.OR); }
                  | unary_expr AND compound_expr     { $$ = new CompoundExprNode($1, $3, Operator.AND); }
                  | unary_expr BIT_AND compound_expr { $$ = new CompoundExprNode($1, $3, Operator.BIT_AND); }
                  | unary_expr BIT_OR compound_expr  { $$ = new CompoundExprNode($1, $3, Operator.BIT_OR); }
                  | unary_expr BIT_XOR compound_expr { $$ = new CompoundExprNode($1, $3, Operator.BIT_XOR); }
                  | unary_expr EQ compound_expr       { $$ = new CompoundExprNode($1, $3, Operator.EQ); }
                  | unary_expr NEQ compound_expr      { $$ = new CompoundExprNode($1, $3, Operator.NEQ); }
                  | unary_expr LT compound_expr       { $$ = new CompoundExprNode($1, $3, Operator.LT); }
                  | unary_expr LTE compound_expr      { $$ = new CompoundExprNode($1, $3, Operator.LTE); }
                  | unary_expr GT compound_expr       { $$ = new CompoundExprNode($1, $3, Operator.GT); }
                  | unary_expr GTE compound_expr      { $$ = new CompoundExprNode($1, $3, Operator.GTE); }
                  | unary_expr PLUS compound_expr     { $$ = new CompoundExprNode($1, $3, Operator.PLUS); }
                  | unary_expr MINUS compound_expr    { $$ = new CompoundExprNode($1, $3, Operator.MINUS); }
                  | unary_expr MULTIPLY compound_expr { $$ = new CompoundExprNode($1, $3, Operator.MULTIPLY); }
                  | unary_expr DIVIDE compound_expr   { $$ = new CompoundExprNode($1, $3, Operator.DIVIDE); }
                  | unary_expr MODULO compound_expr   { $$ = new CompoundExprNode($1, $3, Operator.MODULO); }
                  | unary_expr POW compound_expr      { $$ = new CompoundExprNode($1, $3, Operator.POW); }
                  | unary_expr PIPES_LEFT compound_expr { $$ = new CompoundExprNode($1, $3, Operator.PIPES_LEFT); }
                  | unary_expr PIPES_RIGHT compound_expr { $$ = new CompoundExprNode($1, $3, Operator.PIPES_RIGHT); }

unary_expr        : primary_expr                     { $$ = $1; }
                  | U_PLUS unary_expr                { $$ = new UnaryExprNode($2, Operator.U_PLUS); }
                  | U_MINUS unary_expr               { $$ = new UnaryExprNode($2, Operator.U_MINUS); }
                  | NEG unary_expr                   { $$ = new UnaryExprNode($2, Operator.NEG); }
                  | BIT_NOT unary_expr               { $$ = new UnaryExprNode($2, Operator.BIT_NOT); }
                  | DOLLAR unary_expr                { $$ = new UnaryExprNode($2, Operator.DOLLAR); }

primary_expr      : const_value                       { $$ = $1; }
                  | function_call                    { $$ = $1; }
                  | lambda_expr                      { $$ = $1; }
                  | indexed_ident                    { $$ = $1; }
                  | non_indexed_ident                { $$ = $1; }

indexed_ident     : IDENTIFIER_INDEXED const_value END_INDEX
                    { $$ = new IndexedIdentifierNode($1, $2); }

non_indexed_ident : IDENTIFIER_FUNCTION              { $$ = new IdentifierNode($1); }
                  | IDENTIFIER_HASHMAP               { $$ = new IdentifierNode($1); }
                  | IDENTIFIER_ARRAY                 { $$ = new IdentifierNode($1); }
                  | IDENTIFIER_SCALAR                { $$ = new IdentifierNode($1); }
                  | IDENTIFIER_VARIANT               { $$ = new IdentifierNode($1); }

function_call     : IDENTIFIER LPAREN argument_list RPAREN
                    { $$ = new FunctionCallNode($1, $3); }

ternary_expr      : expression QMARK expression COLON expression
                    { $$ = new TernaryExprNode($1, $3, $5); }

lambda_expr       : LBRACK argument_list RBRACK ARROW block SEMICOLON
                    { $$ = new LambdaExprNode($2, $5); }

argument_list     : expression                       { $$ = new AbsynListNode($1); }
                  | argument_list COMMA expression  { $1.addToList($3); $$ = $1; }


regex_match      : expression REG_MATCHES regex_const { $$ = new RegexExprNode($1, $3, RegexExprNode.RegexMatchType.MATCHES); }
		 ;

regex_nonmatch   : expression REG_NOT_MATCHES regex_const { $$ = RegexExprNode($1, $4, RegexExprNode.RegexMatchType.NOT_MATCHES); }
		 ;

const_value      : array { $$ = $1; }
                 | hashmap { $$ = $1; }
                 | number { $$ = $1; }
                 | string_const { $$ = $1; }
		 ;

array            : LCURLY array_elements RCURLY { $$ = ArrayNode($2); }
		 ;

array_elements    : expression { $$ = [$1]; }
                  | array_elements COMMA expression { $$ = $1 + [$3]; }
		  ;

hashmap          : LCURLY key_values RCURLY { $$ = new HashMapNode($2); }
		 ;

key_values        : expression COLON expression { $$ = { $1: $3 }; }
                  | key_values COMMA expression COLON expression { $$ = $1; $1[$3] = $5; }
		  ;

condition        : expression { $$ = new ConditionNode($1); }
		 ;

number           : integer { $$ = $1; }
                  | float { $$ = $1; }
		 ;

float            : FLOAT_NUM  { $$ = new NumberNode($1, NumberNode.NumberType.FLOAT); }
		 | SCI_NUM  { $$ = new NumberNode($1, NumberNode.NumberType.SCIENTIFIC); }
		 ;

integer          : DECIMAL_NUM  { $$ = new NumberNode($1, NumberNode.NumberType.INTEGER); }
		 | BIN_NUM_PREFIX BIN_NUM  { $$ = new NumberNode($2, NumberNode.NumberType.INTEGER); }
		 | HEX_NUM_PREFIX HEX_NUM  { $$ = new NumberNode($2, NumberNode.NumberType.INTEGER); }
		 | OCT_NUM_PREFIX OCT_NUM  { $$ = new NumberNode($2, NumberNode.NumberType.INTEGER); }
		 ;
                  
string_const     : mln_qstring { $$ = $1; }
		 | mln_fstring { $$ = $1; }
		 | qstring { $$ = $1; }
		 | fstring { $$ = $1; }
		 ;


regex_const      : regex_opener CHARSEQ_WITHOUT_NEWLINE regex_closer { $$ = new RegexConstNode($2); }
		 ;

regex_closer     : SLASH { $$ = '/'; }
		 | GT { $$ = '>'; }
		 | LCURLY { $$ = '{'; }
		 | LPAREN { $$ = '('; }
		 | PERCENT { $$ = '%'; }
		 | LBRACK { $$ = '['; }
		 ;

regex_opener     : M_SLASH { $$ = '/m'; }
		 | M_LT { $$ = '/m'; }
		 | M_LCURLY { $$ = '/m'; }
		 | M_LPAREN { $$ = '/m'; }
		 | M_PERCENT { $$ = '/m'; }
		 | M_LBRACK { $$ = '/m'; }
		 ;

mln_fstring       : TRIPLE_QUOTE mln_fstring_body TRIPLE_QUOTE  { $$ = new FStringConstNode($2); }
		  ;

quote_closers    : SLASH        				{ $$ = Delimiter.SLASH;   }
                  | GT						{ $$ = Delimiter.ANGLE;   }
                  | RPAREN      				{ $$ = Delimiter.PAREN;   }
                  | RCURLY					{ $$ = Delimiter.CURLY;   }
                  | PERCENT					{ $$ = Delimiter.PERCENT; }
                  | RBRACK					{ $$ = Delimiter.BRACK;   }
		 ;

quote_openers    : Q_SLASH      				{ $$ = Delimiter.SLASH;   }
                  | Q_LT					{ $$ = Delimiter.ANGLE;   }
                  | Q_LPAREN    				{ $$ = Delimiter.PAREN;   }
                  | Q_LCURLY					{ $$ = Delimiter.CURLY;   }
                  | Q_PERCENT   				{ $$ = Delimiter.PERCENT; }
                  | Q_LBRACK					{ $$ = Delimiter.BRACK;   }	
		 ;

fstring          : DQUOTE fstring_body DQUOTE			{ $$ = new FStringConstNode($2); }
		 ;

mln_fstring_body    : fstring_single { $$ = new AbsynListNode($1); }
		 | fstring_body fstring_single  { $1.addToList($2); $$ = $1; } 
		 ;

mln_fstring_single  : CHARSEQ_WITH_NEWLINE			{ $$ = $2; }
	             | DOLLAR_LCURLY expression RCURLY		{ $$ = $2; }


fstring_body    : fstring_single { $$ = new AbsynListNode($1); }
		 | fstring_body fstring_single  { $1.addToList($2); $$ = $1; } 
		 ;

fstring_single  : CHARSEQ_WITHOUT_NEWLINE			{ $$ = $2; }
		 | DOLLAR_LCURLY expression RCURLY		{ $$ = $2; }
		 ;

qstring         : SQUOTE CHARSEQ_WITHOUT_NEWLINE SQUOTE		{ $$ = $2; }
                 ;
%%


  private Yylex lexer;


  private int yylex () {
    int yyl_return = -1;
    try {
      yylval = new ParserVal(0);
      yyl_return = lexer.yylex();
    }
    catch (IOException e) {
      System.err.println("IO error :"+e);
    }
    return yyl_return;
  }


  public void yyerror (String error) {
    System.err.println ("Error: " + error);
  }


  public Parser(Reader r) {
    lexer = new Yylex(r, this);
  }


  static boolean interactive;

  public static void main(String args[]) throws IOException {
    System.out.println("BYACC/Java with JFlex Calculator Demo");

    Parser yyparser;
    if ( args.length > 0 ) {
      // parse a file
      yyparser = new Parser(new FileReader(args[0]));
    }
    else {
      // interactive mode
      System.out.println("[Quit with CTRL-D]");
      System.out.print("Expression: ");
      interactive = true;
	    yyparser = new Parser(new InputStreamReader(System.in));
    }

    yyparser.yyparse();
    
    if (interactive) {
      System.out.println();
      System.out.println("Have a nice day");
    }
  }

