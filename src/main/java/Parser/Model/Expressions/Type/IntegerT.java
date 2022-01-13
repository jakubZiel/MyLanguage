package Parser.Model.Expressions.Type;

import ExceptionHandler.Exceptions.InterpreterException;
import Lexer.TokenType;
import Parser.Model.Expressions.Literal;

public class IntegerT extends Literal<Integer>{
    public IntegerT(int val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return "IntegerT{" +
                "val=" + val +
                '}';
    }

    @Override
    public Literal<Integer> add(Literal<Integer> operand) {
        return new IntegerT(val + operand.val);
    }

    @Override
    public Literal<Integer> subtract(Literal<Integer> operand) {
        return new IntegerT(val - operand.val);
    }

    @Override
    public Literal<Integer> multiply(Literal<Integer> operand) {
        return new IntegerT(val * operand.val);
    }

    @Override
    public Literal<Integer> divide(Literal<Integer> operand) {
        return new IntegerT(val / operand.val);
    }

    @Override
    public Literal<Integer> modulo(Literal<Integer> operand) throws InterpreterException {
        return new IntegerT(val % operand.val);
    }

    @Override
    public TokenType getType() {
        return TokenType.INT;
    }
}
