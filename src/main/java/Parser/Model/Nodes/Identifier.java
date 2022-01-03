package Parser.Model.Nodes;

import ExceptionHandler.Exceptions.InterpreterException;
import Interpreter.Scope;
import Interpreter.Variable;
import Lexer.Token;
import Parser.Model.Expressions.Expression;
import Parser.Model.Expressions.Literal;

public class Identifier extends Expression {
    private String name;

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

    @Override
    public <T> Literal<T> execute(Scope scope) throws InterpreterException {
        var variable = (Variable) scope.getVariable(name);
        var value = (Literal<T>) variable.getValue();
        return value;
    }
}
