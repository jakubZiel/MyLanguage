package Parser.Model.Expressions;

import ExceptionHandling.Exceptions.InterpreterException;
import Interpreter.Visited;
import Interpreter.Visitor;
import Lexer.TokenType;

import java.util.LinkedList;
import java.util.List;

public class Expression implements Visited {
    private List<Expression> operands;
    private List<TokenType> operators;

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
    public <T>Literal<T> accept(Visitor visitor) throws InterpreterException {
        return visitor.visit(this);
    }
}
