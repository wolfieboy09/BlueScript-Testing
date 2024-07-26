package dev.bluescript.lang.ast;

import dev.bluescript.lang.ast.statement.Statement;

public class TryCatch extends Statement {
    public final Block tryBlock;
    public final String exception;
    public final String alias;
    public final Block catchBlock;

    public TryCatch(Block tryBlock, String exception, String alias, Block catchBlock) {
        this.tryBlock = tryBlock;
        this.exception = exception;
        this.alias = alias;
        this.catchBlock = catchBlock;
    }
}
