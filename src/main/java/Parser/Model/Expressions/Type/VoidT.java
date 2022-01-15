package Parser.Model.Expressions.Type;

import ExceptionHandling.Exceptions.InterpreterException;
import Lexer.TokenType;
import Parser.Model.Expressions.Literal;

public class VoidT extends Literal<Void> {
    @Override
    public Literal<Void> add(Literal<Void> operand) throws InterpreterException {
        throw new InterpreterException("Can't operate on Void", null);
    }

    @Override
    public Literal<Void> subtract(Literal<Void> operand) throws InterpreterException {
        throw new InterpreterException("Can't operate on Void", null);
    }

    @Override
    public Literal<Void> multiply(Literal<Void> operand) throws InterpreterException {
        throw new InterpreterException("Can't operate on Void", null);

    }

    @Override
    public Literal<Void> divide(Literal<Void> operand) throws InterpreterException {
        throw new InterpreterException("Can't operate on Void", null);

    }

    @Override
    public Literal<Void> modulo(Literal<Void> operand) throws InterpreterException {
        throw new InterpreterException("Can't operate on Void", null);
    }

    @Override
    public TokenType getType() {
        return TokenType.VOID;
    }
}
