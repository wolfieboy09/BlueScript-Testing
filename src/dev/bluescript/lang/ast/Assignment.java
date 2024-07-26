package dev.bluescript.lang.ast;

import dev.bluescript.lang.ast.statement.Statement;

public class Assignment extends Statement {
    public final String identifier;
    public final Expression expression;

    public Assignment(String identifier, Expression expression) {
        this.identifier = identifier;
        this.expression = expression;
    }
}
