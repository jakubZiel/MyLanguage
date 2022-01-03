package Parser.Model.Statements;

import ExceptionHandler.Exceptions.InterpreterException;
import Interpreter.Visitor;
import Parser.Model.Blocks.Block;
import Parser.Model.Conditions.Condition;
import Parser.Model.Instructions.Instruction;

public class WhileStatement extends Instruction {
    private Condition condition;
    private Block body;

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
}
