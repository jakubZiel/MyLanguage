package Lexer;

import ExceptionHandler.Exceptions.UnexpectedCharException;

import java.io.IOException;

public class Filter {
    private final Lexer lexer;

    public Filter(Lexer lexer){
        this.lexer = lexer;
    }

    public Token getToken() throws UnexpectedCharException, IOException {
        var token = lexer.scanToken();

        while(token.type != TokenType.COMMENT_T) {
            token = lexer.scanToken();
        }
        return token;
    }
}
