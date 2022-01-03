package Parser.Model.Nodes;

import Lexer.Token;
import Lexer.TokenType;

public class Signature {
    private TokenType type;
    private String identifier;

    public Signature(TokenType type, Token identifier) {
        this.type = type;
        this.identifier = (String) identifier.getValue();
    }

    public TokenType getType() {
        return type;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return "Signature{" +
                "type=" + type +
                ", identifier='" + identifier + '\'' +
                '}';
    }
}
