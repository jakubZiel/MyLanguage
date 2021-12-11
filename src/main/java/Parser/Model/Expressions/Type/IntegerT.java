package Parser.Model.Expressions.Type;

import Lexer.Token;
import Parser.Model.Expressions.Literal;

public class IntegerT extends Literal{
    int val;

    public IntegerT(int val) {
        this.val = val;
    }
}
