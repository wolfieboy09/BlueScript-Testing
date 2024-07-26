package dev.bluescript.lang.ast;

import dev.bluescript.lang.ast.statement.Statement;

import java.util.List;

public class MethodCall extends Statement {
    public final String identifier;
    public final List<Expression> arguments;

    public MethodCall(String identifier, List<Expression> arguments) {
        this.identifier = identifier;
        this.arguments = arguments;
    }
}
