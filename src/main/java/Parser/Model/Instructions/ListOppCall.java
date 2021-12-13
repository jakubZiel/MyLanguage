package Parser.Model.Instructions;

import Lexer.Token;
import Lexer.TokenType;
import Parser.Model.Expressions.ArrowExpression;

public class ListOppCall extends Instruction{
    private String identifier;
    private TokenType operation;
    private ArrowExpression arrowExpression;

    public ListOppCall(Token identifier, Token operation, ArrowExpression arrowExpression) {
        this.identifier = (String) identifier.getValue();
        this.operation = operation.type;
        this.arrowExpression = arrowExpression;
    }

    @Override
    public String toString() {
        return "ListOppCall{" +
                "identifier='" + identifier + '\'' +
                ", operation=" + operation +
                ", arrowExpression=" + arrowExpression +
                '}';
    }
}
