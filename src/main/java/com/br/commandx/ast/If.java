package com.br.commandx.ast;

import java.util.List;
import java.util.Map;


public class If implements ASTNode {
	 private ASTNode logicalExpression;
	    private List<ASTNode> ifBody;
	    private List<ASTNode> elseBody;

	    public If(ASTNode logicalExpression, List<ASTNode> ifBody, List<ASTNode> elseBody) {
	        super();
	    	this.logicalExpression = logicalExpression;
	        this.ifBody = ifBody;
	        this.elseBody = elseBody;
	    }
	
	@Override
	public Object execute(Map<String, Object> symbolTable) {
		if((boolean)logicalExpression.execute(symbolTable))
		{
			for (ASTNode node : ifBody) {
	            node.execute(symbolTable);
	        }
		}
		else {
			for (ASTNode node : elseBody) {
	            node.execute(symbolTable);
	        }
		}
		return null;
	}

}
