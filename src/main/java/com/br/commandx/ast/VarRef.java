package com.br.commandx.ast;

import java.util.Map;

public class VarRef implements ASTNode {

	private String varName;

    public VarRef(String varName) {
        this.varName = varName;
    }

    @Override
    public Object execute(Map<String, Object> symbolTable) {
        if (symbolTable.containsKey(varName)) {
            return symbolTable.get(varName);
        } else {
            throw new RuntimeException("Variável não encontrada: " + varName);
        }
    }
}
