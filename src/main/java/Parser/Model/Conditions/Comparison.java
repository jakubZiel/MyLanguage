package Parser.Model.Conditions;

import Lexer.TokenType;
import Parser.Model.Expressions.Expression;

public class Comparison extends Condition {
    TokenType operator;
    Expression left;
    Expression right;

    public Comparison(TokenType operator, Expression left, Expression right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "Comparison{" +
                "operator=" + operator +
                ", left=" + left +
                ", right=" + right +
                '}';
    }

    public TokenType getOperator() {
        return operator;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }
}
