package Parser.Model.Expressions;

import Lexer.TokenType;
import Parser.Model.Node;

import java.util.LinkedList;
import java.util.List;

public class Expression extends Node{
    private List<Node> operands;
    private List<TokenType> operators;

    public Expression() {
        this.operands = new LinkedList<>();
        this.operators = new LinkedList<>();
    }

    public void addOperand(Node operand){
        operands.add(operand);
    }
    public void addOperator(TokenType operator){
        operators.add(operator);
    }
    public int operands(){return operands.size();}

    @Override
    public String toString() {
        return "Expression{" +
                "operands=" + operands +
                ", operators=" + operators +
                '}';
    }
}
