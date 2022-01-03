package Parser.Model.Conditions;

import ExceptionHandler.Exceptions.InterpreterException;
import Interpreter.Scope;
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

    public boolean execute(Scope scope) throws InterpreterException {
        var leftLiteral = left.execute(scope);
        var rightLiteral = right.execute(scope);

        if (leftLiteral.getClass() != rightLiteral.getClass())
            throw new InterpreterException("Can not compare " + leftLiteral.getClass() + " and " + rightLiteral.getClass(), null);

        switch (operator) {
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
