package Parser.Model.Instructions;

import ExceptionHandler.Exceptions.InterpreterException;
import Interpreter.Scope;


public abstract class Instruction {
    public void execute(Scope scope) throws InterpreterException {}
}
