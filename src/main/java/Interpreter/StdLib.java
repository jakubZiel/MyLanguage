package Interpreter;

import Parser.Model.Expressions.Literal;

public class StdLib {
    public <T> void print(Literal<T> literal) {
        System.out.println(literal.val);
    }
}
