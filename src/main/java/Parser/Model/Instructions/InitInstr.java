package Parser.Model.Instructions;

import ExceptionHandler.Exceptions.InterpreterException;
import Interpreter.Scope;
import Lexer.Token;
import Lexer.TokenType;
import Parser.Model.Expressions.Expression;

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

    @Override
    public void execute(Scope scope) throws InterpreterException {
        if (!scope.addVariable(identifier, assignedValue.execute(scope)))
            throw new InterpreterException("Variable " + identifier + " doesn't exist in this context", null);
    }
}
