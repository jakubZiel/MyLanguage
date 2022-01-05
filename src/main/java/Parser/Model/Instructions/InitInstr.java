package Parser.Model.Instructions;

import ExceptionHandler.Exceptions.InterpreterException;
import Interpreter.Scope;
import Interpreter.Visitor;
import Lexer.Token;
import Lexer.TokenType;
import Parser.Model.Expressions.Expression;
import Parser.Model.Expressions.Literal;

public class InitInstr extends Instruction{
    private TokenType type;
    private String identifier;
    private Expression assignedValue;

    public InitInstr(TokenType type, Token identifier, Expression assignedValue) {
        this.type = type;
        this.identifier = (String) identifier.getValue();
        this.assignedValue = assignedValue;
    }

    @Override
    public String toString() {
        return "InitInstr{" +
                "type=" + type +
                ", identifier='" + identifier + '\'' +
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
