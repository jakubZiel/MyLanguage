package Interpreter;

import Lexer.TokenType;
import Parser.Model.Expressions.Literal;

public class Variable {
    Literal value;
    TokenType declaredType;
    private final String identifier;

    public Variable(String identifier, Literal value) {
        this.value = value;
        this.identifier = identifier;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Literal value) {
        this.value = value;
    }

    public String getIdentifier() {
        return identifier;
    }
}
