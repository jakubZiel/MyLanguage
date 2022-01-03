package Parser.Model.Expressions;

import ExceptionHandler.Exceptions.InterpreterException;
import Interpreter.Scope;
import Lexer.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListDef extends Literal<List<Expression>> {
    private TokenType elementsType;
    public ListDef() {
        this.val = new ArrayList<>();
    }

    public ListDef(List<Expression> elements) {
        this.val = elements;
    }

    @Override
    public String toString() {
        return "ListDef{" +
                "elementsType=" + elementsType +
                ", elements=" + val +
                '}';
    }

    @Override
    public Literal<List<Expression>> execute(Scope scope) throws InterpreterException {
        Literal<List<Expression>> result = new ListDef();

        result.val.add(val.get(0).execute(scope));

        return result;
    }

    @Override
    public Literal<List<Expression>> add(Literal<List<Expression>> operand) {
        return new ListDef(Stream.concat(val.stream(), operand.val.stream()).collect(Collectors.toList()));
    }

    @Override
    public Literal<List<Expression>> subtract(Literal<List<Expression>> operand) throws InterpreterException {
        val.removeAll(operand.val);
        return this;
    }

    @Override
    public Literal<List<Expression>> multiply(Literal<List<Expression>> operand) throws InterpreterException {
        throw new InterpreterException("Can not multiply" + getClass(), null);
    }

    @Override
    public Literal<List<Expression>> divide(Literal<List<Expression>> operand) throws InterpreterException {
        throw new InterpreterException("Can not divide" + getClass(), null);
    }

    @Override
    public Literal<List<Expression>> modulo(Literal<List<Expression>> operand) throws InterpreterException {
        throw new InterpreterException("Can not modulo" + getClass(), null);
    }
}
