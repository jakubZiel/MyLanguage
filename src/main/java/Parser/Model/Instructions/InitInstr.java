package Parser.Model.Instructions;

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
}
