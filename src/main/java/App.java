import DataSource.DataSourceIO;
import DataSource.IDataSource;
import ExceptionHandling.Exceptions.LanguageException;
import Interpreter.ExecuteVisitor;
import Interpreter.Scope;
import Lexer.Lexer;
import Parser.Model.Nodes.Program;
import Parser.Parser;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Wrong number of parameters, expected .list file path.");
            return;
        }
        try {
            IDataSource dataSource = new DataSourceIO(args[0]);
            Lexer lexer = new Lexer(dataSource);
            Parser parser = new Parser(lexer);
            ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
            Program program = parser.parseProgram();

            visitor.visit(program);
        }
        catch (LanguageException e){
            e.printErrorMessage();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
