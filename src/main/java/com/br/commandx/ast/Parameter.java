package com.br.commandx.ast;

import java.util.Map;

public class Parameter implements ASTNode {

	private String type;
    private String name;

    public Parameter(String type, String name) {
        super();
    	this.type = type;
        this.name = name;
    }

    @Override
    public Object execute(Map<String, Object> symbolTable) {
        // Parâmetros não precisam de execução específica neste exemplo
        return null;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

}
