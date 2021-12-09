package Parser;

import DataSource.DataSourceString;
import DataSource.IDataSource;
import Lexer.Token;
import ExceptionHandler.Exceptions.UnexpectedCharException;
import Lexer.Lexer;

import java.io.IOException;

import Lexer.TokenType;

public class Parser {

    private Token peeked;
    private final Lexer lexer;

    public Parser (Lexer lexer){
        this.lexer = lexer;
    }

    private Token getToken() throws UnexpectedCharException, IOException {
        if (peeked != null){
            Token token = peeked;
            peeked = null;
            return  token;
        }
        return lexer.scanToken();
    }

    private Token peekToken() throws UnexpectedCharException, IOException {
        if (peeked == null){
            peeked = lexer.scanToken();
        }
        return peeked;
    }

    public static void main(String[] args) throws UnexpectedCharException, IOException {
        String source =
                "int main(){" +
                        "int a = 34;" +
                        "while(a > 30){" +
                        "a = a - 1;" +
                        "} " +
                        "}";

        IDataSource ds = new DataSourceString(source);
        Lexer lexer = new Lexer(ds);
        Parser parser = new Parser(lexer);

        while (true){
            var token = lexer.scanToken();
            System.out.println(token.type);
            if (token.type ==  TokenType.END_T)
                break;
        }

    }

}
