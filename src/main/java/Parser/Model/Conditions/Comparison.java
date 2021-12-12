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

    public void setLeft(Expression left) {
        this.left = left;
    }

    public void setRight(Expression right) {
        this.right = right;
    }
}
