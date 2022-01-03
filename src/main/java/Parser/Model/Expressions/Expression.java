package Parser.Model.Expressions;

import ExceptionHandler.Exceptions.InterpreterException;
import Interpreter.Scope;
import Interpreter.Visited;
import Interpreter.Visitor;
import Lexer.TokenType;

import java.util.LinkedList;
import java.util.List;

public class Expression implements Visited {
    private List<Expression> operands;
    private List<TokenType> operators;

    public <T> Literal<T> execute(Scope scope) throws InterpreterException {
        var operandIter = operands.iterator();
        var operatorIter = operators.iterator();
        Literal<T> result = operandIter.next().execute(scope);

        while (operatorIter.hasNext()) {
            switch(operatorIter.next()){
                case ADD:
                    result = result.add(operandIter.next().execute(scope));
                    break;
                case SUBTRACT:
                    result = result.subtract(operandIter.next().execute(scope));
                    break;
                case MULTIPLY:
                    result = result.multiply(operandIter.next().execute(scope));
                    break;
                case DIVIDE:
                    result = result.divide(operandIter.next().execute(scope));
                    break;
                case MODULO:
                    result = result.modulo(operandIter.next().execute(scope));
                    break;
            }
        }
        return result;
    }

    public Expression() {
        this.operands = new LinkedList<>();
        this.operators = new LinkedList<>();
    }

    public void addOperand(Expression operand){
        operands.add(operand);
    }
    public void addOperator(TokenType operator){
        operators.add(operator);
    }
    public int operands(){return operands.size();}

    public List<Expression> getOperands() {
        return operands;
    }

    public List<TokenType> getOperators() {
        return operators;
    }

    @Override
    public String toString() {
        return "Expression{" +
                "operands=" + operands +
                ", operators=" + operators +
                '}';
    }

    @Override
    public Object accept(Visitor visitor) throws InterpreterException {
        return visitor.visit(this);
    }
}
