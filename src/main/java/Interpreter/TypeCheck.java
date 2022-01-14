package Interpreter;

import ExceptionHandling.Exceptions.InterpreterException;
import Lexer.TokenType;
import Parser.Model.Expressions.Arguments;

import Parser.Model.Expressions.Literal;
import Parser.Model.Nodes.Parameters;

public class TypeCheck {
    public static void check(Parameters parameters, Arguments arguments, ExecuteVisitor visitor) throws InterpreterException {
        if (parameters.getSignatures().size() != arguments.arguments.size())
            throw new InterpreterException("Expected " + parameters.getSignatures().size() + " ,but received " + arguments.arguments.size(), visitor);

        var iterator = arguments.arguments.iterator();

        for (var parameter : parameters.getSignatures()){
            if (!parameter.getType().equals(iterator.next().accept(visitor).getType()))
                throw new InterpreterException("Wrong argument type", visitor);
        }
    }

    public static <T> void check(Literal<T> assignedValue, TokenType variableType, ExecuteVisitor visitor) throws InterpreterException {
        if (!variableType.equals(assignedValue.getType()))
            throw new InterpreterException("Assigned " + assignedValue.getType() + " to variable of type " + variableType, visitor);
    }
}
