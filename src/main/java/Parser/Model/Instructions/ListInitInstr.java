package Parser.Model.Instructions;

import Exceptions.InterpreterException;
import Interpreter.Visitor;
import Lexer.Token;
import Parser.Model.Expressions.Expression;
import Parser.Model.Expressions.Literal;
import Parser.Model.Expressions.Type.ListT;

public class ListInitInstr extends InitInstr{
    private final ListT nestedType;

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

    public ListT getNestedType() {
        return nestedType;
    }

    @Override
    public <T> Literal<T> accept(Visitor visitor) throws InterpreterException {
        visitor.visit(this);
        return null;
    }
}