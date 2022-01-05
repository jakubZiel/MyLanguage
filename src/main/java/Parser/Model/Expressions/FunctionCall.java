package Parser.Model.Expressions;

import ExceptionHandler.Exceptions.InterpreterException;
import Interpreter.Visitor;

public class FunctionCall extends Expression {
    private String identifier;
    private Arguments arguments;

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
