package dev.bluescript.lang.ast;

public class Identifier extends Expression {
    public final String identifier;

    public Identifier(String identifier) {
        this.identifier = identifier;
    }
}