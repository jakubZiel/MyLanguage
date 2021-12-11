package Parser.Model.Blocks;

import Lexer.TokenType;
import Parser.Model.Nodes.Parameters;

public class FunctionDeclaration {
    private TokenType returnedType ;
    private String identifier;
    private Parameters parameters;
    private Block body;

    public FunctionDeclaration(TokenType returnedType, String identifier, Parameters parameters, Block body) {
        this.identifier = identifier;
        this.parameters = parameters;
        this.returnedType = returnedType;
        this.body = body;
    }
}
