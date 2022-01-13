package Parser.Model.Expressions;

import ExceptionHandler.Exceptions.InterpreterException;
import Interpreter.Visitor;
import Lexer.Token;

public class ListInsertDeleteCall extends ListOppCall {
    private final Expression expression;

    public ListInsertDeleteCall(Token identifier, Token operation, Expression expression) {
        super(identifier, operation, null);
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public <T> Literal<T> accept(Visitor visitor) throws InterpreterException {
        visitor.visit(this);
        return null;
    }
}
