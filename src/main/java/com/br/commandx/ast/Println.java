package com.br.commandx.ast;

import java.util.Map;

public class Println implements ASTNode {
	private ASTNode logicalExpression;

    public Println(ASTNode logicalExpression) {
        super();
    	this.logicalExpression = logicalExpression;
    }

    @Override
    public Object execute(Map<String, Object> symbolTable) {
    	System.out.println(logicalExpression.execute(symbolTable));
    	return null;
    }
}


