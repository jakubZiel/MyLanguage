package Lexer;

import org.javatuples.Pair;

public abstract class BaseToken {
    public final Pair<Integer, Integer> position;
    public final TokenType type;

    public BaseToken(TokenType type, Pair<Integer, Integer> position) {
        this.type = type;
        this.position = position;
    }
}
