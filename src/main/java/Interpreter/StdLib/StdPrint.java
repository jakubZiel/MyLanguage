package Interpreter.StdLib;

import Lexer.ExceptionHandler.Exceptions.InterpreterException;
import Interpreter.ExecuteVisitor;
import Parser.Model.Expressions.Arguments;
import Parser.Model.Expressions.Literal;
import Parser.Model.Expressions.Type.VoidT;

public class StdPrint implements StdFunction{
    @Override
    public Literal<?> execute(Arguments arguments, ExecuteVisitor executeVisitor) throws InterpreterException {

        for (var argument : arguments.arguments){
            System.out.print(argument.accept(executeVisitor).val);
        }

        if (arguments.arguments.size() == 0)
            System.out.println();

        return new VoidT();
    }
}
