package Parser.Model.Blocks;

import Lexer.ExceptionHandler.Exceptions.InterpreterException;
import Interpreter.Visited;
import Interpreter.Visitor;
import Parser.Model.Expressions.Literal;
import Parser.Model.Instructions.Instruction;


import java.util.LinkedList;
import java.util.List;

public class Block implements Visited {

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

    @Override
    public <T> Literal<T> accept(Visitor visitor) throws InterpreterException {

        return null;
    }
}
