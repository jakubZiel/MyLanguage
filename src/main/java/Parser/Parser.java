package Parser;

import Lex.Token;
import ExceptionHandler.Exceptions.UnexpectedCharException;
import Lex.Lexer;

import java.io.IOException;

public class Parser {

    private Token token;
    private final Lexer lexer;

    public Parser (Lexer lexer){
        this.lexer = lexer;
    }

    private boolean nextToken(){
        try{
            token = lexer.scanToken();
        } catch (UnexpectedCharException | IOException e) {
            e.printStackTrace();
            return false;
        }
        return  true;
    }


}
