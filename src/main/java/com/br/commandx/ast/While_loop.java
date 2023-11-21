package com.br.commandx.ast;

import java.util.List;
import java.util.Map;

public class While_loop implements ASTNode {
	private List<ASTNode> whileBody;
	private ASTNode logicalExpression;
	
	public While_loop(ASTNode logicalExpression, List<ASTNode> whileBody) {
		super();
		this.logicalExpression = logicalExpression;
		this.whileBody = whileBody;
	}
	
	@Override
	public Object execute(Map<String, Object> symbolTable) {
		while((boolean)logicalExpression.execute(symbolTable)) {
			for(ASTNode node : whileBody) {
				node.execute(symbolTable);
			}
		}
		return null;
	}

}
