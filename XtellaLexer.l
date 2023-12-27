%%

%byaccj

%{
  private Parser yyparser;

  public Yylex(java.io.Reader r, Parser yyparser) {
    this(r);
    this.yyparser = yyparser;
  }
%}

Name = [a-zA-Z_][a-zA-Z_0-9]*
Decimal = [0-9]+
Float = [0-9]+"."[0-9]+([eE][-+]?[0-9]+)?

%%

"=="                        
"!="                        
"<"                         
"<="                        
">"                         
">="                        
"&&"                        
"||"                        
"!"                         
"{"                         
"}"                         
"("                         
")"                         
";"                         
"["                         
"]"                         
","                         
":="                        
"if"                        
"else"                      
"end"                       
"for"                       
"do"                        
"while"                     
"return"                    
"match"                     
"with"                      
"unless"                    
"defun"                     
"read"                      
"into"                      
"write"                     
"append"                    
"close"                     
"open"                      
"as"                        
"<<"                        
">>"                        
"=~"                        
"!=~"                       
"/*" .* "*/" |
	"//" .* [\n]	     /* Ignore comments */ 
[ \t\n]+                    /* Ignore whitespace and newline */ 
.                           /* Handle unrecognized characters */ 
