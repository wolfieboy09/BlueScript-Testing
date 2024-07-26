package dev.bluescript.lang;

import dev.bluescript.lang.ast.*;
import dev.bluescript.lang.ast.statement.PrintStatement;
import dev.bluescript.lang.ast.statement.Statement;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Interpreter {
    private final Script script;
    private final Map<String, Object> variables = new HashMap<>();

    public Interpreter(Script script) {
        this.script = script;
    }

    public void interpret() {
        for (Statement statement : script.statements) {
            execute(statement);
        }
    }

    private void execute(Statement statement) {
        switch (statement) {
            case VarDecl varDecl -> executeVarDecl(varDecl);
            case Assignment assignment -> executeAssignment(assignment);
            case TryCatch tryCatch -> executeTryCatch(tryCatch);
            case ForLoop forLoop -> executeForLoop(forLoop);
            case MethodCall methodCall -> executeMethodCall(methodCall);
            case Block block -> executeBlock(block);
            case PrintStatement printStatement -> executePrintStmt(printStatement);
            case null, default -> {
                assert statement != null;
                throw new RuntimeException("Unknown statement type: " + statement.getClass());
            }
        }
    }

    private void executeVarDecl(@NotNull VarDecl varDecl) {
        String identifier = varDecl.identifier;
        Object value = evaluate(varDecl.expression);
        variables.put(identifier, value);
    }

    private void executeAssignment(@NotNull Assignment assignment) {
        String identifier = assignment.identifier;
        Object value = evaluate(assignment.expression);
        if (!variables.containsKey(identifier)) {
            throw new RuntimeException("Variable not declared: " + identifier);
        }
        variables.put(identifier, value);
    }

    private void executeTryCatch(TryCatch tryCatch) {
        try {
            executeBlock(tryCatch.tryBlock);
        } catch (Exception e) {
            if (!e.getClass().getSimpleName().equals(tryCatch.exception)) {
                throw e;
            }
            variables.put(tryCatch.alias, e.getMessage());
            executeBlock(tryCatch.catchBlock);
        }
    }

    private void executeForLoop(@NotNull ForLoop forLoop) {
        Object iterable = variables.get(forLoop.iterable);
        if (iterable instanceof Iterable) {
            for (Object item : (Iterable<?>) iterable) {
                variables.put(forLoop.iterator, item);
                executeBlock(forLoop.block);
            }
        } else {
            throw new RuntimeException(forLoop.iterable + " is not iterable");
        }
    }

    private void executeMethodCall(MethodCall methodCall) {
        // Handle method call
    }

    private void executeBlock(@NotNull Block block) {
        for (Statement statement : block.statements) {
            execute(statement);
        }
    }

    private void executePrintStmt(@NotNull PrintStatement printStatement) {
        for (Expression expression : printStatement.expressions) {
            Object value = evaluate(expression);
            System.out.print(value + " ");
        }
        System.out.println();
    }

    private Object evaluate(Expression expression) {
        switch (expression) {
            case StringLiteral stringLiteral -> {
                return stringLiteral.value;
            }
            case NumberLiteral numberLiteral -> {
                return numberLiteral.value;
            }
            case Identifier identifier1 -> {
                String identifier = identifier1.identifier;
                if (!variables.containsKey(identifier)) {
                    throw new RuntimeException("Variable not declared: " + identifier);
                }
                return variables.get(identifier);
            }
            case null, default -> {
                assert expression != null;
                throw new RuntimeException("Unknown expression type: " + expression.getClass());
            }
        }
    }
}
