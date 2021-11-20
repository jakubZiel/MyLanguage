package Lexer;

import org.javatuples.Pair;

public abstract class BaseToken {
    private final Pair<Integer, Integer> position;
    private final TokenType type;

    public BaseToken(TokenType type, Pair<Integer, Integer> position) {
        this.type = type;
        this.position = position;
    }
}
