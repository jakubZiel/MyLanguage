package Parser.Model.Expressions;

import Lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class ListDef extends Literal {
    private TokenType elementsType;
    private List<Expression> elements;

    public ListDef() {
        this.elements = new ArrayList<>();
    }
    public ListDef(List<Expression> elements) {
        this.elements = elements;
    }

}
