package com.br.commandx.ast;

import java.util.Map;

public class LogicalNot implements ASTNode {
	
	private ASTNode operand;

    public LogicalNot(ASTNode operand) {
        super();
    	this.operand = operand;
    }

    @Override
    public Object execute(Map<String, Object> symbolTable) {
        Object result = operand.execute(symbolTable);

        // Garante que o resultado é do tipo booleano
        if (result instanceof Boolean) {
            // Realiza a negação lógica
            return !(Boolean) result;
        } else {
            throw new RuntimeException("LogicalNot operation can only be applied to boolean values.");
        }
    }
}










