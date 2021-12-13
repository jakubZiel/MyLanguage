package Parser.Model.Expressions;

import Lexer.Token;

public class FunctionCall extends Expression {
    private String identifier;
    private Arguments arguments;

    public FunctionCall(Token identifier, Arguments arguments) {
        this.identifier = (String) identifier.getValue();
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "FunctionCall{" +
                "identifier='" + identifier + '\'' +
                ", arguments=" + arguments +
                '}';
    }
}
