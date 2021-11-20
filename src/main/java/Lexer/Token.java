package Lexer;

import org.javatuples.Pair;
import static Lexer.TokenType.*;

public class Token extends BaseToken{
    private Object value;

    public Token(TokenType type, Pair<Integer, Integer> position, Object value) {
        super(type, position);
        this.value = value;
    }

    public Token(){
        super(NULL, new Pair<>(-1, -1));
    }
}
