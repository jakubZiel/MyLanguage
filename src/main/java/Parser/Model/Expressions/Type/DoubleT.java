package Parser.Model.Expressions.Type;

import Parser.Model.Expressions.Literal;

public class DoubleT extends Literal {
    double val;

    public DoubleT(double val) {
        this.val = val;
    }
}
