parser grammar Java1_2ANTLRParser;

@header {
	package ca.ucalgary.cpsc499_02.w26;
}

options { tokenVocab=Java1_2ANTLRLexer; }

identifier              				: Identifier ;
qualifiedIdentifier     				: identifier (PERIOD identifier)* ;
literal                 				: IntegerLiteral 
									| FloatingPointLiteral 
									| CharacterLiteral 
									| StringLiteral 
									| BooleanLiteral 
									| NullLiteral
									;

type            						: qualifiedIdentifier dims? 
									| basicType
									;
basicType       						: BYTE 
									| SHORT 
									| CHAR 
									| INT 
									| LONG 
									| FLOAT 
									| DOUBLE 
									| BOOLEAN
									;
dims									: (OPEN_BRACKET CLOSE_BRACKET)+ ;

expression          					: expression1 (assignmentOperator expression1)? ;
assignmentOperator  					: EQUALS 
									| PLUS_EQUALS 
									| MINUS_EQUALS 
									| ASTERISK_EQUALS 
									| SLASH_EQUALS 
									| AMPERSAND_EQUALS 
									| PIPE_EQUALS 
									| CARET_EQUALS 
									| PERCENT_EQUALS 
									| DOUBLE_LESS_THAN_EQUALS 
									| DOUBLE_GREATER_THAN_EQUALS 
									| TRIPLE_GREATER_THAN_EQUALS
									;
expression1         					: expression2 expression1Rest? ;
expression1Rest     					: QUESTION expression COLON expression1 ;
expression2         					: expression3 expression2Rest ;
expression2Rest     					: (infixOp expression3)* (INSTANCEOF type)? ;
infixOp             					: DOUBLE_PIPE 
									| DOUBLE_AMPERSAND 
									| PIPE 
									| CARET 
									| AMPERSAND 
									| DOUBLE_EQUALS 
									| EXCLAMATION_EQUALS 
									| LESS_THAN 
									| GREATER_THAN 
									| LESS_THAN_OR_EQUALS 
									| GREATER_THAN_OR_EQUALS 
									| DOUBLE_LESS_THAN 
									| DOUBLE_GREATER_THAN 
									| TRIPLE_GREATER_THAN 
									| PLUS 
									| MINUS 
									| ASTERISK 
									| SLASH 
									| PERCENT
									;
expression3         					: prefixOp expression3 
									| OPEN_PARENTHESIS (expression | type) CLOSE_PARENTHESIS expression3 
									| primary selector* postfixOp*
									;
prefixOp            					: DOUBLE_PLUS 
									| DOUBLE_MINUS 
									| EXCLAMATION 
									| TILDE 
									| PLUS 
									| MINUS
									;
postfixOp           					: DOUBLE_PLUS 
									| DOUBLE_MINUS
									;
primary             					: parenthesizedExpression 
									| THIS arguments? 
									| SUPER superSuffix 
									| literal 
									| NEW creator 
									| qualifiedIdentifier identifierSuffix? 
									| basicType dims? PERIOD CLASS 
									| VOID PERIOD CLASS
									;
identifierSuffix    					: arguments 
									| PERIOD (CLASS | THIS | SUPER (superSuffix) | (NEW innerCreator))
									;
selector            					: PERIOD identifier arguments? 
									| PERIOD THIS 
									| PERIOD SUPER superSuffix 
									| PERIOD NEW innerCreator 
									| OPEN_BRACKET expression CLOSE_BRACKET
									;
superSuffix         					: arguments 
									| PERIOD identifier arguments?
									;
arguments           					: OPEN_PARENTHESIS (expression (COMMA expression)*)? CLOSE_PARENTHESIS ;
creator             					: qualifiedIdentifier (arrayCreatorRest | classCreatorRest) ;
innerCreator        					: identifier classCreatorRest ;
arrayCreatorRest    					: OPEN_BRACKET (CLOSE_BRACKET dims? arrayInitializer | expression CLOSE_BRACKET (OPEN_BRACKET expression CLOSE_BRACKET)* dims?) ;
classCreatorRest    					: arguments classBody? ;
arrayInitializer    					: OPEN_BRACE (variableInitializer (COMMA variableInitializer)*)? (COMMA)? CLOSE_BRACE ;
variableInitializer 					: arrayInitializer 
									| expression
									;

parenthesizedExpression				: OPEN_PARENTHESIS expression CLOSE_PARENTHESIS;
block                               	: OPEN_BRACE blockStatement* CLOSE_BRACE;
blockStatement                      	: localVariableDeclarationStatement 
									| classOrInterfaceDeclaration 
									| statement
									;
localVariableDeclarationStatement	: FINAL? type variableDeclarators SEMICOLON;
statement                           	: block 
                                    	| labeledStatement
									| ifStatement
									| forStatement
                                    	| whileStatement 
                                    	| doStatement 
                                    	| tryStatement
                                    	| switchStatement 
                                    	| synchronizedStatement 
                                    	| returnStatement
                                    	| throwStatement 
                                    	| breakStatement 
                                    	| continueStatement 
                                    	| emptyStatement
                                    	| expressionStatement
                                    	;
ifStatement							: IF parenthesizedExpression statement elseClause? ;
elseClause							: ELSE statement ;
forStatement							: FOR OPEN_PARENTHESIS forInit? SEMICOLON expression? SEMICOLON forUpdate? CLOSE_PARENTHESIS statement ;
whileStatement						: WHILE parenthesizedExpression statement ;
doStatement							: DO statement WHILE parenthesizedExpression SEMICOLON ;
tryStatement							: TRY block (catches | (catches? finallyClause)) ;
switchStatement						: SWITCH parenthesizedExpression OPEN_BRACE switchBlockStatementGroup* switchLabel* CLOSE_BRACE ;
synchronizedStatement				: SYNCHRONIZED parenthesizedExpression block ;
returnStatement						: RETURN expression? SEMICOLON ;
throwStatement						: THROW expression SEMICOLON ;
breakStatement						: BREAK identifier? SEMICOLON ;
continueStatement					: CONTINUE identifier? SEMICOLON ;
emptyStatement						: SEMICOLON ;
expressionStatement                 	: statementExpression SEMICOLON;
labeledStatement						: identifier COLON statement ;
statementExpression                 	: expression;
constantExpression                  	: expression;

catches                     			: catchClause+;
catchClause                 			: CATCH OPEN_PARENTHESIS formalParameter CLOSE_PARENTHESIS block;
finallyClause						: FINALLY block ;
switchBlockStatementGroup   			: switchLabel+ blockStatement+ ;
switchLabel                 			: CASE constantExpression COLON 
									| DEFAULT COLON
									;

forInit         						: statementExpression (COMMA statementExpression)* 
									| FINAL? type variableDeclarators
									;
forUpdate       						: statementExpression (COMMA statementExpression)* ;

modifier                    			: PUBLIC 
									| PROTECTED 
									| PRIVATE 
									| STATIC 
									| ABSTRACT 
									| FINAL 
									| NATIVE 
									| SYNCHRONIZED 
									| TRANSIENT 
									| VOLATILE 
									| STRICTFP
									;
variableDeclarators         			: variableDeclarator (COMMA variableDeclarator)* ;
variableDeclarator          			: identifier  dims? (EQUALS variableInitializer)? ;
variableDeclaratorId        			: identifier dims? ;
constantDeclarator          			: identifier dims? EQUALS variableInitializer;

compilationUnit                     	: (PACKAGE qualifiedIdentifier SEMICOLON)? importDeclaration* typeDeclaration* EOF ;
importDeclaration                   	: IMPORT identifier (PERIOD identifier)* (PERIOD ASTERISK)? SEMICOLON ;
typeDeclaration                     	: classOrInterfaceDeclaration ;
classOrInterfaceDeclaration         	: modifier* (classDeclaration | interfaceDeclaration) ;
classDeclaration                    	: CLASS identifier superclass? superinterfaces? classBody ;
superclass							: EXTENDS type ;
superinterfaces						: IMPLEMENTS typeList ;
interfaceDeclaration                	: INTERFACE identifier extendsInterfaces? interfaceBody ;
extendsInterfaces					: EXTENDS typeList ;
typeList                         	: type (COMMA type)* ;
classBody                           	: OPEN_BRACE classBodyDeclaration* CLOSE_BRACE ;
interfaceBody                       	: OPEN_BRACE interfaceBodyDeclaration* CLOSE_BRACE ;
classBodyDeclaration                	: emptyDeclaration 
									| initializer
									| staticInitializer
									| memberDeclaration ;
emptyDeclaration						: SEMICOLON ;
initializer							: block ;
staticInitializer					: STATIC block ;
memberDeclaration                   	: methodDeclaration 
									| fieldDeclaration 
									| constructorDeclaration
									| classOrInterfaceDeclaration
									;
methodDeclaration                   	: modifier* result identifier formalParameters dims? throws_? (block | SEMICOLON) ;
result								: type
									| VOID
									;
throws_								: THROWS qualifiedIdentifiers ;
fieldDeclaration						: modifier* type variableDeclarators SEMICOLON ;
constructorDeclaration              	: modifier* identifier formalParameters throws_? constructorBody ;
constructorBody						: OPEN_BRACE explicitConstructorInvocation? blockStatement* CLOSE_BRACE ;
explicitConstructorInvocation		: (THIS | SUPER) arguments SEMICOLON ;
interfaceBodyDeclaration            	: emptyDeclaration 
									| interfaceMemberDeclaration;
interfaceMemberDeclaration          	: interfaceMethodDeclaration 
									| constantDeclaration 
									| classOrInterfaceDeclaration
									;
interfaceMethodDeclaration          	: modifier* result identifier formalParameters dims? throws_? SEMICOLON ;
constantDeclaration					: modifier* type identifier variableDeclarators SEMICOLON ;
qualifiedIdentifiers             	: qualifiedIdentifier (COMMA qualifiedIdentifier)*;
formalParameters                    	: OPEN_PARENTHESIS (formalParameter (COMMA formalParameter)*)? CLOSE_PARENTHESIS;
formalParameter                     	: FINAL? type variableDeclaratorId;
