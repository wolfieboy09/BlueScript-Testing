package dev.bluescript.lang.ast.statement;

import dev.bluescript.lang.ast.Expression;

import java.util.List;

public class PrintStatement extends Statement {
    public final List<Expression> expressions;

    public PrintStatement(List<Expression> expressions) {
        this.expressions = expressions;
    }
}
