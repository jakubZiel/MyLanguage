package Interpreter;

import Lexer.Lexer;
import Parser.Parser;
import org.junit.jupiter.api.Test;

class ExecuteVisitorTest {

    @Test
    void visitExpression() throws Exception {
        String data = "(123 / 12) + (3 / 32) * 3";
        Parser parser = new Parser(Lexer.lexerFactory(data));
        var expression = parser.parseExpression();

        ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
        var val = visitor.visit(expression);
    }
    @Test
    void visitProgram() throws Exception {
        String data = "double fun(int x, double f){ x = 3; f = 123.5; int i = 0; while(i < 10){i = i + 1; int a = 123; a = 245;}; return f + f;} int main(){int y = 123;  fun(0, 0.5); double z = 543.1; z = fun(123, 124.3); return z * 0.3;}";
        Parser parser = new Parser(Lexer.lexerFactory(data));

        var ASTree = parser.parseProgram();
        ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
        visitor.visit(ASTree);
    }

    @Test
    void visitListDef() throws Exception {
        String data = "int main(){ int x = 123; int z = 123; list<list<int>> array = [[1, x], [1, z] , [3, 4]]; return 0;}";
        Parser parser = new Parser(Lexer.lexerFactory(data));

        var ASTree = parser.parseProgram();
        ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
        visitor.visit(ASTree);
    }

    @Test
    void visitComparison() throws Exception{
        String data = "41 + 23 != (41 + 1) * 21";
        Parser parser = new Parser(Lexer.lexerFactory(data));
        var ASTree = parser.parseBaseCond();
        ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
        var result = visitor.visit(ASTree);
    }

    @Test
    void visitCondition() throws Exception{
        String data = "15 > 12 && 0 < 2";
        Parser parser = new Parser(Lexer.lexerFactory(data));
        var ASTree = parser.parseCondition();
        ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
        var result = visitor.visit(ASTree);
    }

    @Test
    void visitIfStatement() throws Exception {
        String data = "double fun(int x, double f)" +
                "{ " +
                "x = 3; " +
                "f = 123.5;" +
                " int i = 23;" +
                " if (i < 10)" +
                "{i = i + 1;" +
                " int a = 123;" +
                " a = 245;" +
                "} elseif(i > 20){i = i + 10;} else { i = i - 10;};"+
                " return f * f;" +
                "} " +
                "int main()" +
                "{int y = 123;" +
                " int z = 543;" +
                " fun(123, 124.0);" +
                " return z + y;}";

        Parser parser = new Parser(Lexer.lexerFactory(data));

        var ASTree = parser.parseProgram();
        ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
        visitor.visit(ASTree);
    }

    @Test
    void visitArrowExpression() throws Exception {
        String data =
                "string greetings(string name){"  +
                    "return \"greetings to \n\" + name;" +
                "}" +
                "int fib(int n){" +
                    "if (n == 0 || n == 1){"+
                    "   return n;" +
                    "};" +
                    "return fib(n - 1) + fib(n - 2);" +
                "}" +
                "int main()" +
                    "{" +
                    "print(greetings(\"jakub zielinski\n\n\"));"    +
                    "int y = 5;" +
                    "int z = 10;" +
                    "list<int> array = [12, y + 23, y * y, 123];" +
                    "list<int> array2 = [1 , 2 , 3, 4];" +
                    "list<int> array3 = array + array2;" +
                    "array3.foreach(u -> u / 2);" +
                    "array3.filter(t -> t > 12);" +
                    "int i = 0;" +
                    "while ( i < 5){" +
                        "int j = 0;" +
                        "while (j < 5){" +
                            "print(1, \" \");" +
                            "j = j + 1;" +
                        "};" +
                    "print();" +
                    "i = i + 1;" +
                    "};" +
                    "string hello = \"hello world\";" +
                    "string world = \"world world\";" +
                    "string helloworld = hello + world + hello + world; " +
                    "int cell = array[1];" +
                    "if (hello != world){" +
                        "helloworld = hello;" +
                        "print(\"different\n\");" +
                        "array3.add(2341);" +
                    "};" +
                    "array3[0] = 6969;" +
                    "array3.add(2022);" +
                    "array3.remove(2);" +
                    "print(array3);" +
                    "return fib(6) + fib(5) + fib(4);" +
                "}";

        Parser parser = new Parser(Lexer.lexerFactory(data));
        var ASTree = parser.parseProgram();
        ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
        visitor.visit(ASTree);
    }
}