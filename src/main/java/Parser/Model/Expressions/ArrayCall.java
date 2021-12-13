package Parser.Model.Expressions;

import Lexer.Token;
import Parser.Model.Node;

public class ArrayCall extends Node {
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
