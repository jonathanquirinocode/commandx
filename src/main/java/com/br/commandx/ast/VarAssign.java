package com.br.commandx.ast;

import java.util.Map;

public class VarAssign implements ASTNode {

	private String name;
    private ASTNode value;

    public VarAssign(String name, ASTNode value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public Object execute(Map<String, Object> symbolTable) {
        // Verifica se a variável está na tabela de símbolos
        if (!symbolTable.containsKey(name)) {
            throw new RuntimeException("Variable '" + name + "' not declared.");
        }

        // Avalia o valor da expressão à direita e atribui à variável
        Object result = value.execute(symbolTable);
        symbolTable.put(name, result);

        return result;
    }

    public String getName() {
        return name;
    }

    public ASTNode getValue() {
        return value;
    }
}
