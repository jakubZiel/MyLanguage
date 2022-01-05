package Interpreter;

import ExceptionHandler.Exceptions.InterpreterException;
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
    public <T> Literal<List<Literal<T>>> visit(ListDef listDef) throws InterpreterException {

        List<Literal<T>> literals = new ArrayList<>();

        for (var expression : listDef.val){
            literals.add(expression.accept(this));
        }

        return new Literal<>(literals);
    }

    @Override
    public <T> Literal<T> visit(Expression expression) throws InterpreterException {
        var operandIter = expression.getOperands().iterator();
        var operatorIter = expression.getOperators().iterator();
        Literal<T> result = operandIter.next().accept(this);

        while (operatorIter.hasNext()) {
            switch(operatorIter.next()){
                case ADD:
                    result = result.add(operandIter.next().accept(this));
                    break;
                case SUBTRACT:
                    result = result.subtract(operandIter.next().accept(this));
                    break;
                case MULTIPLY:
                    result = result.multiply(operandIter.next().accept(this));
                    break;
                case DIVIDE:
                    result = result.divide(operandIter.next().accept(this));
                    break;
                case MODULO:
                    result = result.modulo(operandIter.next().accept(this));
                    break;
            }
        }
        return result;
    }

    public <T> Literal<T> visit(Literal<T> literal) throws InterpreterException {
        return literal;
    }

    public <T> Literal<T> visit(Identifier identifier) throws InterpreterException{
        var variable = (Variable) scope.getVariable(identifier.getName());
        var value = (Literal<T>) variable.getValue();
        return value;
    }

    public void visit(Program program) throws InterpreterException {
        Scope.setFunctions(program);

        if (program.getFunctions()
                .stream()
                .anyMatch(function -> function.getIdentifier().equals(FunctionDeclaration.MAIN))){
            new FunctionCall(FunctionDeclaration.MAIN, new Arguments()).accept(this);
        }
    }

    public <T> Literal<T> visit(FunctionCall functionCall) throws InterpreterException {
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
        return functionContext.returned.accept(this);
    }

    public void visit(FunctionDeclaration function) throws InterpreterException {
        visit(function.getBody());
    }

    public void visit(WhileStatement whileStatement) throws InterpreterException {
        var condition = whileStatement.getCondition();
        var body = whileStatement.getBody();

        while (visit(whileStatement.getCondition()) && returned == null){
               visit(body);
        }
    }

    public void visit(IfStatement ifStatement) throws InterpreterException {
        if (visit(ifStatement.getCondition())){
            visit(ifStatement.getBlock());
            return;
        }
        var elifBodyIter = ifStatement.getElseIfBodies().iterator();
        for (var condition : ifStatement.getElseIfConditions()){
            var block =  elifBodyIter.next();

            if (visit(condition)){
                visit(block);
                return;
            }
        }
        if (ifStatement.getElseBlock() != null){
            visit(ifStatement.getElseBlock());
        }
    }

    public <T> Literal<T> visit(ReturnInst returnInst) throws InterpreterException {
        returned = returnInst.getReturned().accept(this);
        return (Literal<T>) returned;
    }

    @Override
    public void visit(AssignInst assignInst) throws InterpreterException {
        if (!scope.setVariable( assignInst.getIdentifier(), assignInst.getAssignedValue().accept(this)))
            throw new InterpreterException("Variable " + assignInst.getIdentifier() + " doesn't exist in this context", null);
    }

    @Override
    public void visit(ListInitInstr listInitInstr) throws InterpreterException {
        //TODO add type checking
        if (!scope.addVariable( listInitInstr.getIdentifier(), listInitInstr.getAssignedValue().accept(this)))
            throw new InterpreterException("Variable " + listInitInstr.getIdentifier() + " doesn't exist in this context", null);
    }

    @Override
    public void visit(InitInstr initInstr) throws InterpreterException {
        if (!scope.addVariable(initInstr.getIdentifier(), initInstr.getAssignedValue().accept(this)))
            throw new InterpreterException("Variable " + initInstr.getIdentifier() + " doesn't exist in this context", null);
    }

    public <T> Literal<T> visit(CallInstr callInstr) throws InterpreterException {
        if (callInstr.getFunctionCall() != null)
            return callInstr.getFunctionCall().accept(this);
        else if (callInstr.getListOppCall() != null)
            return callInstr.getListOppCall().accept(this);
        else
            return callInstr.getArrayCall().accept(this);
    }

    public <T> Literal<T> visit(ArrayCall arrayCall) throws InterpreterException{
        if (arrayCall.getAssignedValue() == null) {

            return null;
        }
        return null;
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
            instruction.accept(this);
            System.out.println(instruction);
        }
        scope = scope.parent;
    }

    public <T> List<Literal<T>> visit(Arguments arguments) throws InterpreterException {
        List<Literal<T>> literals = new ArrayList<>();
        for (var arg: arguments.arguments) {
            literals.add(arg.accept(this));
        }
        return literals;
    }

    public boolean visit(Condition condition) throws InterpreterException {

        if (condition instanceof Comparison)
            return visit((Comparison) condition);

        var condIter = condition.getConditions().iterator();
        var operatorIter = condition.getOperators().iterator();

        var operand = condIter.next();
        boolean result = operand instanceof Comparison ? visit((Comparison) operand) : visit(operand);

        while (operatorIter.hasNext()) {
            operand = condIter.next();
            var currentBool = operand instanceof Comparison ? visit((Comparison) operand) : visit(operand);
            switch(operatorIter.next()){
                case OR:
                    result = result || currentBool;
                    break;
                case AND:
                    if (result)
                        result = currentBool;
                    else
                        return false;
            }
        }
        return result;
    }

    public boolean visit(Comparison comparison) throws InterpreterException{
        var leftLiteral = comparison.getLeft().accept(this);
        var rightLiteral = comparison.getRight().accept(this);

        if (leftLiteral.getClass() != rightLiteral.getClass())
            throw new InterpreterException("Can not compare " + leftLiteral.getClass() + " and " + rightLiteral.getClass(), null);

        switch (comparison.getOperator()) {
            case EQUAL:
                return leftLiteral.equal(rightLiteral);
            case N_EQUAL:
                return leftLiteral.notEqual(rightLiteral);
            case ANGLE_L:
                return leftLiteral.less(rightLiteral);
            case ANGLE_R:
                return leftLiteral.more(rightLiteral);
            case LESS_OR_EQUAL:
                return leftLiteral.lessEqual(rightLiteral);
            default:
                return leftLiteral.moreEqual(rightLiteral);
        }
    }
}
