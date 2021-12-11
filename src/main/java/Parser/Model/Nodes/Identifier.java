package Parser.Model.Nodes;

import Lexer.Token;
import Parser.Model.Node;

public class Identifier extends Node {
    private String name;

    public Identifier(Token token) {
        this.name = (String) token.getValue();
    }
}
