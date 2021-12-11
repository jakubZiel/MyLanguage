package Parser.Model.Expressions;

import Lexer.Token;
import Parser.Model.Node;

public class ArrayCall extends Node {
    String name;
    Expression index;

    public ArrayCall(Token token, Expression index) {
        this.name = (String) token.getValue();
        this.index = index;
    }
}
