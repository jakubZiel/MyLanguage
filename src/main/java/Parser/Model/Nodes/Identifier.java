package Parser.Model.Nodes;

import ExceptionHandling.Exceptions.InterpreterException;
import Interpreter.Visitor;
import Lexer.Token;
import Parser.Model.Expressions.Expression;
import Parser.Model.Expressions.Literal;

public class Identifier extends Expression {
    private final String name;

    public Identifier(String name) {
        this.name = name;
    }

    public Identifier(Token token) {
        this.name = (String) token.getValue();
    }

    @Override
    public String toString() {
        return "Identifier{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    @Override
    public <T> Literal<T> accept(Visitor visitor) throws InterpreterException {
        return visitor.visit(this);
    }
}
