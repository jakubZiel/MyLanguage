package Parser.Model.Instructions;

import DataSource.DataSourceString;
import DataSource.IDataSource;
import Lexer.Lexer;
import Lexer.Token;
import Lexer.TokenType;
import Parser.Model.Expressions.ArrowExpression;
import Parser.Parser;

public class ListOppCall extends Instruction{
    private String identifier;
    private TokenType operation;
    private ArrowExpression arrowExpression;

    public ListOppCall(Token identifier, Token operation, ArrowExpression arrowExpression) {
        this.identifier = (String) identifier.getValue();
        this.operation = operation.type;
        this.arrowExpression = arrowExpression;
    }

    @Override
    public String toString() {
        return "ListOppCall{" +
                "identifier='" + identifier + '\'' +
                ", operation=" + operation +
                ", arrowExpression=" + arrowExpression +
                '}';
    }

    public static void main(String[] args) throws Exception {
        IDataSource ds = new DataSourceString("(x ->  x * x )");
        Lexer lexer  = new Lexer(ds);
        Parser parser = new Parser(lexer);

        var expression = parser.parseArrowExpression();



    }
}
