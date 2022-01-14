package Interpreter;

import Lexer.ExceptionHandler.Exceptions.InterpreterException;
import Parser.Model.Expressions.Literal;

public interface Visited {
     <T> Literal<T> accept(Visitor visitor) throws InterpreterException;
}
