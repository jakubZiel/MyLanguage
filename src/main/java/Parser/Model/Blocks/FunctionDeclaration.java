package Parser.Model.Blocks;

import Lexer.ExceptionHandler.Exceptions.InterpreterException;
import Interpreter.Visited;
import Interpreter.Visitor;
import Lexer.TokenType;
import Parser.Model.Expressions.Literal;
import Parser.Model.Nodes.Parameters;

public class FunctionDeclaration implements Visited {
    private TokenType returnedType ;
    private String identifier;
    private Parameters parameters;
    private Block body;

    public static final String MAIN = "main";

    public FunctionDeclaration(TokenType returnedType, String identifier, Parameters parameters, Block body) {
        this.identifier = identifier;
        this.parameters = parameters;
        this.returnedType = returnedType;
        this.body = body;
    }

    @Override
    public String toString() {
        return "FunctionDeclaration{" +
                "returnedType=" + returnedType +
                ", identifier='" + identifier + '\'' +
                ", parameters=" + parameters +
                ", body=" + body +
                '}';
    }

    public TokenType getReturnedType() {
        return returnedType;
    }

    public void setReturnedType(TokenType returnedType) {
        this.returnedType = returnedType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public Block getBody() {
        return body;
    }

    public void setBody(Block body) {
        this.body = body;
    }

    @Override
    public <T>Literal<T> accept(Visitor visitor) throws InterpreterException {
        visitor.visit(this);
        return null;
    }
}

