package Interpreter.StdLib;

import Lexer.ExceptionHandler.Exceptions.InterpreterException;
import Interpreter.ExecuteVisitor;
import Parser.Model.Expressions.Arguments;
import Parser.Model.Expressions.Literal;

import java.util.HashMap;

public class StdLib {
   final HashMap<String, StdFunction> functions;

    public StdLib() {
        this.functions = new HashMap<>();
        functions.put("print", new StdPrint());
    }

    public boolean checkFor(String function){
        return functions.containsKey(function);
    }

    public Literal execute(String function, Arguments arguments, ExecuteVisitor executeVisitor) throws InterpreterException {
        return functions.get(function).execute(arguments, executeVisitor);
    }
}
