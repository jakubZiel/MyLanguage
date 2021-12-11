package Lexer;

import DataSource.Position;
import static Lexer.TokenType.*;

public class Token extends BaseToken{
    private Object value;

    public Token(TokenType type, Position position, Object value) {
        super(type, position);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public boolean tokenIs(TokenType... tokenTypes){
        for (var type : tokenTypes){
            if (this.type == type)
                return true;
        }
        return false;
    }

    public Token(){
        super(NULL, new Position(-1, -1));
    }
}
