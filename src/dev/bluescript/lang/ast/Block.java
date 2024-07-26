package dev.bluescript.lang.ast;

import dev.bluescript.lang.ast.statement.Statement;

import java.util.List;

public class Block extends Statement {
    public final List<Statement> statements;

    public Block(List<Statement> statements) {
        this.statements = statements;
    }
}
