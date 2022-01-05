package Parser.Model.Instructions;

import ExceptionHandler.Exceptions.InterpreterException;
import Interpreter.Visitor;
import Lexer.Token;
import Parser.Model.Expressions.Expression;
import Parser.Model.Expressions.Literal;

public class AssignInst extends Instruction{
    private String identifier;
    private Expression assingedValue;
    public AssignInst(Token identifier, Expression assingedValue) {
        this.assingedValue = assingedValue;
        this.identifier = (String) identifier.getValue();
    }

    @Override
    public String toString() {
        return "AssignInst{" +
                "identifier='" + identifier + '\'' +
                ", assignedValue=" + assingedValue +
                '}';
    }

    public String getIdentifier() {
        return identifier;
    }

    public Expression getAssignedValue() {
        return assingedValue;
    }


    @Override
    public <T> Literal<T> accept(Visitor visitor) throws InterpreterException {
        visitor.visit(this);
        return null;
    }
}


