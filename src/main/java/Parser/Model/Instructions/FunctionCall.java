package Parser.Model.Instructions;

import Lexer.Token;
import Parser.Model.Expressions.Arguments;

public class FunctionCall extends Instruction {
    private String identifier;
    private Arguments arguments;

    public FunctionCall(Token identifier, Arguments arguments) {
        this.identifier = (String) identifier.getValue();
        this.arguments = arguments;
    }
}
