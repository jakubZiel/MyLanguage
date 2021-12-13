package Parser.Model.Expressions;

import Lexer.Token;

public class ArrayCall extends Expression {
    String identifier;
    Expression index;

    public ArrayCall(Token token, Expression index) {
        this.identifier = (String) token.getValue();
        this.index = index;
    }

    @Override
    public String toString() {
        return "ArrayCall{" +
                "identifier='" + identifier + '\'' +
                ", index=" + index +
                '}';
    }
}
