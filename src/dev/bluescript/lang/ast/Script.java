package dev.bluescript.lang.ast;
import dev.bluescript.lang.ast.statement.Statement;

import java.util.List;

public class Script {
    public final List<Statement> statements;

    public Script(List<Statement> statements) {
        this.statements = statements;
    }
}
