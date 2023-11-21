
package com.br.commandx;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;

public class Main {

	private static final String EXTENSION = "x";

	public static void main(String[] args) throws IOException {
		String program = args.length > 1 ? args[1] : "test/test." + EXTENSION;

		System.out.println("Interpreting file " + program);

		SimpleLexer lexer = new SimpleLexer(new ANTLRFileStream(program));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		SimpleParser parser = new SimpleParser(tokens);

		SimpleParser.StartContext tree = parser.start();

		CommandxCustomVisitor visitor = new CommandxCustomVisitor();
		visitor.visit(tree);

		System.out.println("Interpretation finished");

	}

}
