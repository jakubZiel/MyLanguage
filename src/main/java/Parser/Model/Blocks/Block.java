package Parser.Model.Blocks;

import Parser.Model.Instructions.Instruction;


import java.util.LinkedList;
import java.util.List;

public class Block {

    List<Instruction> instructions;

    public Block() {
        this.instructions = new LinkedList<>();
    }

    public void addInstruction(Instruction instruction){
        instructions.add(instruction);
    }

    @Override
    public String toString() {
        return "Block{" +
                "instructions=" + instructions +
                '}';
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }
}
