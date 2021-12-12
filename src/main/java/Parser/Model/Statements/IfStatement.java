package Parser.Model.Statements;

import Parser.Model.Blocks.Block;
import Parser.Model.Conditions.Condition;
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
}
