package Interpreter;

import ExceptionHandling.Exceptions.InterpreterException;
import Parser.Model.Blocks.Block;
import Parser.Model.Blocks.FunctionDeclaration;
import Parser.Model.Conditions.Comparison;
import Parser.Model.Conditions.Condition;
import Parser.Model.Expressions.*;
import Parser.Model.Instructions.*;
import Parser.Model.Nodes.Identifier;
import Parser.Model.Nodes.Program;
import Parser.Model.Statements.IfStatement;
import Parser.Model.Statements.WhileStatement;

import java.util.List;

public interface Visitor {
    <T> Literal<T> visit(Identifier identifier) throws InterpreterException;

    <T> Literal<T> visit(Literal<T> literal) throws InterpreterException;

    Literal<List<Expression>> visit(ListDef listDef) throws InterpreterException;

    <T> Literal<T> visit(Expression expression) throws InterpreterException;

    void visit(Program program) throws InterpreterException;

    <T> Literal<T> visit(ReturnInst returnInst) throws InterpreterException;

    void visit(ListInitInstr listInitInstr) throws InterpreterException;

    <T> Literal<T> visit(FunctionCall functionCall) throws InterpreterException;

    <T> Literal<T> visit(CallInstr callInstr) throws InterpreterException;

    <T> Literal<T> visit(ArrayCall arrayCall) throws InterpreterException;

    void visit(ListOppCall listOppCall) throws InterpreterException;

    void visit(ListInsertDeleteCall listInsertDeleteCall) throws InterpreterException;

    void visit(FunctionDeclaration function) throws InterpreterException;

    void visit(AssignInst assignInst) throws InterpreterException;

    void visit(InitInstr initInstr) throws InterpreterException;

    void visit(IfStatement ifStatement) throws InterpreterException;

    void visit(WhileStatement whileStatement) throws InterpreterException;

    boolean visit(Condition condition) throws InterpreterException;

    boolean visit(Comparison comparison) throws InterpreterException;

    void visit(Block block) throws InterpreterException;
}
