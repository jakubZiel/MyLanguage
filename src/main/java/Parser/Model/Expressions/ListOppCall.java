package Parser.Model.Expressions;

import Lexer.ExceptionHandler.Exceptions.InterpreterException;
import Interpreter.Visitor;
import Lexer.Token;
import Lexer.TokenType;

public class ListOppCall extends Expression {
    protected String identifier;
    protected TokenType operation;
    protected ArrowExpression arrowExpression;

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

    public String getIdentifier() {
        return identifier;
    }

    public TokenType getOperation() {
        return operation;
    }

    public ArrowExpression getArrowExpression() {
        return arrowExpression;
    }

    @Override
    public <T> Literal<T> accept(Visitor visitor) throws InterpreterException {
        visitor.visit(this);
        return null;
    }
}
