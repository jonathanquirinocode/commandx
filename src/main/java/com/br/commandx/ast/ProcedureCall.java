package com.br.commandx.ast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcedureCall implements ASTNode {
    private String procedureName;
    private List<ASTNode> argumentList;

    public ProcedureCall(String procedureName, List<ASTNode> argumentList) {
        this.procedureName = procedureName;
        this.argumentList = argumentList;
    }

    @SuppressWarnings("unused")
	@Override
    public Object execute(Map<String, Object> symbolTable) {
        // Recupera o procedimento da tabela de símbolos
        ProcedureDeclaration procedureDeclaration = (ProcedureDeclaration) symbolTable.get(procedureName);
        
     // Verifica se o procedimento existe
        if (procedureDeclaration == null) {
            throw new RuntimeException("Procedimento '" + procedureName + "' não declarado.");
        }
        
     // Verifica se o número de argumentos coincide com o número de parâmetros
        List<ASTNode> parameters = procedureDeclaration.getBody();
        if (argumentList.size() != parameters.size()) {
            throw new RuntimeException("Número incorreto de argumentos para o procedimento '" + procedureName + "'.");
        }
        
        if (procedureDeclaration != null) {
        	// Cria um novo escopo local para a execução da função
            Map<String, Object> localSymbolTable = new HashMap<>(procedureDeclaration.getLocalSymbolTable());

            // Associa os argumentos aos parâmetros no escopo local
            for (int i = 0; i < parameters.size(); i++) {
                ASTNode parameter = parameters.get(i);
                Object argValue = argumentList.get(i).execute(symbolTable);
                if (parameter instanceof VarDecl) {
                    VarDecl varDecl = (VarDecl) parameter;
                    localSymbolTable.put(varDecl.getName(), argValue);
                } else {
                    throw new RuntimeException("Parâmetro inválido na declaração do procedimento '" + procedureName + "'.");
                }
            }

            // Executa o corpo do procedimento
            for (ASTNode node : procedureDeclaration.getBody()) {
                node.execute(localSymbolTable);
            }

            // Retorna null, já que o procedimento não retorna valor
            return null;
        } else {
            throw new RuntimeException("Procedimento não encontrado: " + procedureName);
        }
    }
}
