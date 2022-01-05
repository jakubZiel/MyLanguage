package Parser.Model.Expressions.Type;

import ExceptionHandler.Exceptions.InterpreterException;
import Lexer.Token;
import Parser.Model.Expressions.Literal;

public class StringT extends Literal<String> {
    public StringT(Token token) {
        this.val = (String) token.getValue();
    }

    public StringT(String val){ this.val = val;}

    @Override
    public String toString() {
        return "StringT{" +
                "value='" + val + '\'' +
                '}';
    }

    @Override
    public Literal<String> add(Literal<String> operand) {
        return new StringT(val.concat(operand.val));
    }

    @Override
    public Literal<String> subtract(Literal<String> operand) throws InterpreterException {
        throw new InterpreterException("Can not subtract" + getClass(), null);
    }

    @Override
    public Literal<String> multiply(Literal<String> operand) throws InterpreterException {
        throw new InterpreterException("Can not multiply" + getClass(), null);
    }

    @Override
    public Literal<String> divide(Literal<String> operand) throws InterpreterException {
        throw new InterpreterException("Can not divide" + getClass(), null);
    }

    @Override
    public Literal<String> modulo(Literal<String> operand) throws InterpreterException {
        throw new InterpreterException("Can not modulo" + getClass(), null);
    }
}
