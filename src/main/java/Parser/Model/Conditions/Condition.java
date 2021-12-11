package Parser.Model.Conditions;

import Lexer.TokenType;
import Parser.Model.Node;

import java.util.LinkedList;
import java.util.List;

public class Condition extends Node {
    private List<Condition> conditions;
    private List<TokenType> operators;

    public Condition() {
        this.conditions = new LinkedList<>();
        this.operators = new LinkedList<>();
    }

    public void addOperand(Condition condition){
        conditions.add(condition);
    }
    public void addOperator(TokenType operator){
        operators.add(operator);
    }
}
