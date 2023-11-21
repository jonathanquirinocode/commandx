grammar CommandX;

@parser::header{ 
	import java.util.Map;
	import java.util.HashMap;
	import java.util.List;
	import java.util.ArrayList;
	import com.br.commandx.ast.*;
}

@parser::members {
	Map<String, Object> symbolTable = new HashMap<String, Object>();
}

start: PROGRAM ID BRACKET_OPEN
		{
			List<ASTNode> body = new ArrayList<ASTNode>();
			Map<String, Object> symbolTable = new HashMap<String, Object>();
		}
		(sentence {body.add($sentence.node);})*
		BRACKET_CLOSE
		{
			for(ASTNode n : body) {
				n.execute(symbolTable);
			}
		};

sentence returns [ASTNode node]: 
	  println {$node = $println.node;} 
	| conditional {$node = $conditional.node;}
	| while_loop {$node = $while_loop.node;}
	| var_decl {$node = $var_decl.node;}
	| var_assign {$node = $var_assign.node;}
	| read_statement {$node = $read_statement.node;}
	| function_declaration {$node = $function_declaration.node;}
	| procedure_declaration {$node = $procedure_declaration.node;}
	| function_call {$node = $function_call.node;}
	| procedure_call {$node = $procedure_call.node;}
	| comment
	;

comment: LINE_COMMENT | BLOCK_COMMENT;

println returns [ASTNode node]: PRINTLN logicalExpression SEMICOL
	{$node = new Println($logicalExpression.node);};

read_statement returns [ASTNode node]: READ ID SEMICOL
	{$node = new Read($ID.text);};

conditional returns [ASTNode node]: IF PAR_OPEN logicalExpression PAR_CLOSE
	{
		List<ASTNode> body = new ArrayList<ASTNode>();
	}
	BRACKET_OPEN (s1 = sentence {body.add($s1.node);})* BRACKET_CLOSE
	ELSE
	{
		List<ASTNode> elseBody = new ArrayList<ASTNode>();
	}
	BRACKET_OPEN (s2 = sentence {elseBody.add($s2.node);})* BRACKET_CLOSE
	{
		$node = new If($logicalExpression.node, body, elseBody);
	};

while_loop returns [ASTNode node]: WHILE PAR_OPEN logicalExpression PAR_CLOSE
	{
		List<ASTNode> body = new ArrayList<ASTNode>();
	}
	BRACKET_OPEN (s1 = sentence {body.add($s1.node);})* BRACKET_CLOSE
	{
		$node = new While_loop($logicalExpression.node, body);
	};

logicalExpression returns [ASTNode node]:
	  logicalOrExpression {$node = $logicalOrExpression.node;}
	| logicalAndExpression {$node = $logicalAndExpression.node;}
	| NOT logicalExpression {$node = new LogicalNot($logicalExpression.node);}
	;

logicalOrExpression returns [ASTNode node]: 
    logicalAndExpression {$node = $logicalAndExpression.node;}
    (OR right=logicalAndExpression {$node = new LogicalOr($node, $right.node);})*
    ;

logicalAndExpression returns [ASTNode node]: 
    equalityExpression {$node = $equalityExpression.node;}
    (AND right=equalityExpression {$node = new LogicalAnd($node, $right.node);})*
    ;

equalityExpression returns [ASTNode node]: 
    relationalExpression {$node = $relationalExpression.node;}
    (EQUALITY_OPERATOR right=relationalExpression {$node = new EqualityExpression($node, $right.node, $EQUALITY_OPERATOR.text);})*
    ;

relationalExpression returns [ASTNode node]: 
    additiveExpression {$node = $additiveExpression.node;}
    (RELATIONAL_OPERATOR right=additiveExpression {$node = new RelationalExpression($node, $right.node, $RELATIONAL_OPERATOR.text);})*
    ;

additiveExpression returns [ASTNode node]: 
    multiplicativeExpression {$node = $multiplicativeExpression.node;}
    (ADDITIVE_OPERATOR right=multiplicativeExpression {$node = new AdditiveExpression($node, $right.node, $ADDITIVE_OPERATOR.text);})*
    ;

multiplicativeExpression returns [ASTNode node]: 
    unaryExpression {$node = $unaryExpression.node;}
    (MULTIPLICATIVE_OPERATOR right=unaryExpression {$node = new MultiplicativeExpression($node, $right.node, $MULTIPLICATIVE_OPERATOR.text);})*
    ;
  
unaryExpression returns [ASTNode node]: 
    ADDITIVE_OPERATOR operand=unaryExpression {$node = new UnaryExpression($ADDITIVE_OPERATOR.text, $operand.node);}
    | primaryExpression {$node = $primaryExpression.node;}
    ;

primaryExpression returns [ASTNode node]: 
    logicalNotExpression {$node = $logicalNotExpression.node;}
    | INTEGER_LITERAL {$node = new Constant(Integer.parseInt($INTEGER_LITERAL.text));}
    | BOOLEAN_LITERAL {$node = new Constant(Boolean.parseBoolean($BOOLEAN_LITERAL.text));}
    | CHAR_LITERAL {$node = new Constant($CHAR_LITERAL.text.charAt(1));}
    | STRING_LITERAL {$node = new Constant($STRING_LITERAL.text.substring(1, $STRING_LITERAL.text.length() - 1));}
    | FLOAT_LITERAL {$node = new Constant(Float.parseFloat($FLOAT_LITERAL.text));}
    | ID {$node = new VarRef($ID.text);}
    | PAR_OPEN expr=logicalExpression PAR_CLOSE {$node = $expr.node;}
    ;

logicalNotExpression returns [ASTNode node]: 
    NOT operand=primaryExpression {$node = new LogicalNot($operand.node);}
    ;


var_decl returns [ASTNode node]: VAR ID typeDeclaration SEMICOL {$node = new VarDecl($ID.text, $typeDeclaration.text);};

var_assign returns [ASTNode node]: ID ASSIGN logicalExpression SEMICOL {$node = new VarAssign($ID.text, $logicalExpression.node);};

function_declaration returns [ASTNode node]: FUNC ID PAR_OPEN parameterList PAR_CLOSE BRACKET_OPEN
		{
			List<ASTNode> body = new ArrayList<ASTNode>();
			Map<String, Object> localSymbolTable = new HashMap<String, Object>();
		}
		(s = sentence {body.add($s.node);})* BRACKET_CLOSE
		{$node = new FunctionDeclaration($ID.text, body, localSymbolTable);};

procedure_declaration returns [ASTNode node]: PROC ID PAR_OPEN parameterList PAR_CLOSE BRACKET_OPEN
		{
			List<ASTNode> body = new ArrayList<ASTNode>();
			Map<String, Object> localSymbolTable = new HashMap<String, Object>();
		}
		(s = sentence {body.add($s.node);})* BRACKET_CLOSE
		{$node = new ProcedureDeclaration($ID.text, body, localSymbolTable);};

function_call returns [ASTNode node]: ID PAR_OPEN argumentList PAR_CLOSE SEMICOL
		{$node = new FunctionCall($ID.text, $argumentList.list);};

procedure_call returns [ASTNode node]: ID PAR_OPEN argumentList PAR_CLOSE SEMICOL
		{$node = new ProcedureCall($ID.text, $argumentList.list);};

argumentList returns [List<ASTNode> list]: 
		{List<ASTNode> args = new ArrayList<ASTNode>();}
		(e = logicalExpression {args.add($e.node);} (COMMA e = logicalExpression {args.add($e.node);})*)
		{$list = args;};

parameterList returns [List<Parameter> list]: 
		{List<Parameter> params = new ArrayList<Parameter>();}
		(p = parameter {params.add($p.param);} (COMMA p = parameter {params.add($p.param);})*)
		{$list = params;};

parameter returns [Parameter param]: 
		typeDeclaration ID {$param = new Parameter($typeDeclaration.text, $ID.text);};

typeDeclaration: 'int' | 'str' | 'char' | 'float' | 'boolean';

PROGRAM: 'program';
VAR: 'var';
PRINTLN: 'println';
READ: 'read';
FUNC: 'func';
PROC: 'proc';

IF: 'if';
ELSE: 'else';
WHILE: 'while';

ADDITIVE_OPERATOR: '+' | '-';
MULTIPLICATIVE_OPERATOR: '*' | '/';

AND: '&&';
OR: '||';
NOT: '!';


RELATIONAL_OPERATOR: '>' | '<' | '>=' | '<=';
EQUALITY_OPERATOR: '==' | '!=';

ASSIGN: '=';

PAR_OPEN: '(';
PAR_CLOSE: ')';

BRACKET_OPEN: '{';
BRACKET_CLOSE: '}';

SEMICOL: ';';
COMMA: ',';

INTEGER_LITERAL: [0-9]+;
BOOLEAN_LITERAL: 'true' | 'false';
CHAR_LITERAL: '\'' ~["'\r\n'] '\'';
STRING_LITERAL: '"' ~["\r\n"]* '"';
FLOAT_LITERAL: [0-9]+ '.' [0-9]+;

ID: [a-zA-Z_][a-zA-Z_0-9]*;

LINE_COMMENT: '//' ~[\r\n]* '\r'? '\n' -> skip;
BLOCK_COMMENT: '/*' .*? '*/' -> skip;
WS: [ \t\r\n]+ -> skip;
