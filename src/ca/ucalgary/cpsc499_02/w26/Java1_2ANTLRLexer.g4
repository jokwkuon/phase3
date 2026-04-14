lexer grammar Java1_2ANTLRLexer;

@header {
	package ca.ucalgary.cpsc499_02.w26;
}

ABSTRACT						: 'abstract';
BOOLEAN						: 'boolean';
BREAK						: 'break';
BYTE							: 'byte';
CASE							: 'case';
CATCH						: 'catch';
CHAR							: 'char';
CLASS						: 'class';
CONTINUE						: 'continue';
DEFAULT						: 'default';
DO							: 'do';
DOUBLE						: 'double';
ELSE							: 'else';
EXTENDS						: 'extends';
FINAL						: 'final';
FINALLY						: 'finally';
FLOAT						: 'float';
FOR							: 'for';
IF							: 'if';
IMPLEMENTS					: 'implements';
IMPORT						: 'import';
INSTANCEOF					: 'instanceof';
INT							: 'int';
INTERFACE					: 'interface';
LONG							: 'long';
NATIVE						: 'native';
NEW							: 'new';
PACKAGE						: 'package';
PRIVATE						: 'private';
PROTECTED					: 'protected';
PUBLIC						: 'public';
RETURN						: 'return';
SHORT						: 'short';
STATIC						: 'static';
STRICTFP						: 'strictfp';
SUPER						: 'super';
SWITCH						: 'switch';
SYNCHRONIZED					: 'synchronized';
THIS							: 'this';
THROW						: 'throw';
THROWS						: 'throws';
TRANSIENT					: 'transient';
TRY							: 'try';
VOID							: 'void';
VOLATILE						: 'volatile';
WHILE						: 'while';

SEMICOLON					: ';';
COMMA						: ',';
PERIOD						: '.';
OPEN_PARENTHESIS				: '(';
CLOSE_PARENTHESIS			: ')';
OPEN_BRACE					: '{';
CLOSE_BRACE					: '}';
OPEN_BRACKET					: '[';
CLOSE_BRACKET				: ']';
COLON           				: ':';
QUESTION        				: '?';
EQUALS          				: '=';
PLUS            				: '+';
MINUS           				: '-';
ASTERISK        				: '*';
SLASH           				: '/';
PERCENT         				: '%';
DOUBLE_PLUS     				: '++';
DOUBLE_MINUS    				: '--';
EXCLAMATION     				: '!';
TILDE           				: '~';
DOUBLE_EQUALS   				: '==';
EXCLAMATION_EQUALS			: '!=';
LESS_THAN       				: '<';
GREATER_THAN    				: '>';
LESS_THAN_OR_EQUALS			: '<=';
GREATER_THAN_OR_EQUALS		: '>=';
DOUBLE_AMPERSAND				: '&&';
DOUBLE_PIPE					: '||';
AMPERSAND					: '&';
PIPE							: '|';
CARET						: '^';
DOUBLE_LESS_THAN				: '<<';
DOUBLE_GREATER_THAN			: '>>';
TRIPLE_GREATER_THAN			: '>>>';
PLUS_EQUALS     				: '+=';
MINUS_EQUALS    				: '-=';
ASTERISK_EQUALS 				: '*=';
SLASH_EQUALS    				: '/=';
AMPERSAND_EQUALS 			: '&=';
PIPE_EQUALS     				: '|=';
CARET_EQUALS    				: '^=';
PERCENT_EQUALS  				: '%=';
DOUBLE_LESS_THAN_EQUALS		: '<<=';
DOUBLE_GREATER_THAN_EQUALS	: '>>=';
TRIPLE_GREATER_THAN_EQUALS	: '>>>=';

Identifier 					: JavaLetter JavaLetterOrDigit*;

fragment JavaLetter 			: [\p{Lu}\p{Ll}\p{Lt}\p{Lo}] // Letters
    							| [\p{Nl}]                   // Letter numbers
    							| [\p{Sc}]                   // Currency symbols
    							| [\p{Pc}]                   // Punctuation
    							;

fragment JavaLetterOrDigit 	: JavaLetter
			    				| [\p{Nd}]                   // Decimals
    							| [\p{Mn}\p{Mc}]             // Marks
    							| [\p{Cf}]                   // Format
    							;

IntegerLiteral          		: [0-9]+;
FloatingPointLiteral    		: [0-9]+ '.' [0-9]*;
CharacterLiteral        		: '\'' (~['\\] | '\\' .) '\'';
StringLiteral           		: '"' (~["\\] | '\\' .)* '"';
BooleanLiteral         	 	: 'true' | 'false';
NullLiteral             		: 'null';

WHITESPACE         			: [ \t\r\n\f]+ -> skip;
LINE_COMMENT    				: '//' ~[\r\n]* -> skip;
COMMENT         				: '/*' .*? '*/' -> skip;
