package dev.bluescript.lang;

import dev.bluescript.lang.ast.*;
import dev.bluescript.lang.ast.statement.PrintStatement;
import dev.bluescript.lang.ast.statement.Statement;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int position;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.position = 0;
    }

    private Token currentToken() {
        return tokens.get(position);
    }

    private void advance() {
        position++;
    }

    private void expect(TokenType type) {
        if (currentToken().type == type) {
            advance();
        } else {
            throw new RuntimeException("Expected token: " + type + " but got: " + currentToken().type);
        }
    }

    public Script parse() {
        return script();
    }

    private Script script() {
        List<Statement> statements = new ArrayList<>();
        while (currentToken().type != TokenType.EOF) {
            statements.add(statement());
        }
        return new Script(statements);
    }

    private Statement statement() {
        return switch (currentToken().type) {
            case VAR -> varDecl();
            case IDENTIFIER -> assignmentOrMethodCall();
            case TRY -> tryCatch();
            case FOR -> forLoop();
            case PRINT -> printStmt();
            default -> throw new RuntimeException("Unexpected token: " + currentToken().type);
        };
    }

    private VarDecl varDecl() {
        expect(TokenType.VAR);
        String identifier = currentToken().text;
        expect(TokenType.IDENTIFIER);
        expect(TokenType.ASSIGN);
        Expression expression = expression();
        expect(TokenType.SEMICOLON);
        return new VarDecl(identifier, expression);
    }

    private Statement assignmentOrMethodCall() {
        String identifier = currentToken().text;
        expect(TokenType.IDENTIFIER);
        if (currentToken().type == TokenType.ASSIGN) {
            advance();
            Expression expression = expression();
            expect(TokenType.SEMICOLON);
            return new Assignment(identifier, expression);
        } else if (currentToken().type == TokenType.LPAREN) {
            return methodCall(identifier);
        } else {
            throw new RuntimeException("Unexpected token: " + currentToken().type);
        }
    }

    private TryCatch tryCatch() {
        expect(TokenType.TRY);
        Block tryBlock = block();
        expect(TokenType.EXCEPT);
        String exception = currentToken().text;
        expect(TokenType.IDENTIFIER);
        expect(TokenType.AS);
        String alias = currentToken().text;
        expect(TokenType.IDENTIFIER);
        Block catchBlock = block();
        return new TryCatch(tryBlock, exception, alias, catchBlock);
    }

    private ForLoop forLoop() {
        expect(TokenType.FOR);
        expect(TokenType.LPAREN);
        String iterator = currentToken().text;
        expect(TokenType.IDENTIFIER);
        expect(TokenType.IN);
        String iterable = currentToken().text;
        expect(TokenType.IDENTIFIER);
        expect(TokenType.RPAREN);
        Block block = block();
        return new ForLoop(iterator, iterable, block);
    }

    private MethodCall methodCall(String identifier) {
        List<Expression> arguments = new ArrayList<>();
        expect(TokenType.LPAREN);
        if (currentToken().type != TokenType.RPAREN) {
            arguments.add(expression());
            while (currentToken().type == TokenType.COMMA) {
                advance();
                arguments.add(expression());
            }
        }
        expect(TokenType.RPAREN);
        expect(TokenType.SEMICOLON);
        return new MethodCall(identifier, arguments);
    }

    private Block block() {
        List<Statement> statements = new ArrayList<>();
        expect(TokenType.LBRACE);
        while (currentToken().type != TokenType.RBRACE) {
            statements.add(statement());
        }
        expect(TokenType.RBRACE);
        return new Block(statements);
    }

    private Expression expression() {
        if (currentToken().type == TokenType.STRING_LITERAL) {
            String value = currentToken().text;
            advance();
            return new StringLiteral(value);
        } else if (currentToken().type == TokenType.NUMBER_LITERAL) {
            String value = currentToken().text;
            advance();
            return new NumberLiteral(Double.parseDouble(value));
        } else if (currentToken().type == TokenType.IDENTIFIER) {
            String identifier = currentToken().text;
            advance();
            return new Identifier(identifier);
        } else {
            throw new RuntimeException("Unexpected token: " + currentToken().type);
        }
    }

    private PrintStatement printStmt() {
        expect(TokenType.PRINT);
        expect(TokenType.LPAREN);
        List<Expression> expressions = new ArrayList<>();
        if (currentToken().type != TokenType.RPAREN) {
            expressions.add(expression());
            while (currentToken().type == TokenType.COMMA) {
                advance();
                expressions.add(expression());
            }
        }
        expect(TokenType.RPAREN);
        expect(TokenType.SEMICOLON);
        return new PrintStatement(expressions);
    }
}
