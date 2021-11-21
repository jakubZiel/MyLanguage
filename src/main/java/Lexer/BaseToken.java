package Lexer;

import DataSource.Position;

public abstract class BaseToken {
    public final Position position;
    public final TokenType type;

    public BaseToken(TokenType type, Position position) {
        this.type = type;
        this.position = position;
    }
}
