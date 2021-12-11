package Parser.Model.Nodes;

import Lexer.Token;
import Lexer.TokenType;

public class Signature {
    private TokenType type;
    private String identfiier;

    public Signature(TokenType type, Token identfiier) {
        this.type = type;
        this.identfiier = (String) identfiier.getValue();
    }
}
