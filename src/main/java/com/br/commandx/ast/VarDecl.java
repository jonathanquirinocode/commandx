package com.br.commandx.ast;

import java.util.Map;

public class VarDecl implements ASTNode {

	 private String name;
	    private String type;

	    public VarDecl(String name, String type) {
	        this.name = name;
	        this.type = type;
	    }

	    @Override
	    public Object execute(Map<String, Object> symbolTable) {
	        // Verifica se a variável já está na tabela de símbolos
	        if (symbolTable.containsKey(name)) {
	            throw new RuntimeException("Variable '" + name + "' already declared.");
	        }

	        // Adiciona a variável à tabela de símbolos com um valor inicial nulo
	        symbolTable.put(name, null);

	        return null;
	    }

	    public String getName() {
	        return name;
	    }

	    public String getType() {
	        return type;
	    }

}
