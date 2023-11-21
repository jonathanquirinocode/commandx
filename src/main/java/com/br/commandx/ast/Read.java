package com.br.commandx.ast;

import java.util.Map;
import java.util.Scanner;

public class Read implements ASTNode {

	private String variableName;

    public Read(String variableName) {
        super();
    	this.variableName = variableName;
    }

    @Override
    public Object execute(Map<String, Object> symbolTable) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter a value for variable '" + variableName + "': ");
        String userInput = scanner.nextLine();
        
        if (symbolTable.containsKey(variableName)) {
            
            Object existingValue = symbolTable.get(variableName);
            if (existingValue instanceof Integer) {
                symbolTable.put(variableName, Integer.parseInt(userInput));
            } else if (existingValue instanceof Boolean) {
                symbolTable.put(variableName, Boolean.parseBoolean(userInput));
            } else if (existingValue instanceof Float) {
                symbolTable.put(variableName, Float.parseFloat(userInput));
            } else {
                
                symbolTable.put(variableName, userInput);
            }
        } else {
            
            symbolTable.put(variableName, userInput);
        }
        
        
        scanner.close();

        return null;
    }

}
