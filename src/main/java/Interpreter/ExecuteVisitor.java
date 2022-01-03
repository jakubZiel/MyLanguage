package Interpreter;

import ExceptionHandler.Exceptions.InterpreterException;
import Parser.Model.Blocks.Block;
import Parser.Model.Blocks.FunctionDeclaration;
import Parser.Model.Conditions.Condition;
import Parser.Model.Expressions.Arguments;
import Parser.Model.Expressions.Expression;
import Parser.Model.Expressions.FunctionCall;
import Parser.Model.Expressions.Literal;
import Parser.Model.Instructions.CallInstr;
import Parser.Model.Instructions.Instruction;
import Parser.Model.Instructions.ReturnInst;
import Parser.Model.Nodes.Program;
import Parser.Model.Statements.IfStatement;
import Parser.Model.Statements.WhileStatement;

import java.util.ArrayList;
import java.util.List;

import static Interpreter.Scope.functions;

public class ExecuteVisitor implements Visitor{
    Scope scope;
    Expression returned;
    FunctionCall calledFunction;
    boolean firstBlock = true;

    public ExecuteVisitor(Scope scope) {
        this.scope = scope;
    }

    @Override
    public <T> Literal<T> visit(Expression expression) throws InterpreterException {
        for (var obj : expression.getOperands()) {
            obj.accept(this);
        }
        return null;
    }

    public <T> Literal<T> visit(Literal<T> literal) throws InterpreterException {
        return null;
    }

    public void visit(Program program) throws InterpreterException {
        Scope.setFunctions(program);

        if (program.getFunctions()
                .stream()
                .anyMatch(function -> function.getIdentifier().equals(FunctionDeclaration.MAIN))){
            new FunctionCall(FunctionDeclaration.MAIN, new Arguments()).accept(this);
        }
    }

    public <T> T visit(FunctionCall functionCall) throws InterpreterException {
        calledFunction = functionCall;
        ExecuteVisitor functionContext = new ExecuteVisitor(new Scope(null));
        FunctionDeclaration function = functions.get(functionCall.getIdentifier());

        if (function == null)
            throw new InterpreterException("Function " + functionCall.getIdentifier() + " doesn't exist", null);

        var arguments = visit(functionCall.getArguments());
        var argIter = arguments.iterator();

        for (var signature : function.getParameters().getSignatures()) {
            functionContext.scope.addVariable(signature.getIdentifier(), argIter.next());
        }
        functionContext.visit(function);
        return null;
    }

    public <T> T visit(FunctionDeclaration function) throws InterpreterException {
        visit(function.getBody());
        return null;
    }

    public void visit(WhileStatement whileStatement) throws InterpreterException {
        var condition = whileStatement.getCondition();
        var body = whileStatement.getBody();

        while (condition.execute(scope) && returned == null){
               visit(body);
        }
    }

    public void visit(IfStatement ifStatement) throws InterpreterException {
        if (ifStatement.getCondition().execute(scope)){
            visit(ifStatement.getBlock());
            return;
        }
        var elifsBodyIter = ifStatement.getElseIfBodies().iterator();
        for (var condition : ifStatement.getElseIfConditions()){
            var block =  elifsBodyIter.next();

            if (condition.execute(scope)){
                visit(block);
                return;
            }
        }

        if (ifStatement.getElseBlock() != null){
            visit(ifStatement.getElseBlock());
        }
    }

    public void visit(Instruction instruction) throws InterpreterException {
        instruction.execute(scope);
    }

    public void visit(ReturnInst returnInst) throws InterpreterException {
        returned = returnInst.getReturned().execute(scope);
    }

    public void visit(Block block) throws InterpreterException {
        //TODO don't create new scope when entering new block after function call
        if (firstBlock)
            firstBlock = false;
        else
            scope = new Scope(scope);

        for (var instruction : block.getInstructions()){
            if (returned != null)
                break;
            if (instruction instanceof WhileStatement){
                visit((WhileStatement) instruction);
            } else if (instruction instanceof IfStatement){
                visit((IfStatement) instruction);
            } else if (instruction instanceof ReturnInst){
                visit((ReturnInst) instruction);
            } else if (instruction instanceof CallInstr){

                visit((CallInstr) instruction);
            } else
                visit(instruction);
        }
        scope = scope.parent;
    }

    public void visit(CallInstr callInstr) throws InterpreterException {
        if (callInstr.getFunctionCall() != null)
            visit(callInstr.getFunctionCall());
        if (callInstr.getListOppCall() != null)
            return;
        if (callInstr.getArrayCall() != null)
            return;
    }

    public <T> List<Literal<T>> visit(Arguments arguments) throws InterpreterException {
        List<Literal<T>> literals = new ArrayList<>();
        for (var arg: arguments.arguments) {
            literals.add(arg.execute(scope));
        }
        return literals;
    }

    public boolean visit(Condition condition) throws InterpreterException {
        return condition.execute(scope);
    }
}
