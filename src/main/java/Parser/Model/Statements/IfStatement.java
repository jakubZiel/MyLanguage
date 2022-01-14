package Parser.Model.Statements;

import Lexer.ExceptionHandler.Exceptions.InterpreterException;
import Interpreter.Visitor;
import Parser.Model.Blocks.Block;
import Parser.Model.Conditions.Condition;
import Parser.Model.Expressions.Literal;
import Parser.Model.Instructions.Instruction;

import java.util.List;

public class IfStatement extends Instruction {
    private Condition condition;
    private Block block;
    private List<Condition> elseIfConditions;
    private List<Block> elseIfBodies;
    private Block elseBlock;

    public IfStatement(Condition condition, Block block, List<Condition> elseIfConditions, List<Block> elseIfBodies, Block elseBlock) {
        this.condition = condition;
        this.block = block;
        this.elseIfConditions = elseIfConditions;
        this.elseIfBodies = elseIfBodies;
        this.elseBlock = elseBlock;
    }

    public Condition getCondition() {
        return condition;
    }

    public Block getBlock() {
        return block;
    }

    public List<Condition> getElseIfConditions() {
        return elseIfConditions;
    }

    public List<Block> getElseIfBodies() {
        return elseIfBodies;
    }

    public Block getElseBlock() {
        return elseBlock;
    }

    @Override
    public String toString() {
        return "IfStatement{" +
                "condition=" + condition +
                ", block=" + block +
                ", elseIfConditions=" + elseIfConditions +
                ", elseIfBodies=" + elseIfBodies +
                ", elseBlock=" + elseBlock +
                '}';
    }

    @Override
    public <T> Literal<T> accept(Visitor visitor) throws InterpreterException {
        visitor.visit(this);
        return null;
    }
}
