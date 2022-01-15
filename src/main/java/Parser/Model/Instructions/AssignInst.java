package Parser.Model.Instructions;

import ExceptionHandling.Exceptions.InterpreterException;
import Interpreter.Visitor;
import Lexer.Token;
import Parser.Model.Expressions.Expression;
import Parser.Model.Expressions.Literal;

public class AssignInst extends Instruction{
    private final String identifier;
    private final Expression assignedValue;
    public AssignInst(Token identifier, Expression assignedValue) {
        this.assignedValue = assignedValue;
        this.identifier = (String) identifier.getValue();
    }

    @Override
    public String toString() {
        return "AssignInst{" +
                "identifier='" + identifier + '\'' +
                ", assignedValue=" + assignedValue +
                '}';
    }

    public String getIdentifier() {
        return identifier;
    }

    public Expression getAssignedValue() {
        return assignedValue;
    }


    @Override
    public <T> Literal<T> accept(Visitor visitor) throws InterpreterException {
        visitor.visit(this);
        return null;
    }
}


