package Parser.Model.Conditions;

import Exceptions.InterpreterException;
import Interpreter.Visited;
import Interpreter.Visitor;
import Lexer.TokenType;
import Parser.Model.Expressions.Literal;

import java.util.LinkedList;
import java.util.List;

public class Condition implements Visited {
    private final List<Condition> conditions;
    private final List<TokenType> operators;

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
    public int conditions() { return conditions.size();}

    @Override
    public String toString() {
        return "Condition{" +
                "conditions=" + conditions +
                ", operators=" + operators +
                '}';
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public List<TokenType> getOperators() {
        return operators;
    }

    @Override
    public <T> Literal<T> accept(Visitor visitor) throws InterpreterException {
        return null;
    }
}

