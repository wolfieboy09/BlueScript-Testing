package dev.bluescript.lang.ast;

import dev.bluescript.lang.ast.statement.Statement;

public class ForLoop extends Statement {
    public final String iterator;
    public final String iterable;
    public final Block block;

    public ForLoop(String iterator, String iterable, Block block) {
        this.iterator = iterator;
        this.iterable = iterable;
        this.block = block;
    }
}
