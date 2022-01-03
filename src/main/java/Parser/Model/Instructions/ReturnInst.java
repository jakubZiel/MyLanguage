package Parser.Model.Instructions;

import Parser.Model.Expressions.Expression;

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
}
