package Interpreter;

import ExceptionHandler.Exceptions.InterpreterException;

public interface Visited {
     Object accept(Visitor visitor) throws InterpreterException;
}
