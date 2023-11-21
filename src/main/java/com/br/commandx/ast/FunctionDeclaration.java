package com.br.commandx.ast;

import java.util.List;
import java.util.Map;

public class FunctionDeclaration implements ASTNode {
	public static final String RETURN_VARIABLE_NAME = "__return";
	
	private String functionName;
    private List<ASTNode> body;
    private Map<String, Object> localSymbolTable;

    public FunctionDeclaration(String functionName, List<ASTNode> body, Map<String, Object> localSymbolTable) {
        this.functionName = functionName;
        this.body = body;
        this.localSymbolTable = localSymbolTable;
    }

    @Override
    public Object execute(Map<String, Object> symbolTable) {
        symbolTable.put(functionName, this); // Adiciona a função à tabela de símbolos

        // Executa o corpo da função
        for (ASTNode node : body) {
            node.execute(localSymbolTable);
        }

        return localSymbolTable.getOrDefault(RETURN_VARIABLE_NAME, null);
    }
    public List<ASTNode> getBody() {
        return body;
    }

    public Map<String, Object> getLocalSymbolTable() {
        return localSymbolTable;
    }

}
