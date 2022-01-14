package Interpreter.StdLib;

import ExceptionHandling.Exceptions.InterpreterException;
import Interpreter.ExecuteVisitor;
import Parser.Model.Expressions.Arguments;
import Parser.Model.Expressions.Literal;

public interface StdFunction {
    Literal execute(Arguments arguments, ExecuteVisitor executeVisitor) throws InterpreterException;
}
