package Parser.Model.Expressions.Type;

import Lexer.Token;
import Parser.Model.Expressions.Literal;

public class StringT extends Literal {
    private String value;

    public StringT(Token token) {
        this.value = (String) token.getValue();
    }
}
