package Parser.Model.Expressions;

import Lexer.Token;
import Parser.Model.Conditions.Condition;

public class ArrowExpression {
    private String argument;
    private Expression expression;
    private Condition condition;

    public ArrowExpression(Token argument, Expression expression) {
        this.argument = (String) argument.getValue();
        this.expression = expression;
    }

    public ArrowExpression(Token argument, Condition condition) {
        this.argument = (String) argument.getValue();
        this.condition = condition;
    }

    public String getArgument() {
        return argument;
    }

    public Expression getExpression() {
        return expression;
    }

    public Condition getCondition() {
        return condition;
    }

    @Override
    public String toString() {
        return "ArrowExpression{" +
                "argument='" + argument + '\'' +
                ", expression=" + expression +
                ", condition=" + condition +
                '}';
    }
}
