package Parser.Model.Expressions;

import ExceptionHandling.Exceptions.InterpreterException;
import Interpreter.Visitor;

public class FunctionCall extends Expression {
    private final String identifier;
    private final Arguments arguments;

    public FunctionCall(String identifier, Arguments arguments) {
        this.identifier = identifier;
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "FunctionCall{" +
                "identifier='" + identifier + '\'' +
                ", arguments=" + arguments +
                '}';
    }

    public String getIdentifier() {
        return identifier;
    }

    public Arguments getArguments(){
        return arguments;
    }

    @Override
    public <T> Literal<T> accept(Visitor visitor) throws InterpreterException {
        return visitor.visit(this);
    }
}
