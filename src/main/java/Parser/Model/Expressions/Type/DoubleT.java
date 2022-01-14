package Parser.Model.Expressions.Type;

import Lexer.ExceptionHandler.Exceptions.InterpreterException;
import Lexer.TokenType;
import Parser.Model.Expressions.Literal;

public class DoubleT extends Literal<Double> {

    public DoubleT(double val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return val.toString();
    }

    @Override
    public Literal<Double> add(Literal<Double> operand) {
        return new DoubleT(val + operand.val);
    }

    @Override
    public Literal<Double> subtract(Literal<Double> operand) {
        return new DoubleT(val - operand.val);
    }

    @Override
    public Literal<Double> multiply(Literal<Double> operand) {
        return new DoubleT(val * operand.val);
    }

    @Override
    public Literal<Double> divide(Literal<Double> operand) {
        return new DoubleT(val / operand.val);
    }

    @Override
    public Literal<Double> modulo(Literal<Double> operand) throws InterpreterException {
        throw new InterpreterException("Can not modulo" + getClass(), null);
    }

    @Override
    public TokenType getType() {
        return TokenType.DOUBLE;
    }
}
