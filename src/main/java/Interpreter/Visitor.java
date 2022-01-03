package Interpreter;

import ExceptionHandler.Exceptions.InterpreterException;
import Parser.Model.Blocks.FunctionDeclaration;
import Parser.Model.Expressions.Expression;
import Parser.Model.Expressions.FunctionCall;
import Parser.Model.Expressions.Literal;
import Parser.Model.Instructions.Instruction;
import Parser.Model.Nodes.Program;

public interface Visitor {
    <T> Literal<T> visit(Literal<T> literal) throws InterpreterException;

    <T> Literal<T> visit(Expression expression) throws InterpreterException;

    void visit(Program program) throws InterpreterException;

    <T> T visit(FunctionCall functionCall) throws InterpreterException;

    <T> T visit(FunctionDeclaration function) throws InterpreterException;

    void visit(Instruction instruction) throws InterpreterException;
}
