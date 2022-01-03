package Parser.Model.Conditions;

import ExceptionHandler.Exceptions.InterpreterException;
import Interpreter.Scope;
import Lexer.TokenType;

import java.util.LinkedList;
import java.util.List;

public class Condition {
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
    public int conditions() { return conditions.size();}

    @Override
    public String toString() {
        return "Condition{" +
                "conditions=" + conditions +
                ", operators=" + operators +
                '}';
    }

    public boolean execute(Scope scope) throws InterpreterException {
        var condIter = conditions.iterator();
        var operatorIter = operators.iterator();

        boolean result = condIter.next().execute(scope);

        while (operatorIter.hasNext()) {
            switch(operatorIter.next()){
                case OR:
                    result = result || condIter.next().execute(scope);
                    break;
                case AND:
                    if (result)
                        result = condIter.next().execute(scope);
                    else
                        return false;
            }
        }
        return result;
    }
}

