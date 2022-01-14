import DataSource.DataSourceIO;
import DataSource.IDataSource;
import Interpreter.ExecuteVisitor;
import Lexer.ExceptionHandler.Exceptions.InterpreterException;
import Lexer.ExceptionHandler.Exceptions.ParserException;
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
            ExecuteVisitor visitor = new ExecuteVisitor(null);
            Program program = parser.parseProgram();

            visitor.visit(program);
        }
        catch (InterpreterException e){
            System.err.println(e.message);
        } catch(ParserException e) {
            System.err.println(e.message);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
