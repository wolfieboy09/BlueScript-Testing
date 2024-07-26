package dev.bluescript.lang.ast;

public class NumberLiteral extends Expression {
    public final double value;

    public NumberLiteral(double value) {
        this.value = value;
    }
}
