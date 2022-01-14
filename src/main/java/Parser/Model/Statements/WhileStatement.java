package Parser.Model.Statements;

import ExceptionHandling.Exceptions.InterpreterException;
import Interpreter.Visitor;
import Parser.Model.Blocks.Block;
import Parser.Model.Conditions.Condition;
import Parser.Model.Expressions.Literal;
import Parser.Model.Instructions.Instruction;

public class WhileStatement extends Instruction {
    private final Condition condition;
    private final Block body;

    public WhileStatement(Condition condition, Block body) {
        this.body = body;
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "WhileStatement{" +
                "condition=" + condition +
                ", body=" + body +
                '}';
    }

    public Block getBody() {
        return body;
    }

    public Condition getCondition() {
        return condition;
    }

    @Override
    public <T> Literal<T> accept(Visitor visitor) throws InterpreterException {
        visitor.visit(this);
        return null;
    }
}
