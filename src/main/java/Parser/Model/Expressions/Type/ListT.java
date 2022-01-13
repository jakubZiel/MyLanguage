package Parser.Model.Expressions.Type;

import Lexer.TokenType;

public class ListT {
    private TokenType type;
    private int nesting;

    public ListT(int nesting, TokenType type) {
        this.nesting = nesting;
        this.type = type;
    }

    @Override
    public String toString() {
        return "ListT{" +
                "type=" + type +
                ", nesting=" + nesting +
                '}';
    }


}
