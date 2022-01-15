package Interpreter;

import ExceptionHandling.Exceptions.InterpreterException;
import Lexer.TokenType;
import Parser.Model.Expressions.Arguments;

import Parser.Model.Expressions.FunctionCall;
import Parser.Model.Expressions.Literal;
import Parser.Model.Nodes.Parameters;

public class TypeCheck {
    public static void check(Parameters parameters, FunctionCall functionCall, ExecuteVisitor visitor) throws InterpreterException {
        Arguments arguments = functionCall.getArguments();
        if (parameters.getSignatures().size() != arguments.arguments.size())
            throw new InterpreterException("Expected " + parameters.getSignatures().size() + " ,but received " + arguments.arguments.size(), visitor);

        var iterator = arguments.arguments.listIterator();

        for (var parameter : parameters.getSignatures()){
            if (!parameter.getType().equals(iterator.next().accept(visitor).getType()))
                throw new InterpreterException("Wrong argument type at function call " + functionCall.getIdentifier() + ", expected " + parameter.getType() + " at position " + iterator.previousIndex(), visitor);
        }
    }

    public static <T> void check(Literal<T> assignedValue, TokenType variableType, ExecuteVisitor visitor) throws InterpreterException {
        if (!variableType.equals(assignedValue.getType()))
            throw new InterpreterException("Assigned " + assignedValue.getType() + " to variable of type " + variableType, visitor);
    }
}
