package Interpreter;

import ExceptionHandler.Exceptions.InterpreterException;
import Parser.Model.Blocks.Block;
import Parser.Model.Blocks.FunctionDeclaration;
import Parser.Model.Conditions.Comparison;
import Parser.Model.Conditions.Condition;
import Parser.Model.Expressions.Expression;
import Parser.Model.Expressions.FunctionCall;
import Parser.Model.Expressions.Literal;
import Parser.Model.Instructions.*;
import Parser.Model.Nodes.Identifier;
import Parser.Model.Nodes.Program;
import Parser.Model.Statements.IfStatement;
import Parser.Model.Statements.WhileStatement;

public interface Visitor {
    <T> Literal<T> visit(Identifier identifier) throws InterpreterException;

    <T> Literal<T> visit(Literal<T> literal) throws InterpreterException;

    <T> Literal<T> visit(Expression expression) throws InterpreterException;

    void visit(Program program) throws InterpreterException;

    <T> Literal<T> visit(FunctionCall functionCall) throws InterpreterException;

    <T> Literal<T> visit(ReturnInst returnInst) throws InterpreterException;

    <T> Literal<T> visit(ListInitInstr listInitInstr) throws InterpreterException;

    <T> Literal<T> visit(CallInstr callInstr) throws InterpreterException;

    void visit(FunctionDeclaration function) throws InterpreterException;

    void visit(AssignInst assignInst) throws InterpreterException;

    void visit(InitInstr initInstr) throws InterpreterException;

    void visit(IfStatement ifStatement) throws InterpreterException;

    void visit(WhileStatement whileStatement) throws InterpreterException;

    boolean visit(Condition condition) throws InterpreterException;

    boolean visit(Comparison comparison) throws InterpreterException;

    void visit(Block block) throws InterpreterException;
}
