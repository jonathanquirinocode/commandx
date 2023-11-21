package com.br.commandx.ast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionCall implements ASTNode {
    private String functionName;
    private List<ASTNode> arguments;

    public FunctionCall(String functionName, List<ASTNode> arguments) {
        this.functionName = functionName;
        this.arguments = arguments;
    }

    @Override
    public Object execute(Map<String, Object> symbolTable) {
        // Obtém a função da tabela de símbolos global
        FunctionDeclaration function = (FunctionDeclaration) symbolTable.get(functionName);

        // Verifica se a função existe
        if (function == null) {
            throw new RuntimeException("Função '" + functionName + "' não declarada.");
        }

        // Verifica se o número de argumentos coincide com o número de parâmetros
        List<ASTNode> parameters = function.getBody();
        if (arguments.size() != parameters.size()) {
            throw new RuntimeException("Número incorreto de argumentos para a função '" + functionName + "'.");
        }

        // Cria um novo escopo local para a execução da função
        Map<String, Object> localSymbolTable = new HashMap<>(function.getLocalSymbolTable());

        // Associa os argumentos aos parâmetros no escopo local
        for (int i = 0; i < parameters.size(); i++) {
            ASTNode parameter = parameters.get(i);
            Object argValue = arguments.get(i).execute(symbolTable);
            if (parameter instanceof VarDecl) {
                VarDecl varDecl = (VarDecl) parameter;
                localSymbolTable.put(varDecl.getName(), argValue);
            } else {
                throw new RuntimeException("Parâmetro inválido na declaração da função '" + functionName + "'.");
            }
        }

        // Executa o corpo da função no novo escopo local
        for (ASTNode node : function.getBody()) {
            node.execute(localSymbolTable);
        }

        return localSymbolTable.get(FunctionDeclaration.RETURN_VARIABLE_NAME);
    }
}
