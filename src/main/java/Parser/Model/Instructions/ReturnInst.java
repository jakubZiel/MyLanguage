package Parser.Model.Instructions;

import ExceptionHandling.Exceptions.InterpreterException;
import Interpreter.Visitor;
import Parser.Model.Expressions.Expression;
import Parser.Model.Expressions.Literal;

public class ReturnInst extends Instruction {

    Expression returned;

    public ReturnInst(Expression returned) {
        this.returned = returned;
    }

    public Expression getReturned() {
        return returned;
    }

    @Override
    public String toString() {
        return "ReturnInst{" +
                "returned=" + returned +
                '}';
    }

    @Override
    public <T> Literal<T> accept(Visitor visitor) throws InterpreterException {
        return visitor.visit(this);
    }
}
