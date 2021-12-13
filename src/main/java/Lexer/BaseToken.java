package Lexer;

import DataSource.Position;

public abstract class BaseToken {
    public Position position;
    public TokenType type;

    public BaseToken(TokenType type, Position position) {
        this.type = type;
        this.position = position;
    }
}
