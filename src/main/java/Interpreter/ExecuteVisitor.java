package Interpreter;

import ExceptionHandling.Exceptions.InterpreterException;
import Interpreter.StdLib.StdLib;
import Lexer.TokenType;
import Parser.Model.Blocks.Block;
import Parser.Model.Blocks.FunctionDeclaration;
import Parser.Model.Conditions.Comparison;
import Parser.Model.Conditions.Condition;
import Parser.Model.Expressions.*;
import Parser.Model.Expressions.Type.VoidT;
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
    ExecuteVisitor parentContext;
    StdLib library = new StdLib();
    boolean firstBlock = true;

    public ExecuteVisitor(ExecuteVisitor parentContext){
        this.parentContext = parentContext;
    }

    public ExecuteVisitor(Scope scope) {
        this.scope = scope;
    }

    public static ExecuteVisitor executeVisitorFactory(ExecuteVisitor parentContext){
        ExecuteVisitor visitor = new ExecuteVisitor(parentContext);
        visitor.scope = new Scope(null, visitor);
        return visitor;
    }

    public ExecuteVisitor getParentContext() {
        return parentContext;
    }

    public FunctionCall getCalledFunction() {
        return calledFunction;
    }

    @Override
    public Literal<List<Expression>> visit(ListDef listDef) throws InterpreterException {
        List<Expression> literals = new ArrayList<>();
        TokenType elementsType = null;

        for (var expression :  listDef.val){
            var element = expression.accept(this);
            if (elementsType != null && !elementsType.equals(element.getType()))
                throw new InterpreterException("Different type of element in list", this);
            elementsType = element.getType();

            literals.add(element);
        }
        return new ListDef(literals, elementsType);
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
        return (Literal<T>) scope.getVariable(identifier.getName()).getValue();
    }

    public void visit(Program program) throws InterpreterException {
        Scope.setFunctions(program);
        calledFunction = new FunctionCall("App", null);

        if (program.getFunctions()
                .stream()
                .anyMatch(function -> function.getIdentifier().equals(FunctionDeclaration.MAIN))){
            new FunctionCall(FunctionDeclaration.MAIN, new Arguments()).accept(this);
        }
    }

    public Literal visit(FunctionCall functionCall) throws InterpreterException {
        ExecuteVisitor functionContext = ExecuteVisitor.executeVisitorFactory(this);

        functionContext.calledFunction = functionCall;

        if (library.checkFor(functionCall.getIdentifier())){
            return library.execute(functionCall.getIdentifier(), functionCall.getArguments(), this);
        }

        FunctionDeclaration function = functions.get(functionCall.getIdentifier());
        if (function == null)
            throw new InterpreterException("Function " + functionCall.getIdentifier() + " doesn't exist", this);

        var arguments = visit(functionCall.getArguments());
        var argIter = arguments.iterator();

        TypeCheck.check(function.getParameters(), functionCall, this);

        for (var signature : function.getParameters().getSignatures()) {
            functionContext.scope.addVariable(signature.getIdentifier(), argIter.next(), signature.getType());
        }
        functionContext.visit(function);

        if (functionContext.returned == null)
            functionContext.returned = new VoidT();

        TypeCheck.check(functionContext.returned.accept(this), function.getReturnedType(), this);
        return functionContext.returned.accept(this);
    }

    public void visit(FunctionDeclaration function) throws InterpreterException {
        visit(function.getBody());
    }

    public void visit(WhileStatement whileStatement) throws InterpreterException {
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
            throw new InterpreterException("Variable " + assignInst.getIdentifier() + " doesn't exist in this context", this);
    }

    @Override
    public void visit(ListInitInstr listInitInstr) throws InterpreterException {
        if (!scope.addVariable( listInitInstr.getIdentifier(), listInitInstr.getAssignedValue().accept(this), listInitInstr.getNestedType().getType()))
            throw new InterpreterException("Variable " + listInitInstr.getIdentifier() + " already exist in this scope", this);
    }

    @Override
    public void visit(InitInstr initInstr) throws InterpreterException {
        var value = initInstr.getAssignedValue();
        if (!scope.addVariable(initInstr.getIdentifier(), value.accept(this), initInstr.getType()))
            throw new InterpreterException("Variable " + initInstr.getIdentifier() + " already exist in this scope", this);
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
        int index = (int) arrayCall.getIndex().accept(this).val;

        if (arrayCall.getAssignedValue() == null) {
            return scope.getVariable(arrayCall.getIdentifier(), index);
        }
        scope.setVariable(arrayCall.getIdentifier(), arrayCall.getAssignedValue().accept(this), index);
        return null;
    }

    public void visit(ListInsertDeleteCall listInsertDeleteCall) throws InterpreterException {
        var variable = (Variable) scope.getVariable(listInsertDeleteCall.getIdentifier());
        var list = (ListDef) variable.getValue();

        if (listInsertDeleteCall.getOperation() == TokenType.ADD_LIST)
            list.val.add(listInsertDeleteCall.getExpression().accept(this));
        else {
            int index = (int) listInsertDeleteCall.getExpression().accept(this).val;

            list.val.remove(index);
        }
    }

    public void visit(ListOppCall listOppCall) throws InterpreterException {
        var variable = (Variable) scope.getVariable(listOppCall.getIdentifier());
        var list = (ListDef) variable.getValue();

        if (listOppCall.getArrowExpression().getExpression() != null){
            for (int index = 0; index < list.val.size(); index++) {
                scope.addVariable(listOppCall.getArrowExpression().getArgument(), scope.getVariable(listOppCall.getIdentifier(), index), list.val.get(0).accept(this).getType());
                scope.setVariable(listOppCall.getIdentifier(), listOppCall.getArrowExpression().getExpression().accept(this), index);
                scope.remove(listOppCall.getArrowExpression().getArgument());
            }
        } else {
            for (int index = list.val.size() - 1; index > -1; index--){
                scope.addVariable(listOppCall.getArrowExpression().getArgument(), scope.getVariable(listOppCall.getIdentifier(), index), list.val.get(0).accept(this).getType());
                if (!visit(listOppCall.getArrowExpression().getCondition())){
                    scope.addVariable(listOppCall.getArrowExpression().getArgument(), scope.getVariable(listOppCall.getIdentifier(), index), list.getType());
                    list.val.remove(index);
                }
                scope.remove(listOppCall.getArrowExpression().getArgument());
            }
        }
    }

    public void visit(Block block) throws InterpreterException {
        if (firstBlock)
            firstBlock = false;
        else
            scope = new Scope(scope, this);

        for (var instruction : block.getInstructions()){
            if (returned != null)
                break;
            instruction.accept(this);
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
            throw new InterpreterException("Can not compare " + leftLiteral.getClass() + " and " + rightLiteral.getClass(), this);

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
