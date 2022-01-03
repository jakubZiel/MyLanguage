package Parser.Model.Instructions;

import ExceptionHandler.Exceptions.InterpreterException;
import Interpreter.Scope;
import Lexer.Token;
import Parser.Model.Expressions.Expression;
import Parser.Model.Expressions.Type.ListT;

public class ListInitInstr extends InitInstr{
    private ListT nestedType;

    public ListInitInstr(ListT type, Token identifier, Expression assignedValue) {
        super(null, identifier, assignedValue);
        nestedType = type;
    }

    @Override
    public String toString() {
        return "ListInitInstr{" +
                "nestedType=" + nestedType +
                '}';
    }

    @Override
    public void execute(Scope scope) throws InterpreterException {

    }
}
