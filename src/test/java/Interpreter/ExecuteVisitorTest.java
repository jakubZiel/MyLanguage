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
        String data = "double fun(int x, double f){ x = 3; f = 123.5; int i = 0; while(i < 10){i = i + 1; int a = 123; a = 245;}; return x + f;} int main(){int y = 123; int z = 543; fun(123, 124); return z + y;}";
        String expected = "Program{functions=[FunctionDeclaration{returnedType=VOID, identifier='fun', parameters=Parameters{signatures=[Signature{type=INT, identifier='x'}, Signature{type=DOUBLE, identifier='f'}]}, body=Block{instructions=[]}}, FunctionDeclaration{returnedType=INT, identifier='main', parameters=Parameters{signatures=[]}, body=Block{instructions=[ReturnInst{returned=DoubleT{val=0.0}}]}}]}";
        Parser parser = new Parser(Lexer.lexerFactory(data));

        var ASTree = parser.parseProgram();
        ExecuteVisitor visitor = new ExecuteVisitor(null);
        visitor.visit(ASTree);
    }

    @Test
    void visitComparison() throws Exception{
        String data = "41 + 23 != (41 + 1) * 21";
        Parser parser = new Parser(Lexer.lexerFactory(data));
        var ASTree = parser.parseBaseCond();
        ExecuteVisitor visitor = new ExecuteVisitor(null);
        var result = visitor.visit(ASTree);

    }

    @Test
    void visitCondition() throws Exception{
        String data = "15 > 12 && 0 < 2";
        Parser parser = new Parser(Lexer.lexerFactory(data));
        var ASTree = parser.parseCondition();
        ExecuteVisitor visitor = new ExecuteVisitor(null);
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
                " return x + f;" +
                "} " +
                "int main()" +
                "{int y = 123;" +
                " int z = 543;" +
                " fun(123, 124);" +
                " return z + y;}";
        String expected = "Program{functions=[FunctionDeclaration{returnedType=VOID, identifier='fun', parameters=Parameters{signatures=[Signature{type=INT, identifier='x'}, Signature{type=DOUBLE, identifier='f'}]}, body=Block{instructions=[]}}, FunctionDeclaration{returnedType=INT, identifier='main', parameters=Parameters{signatures=[]}, body=Block{instructions=[ReturnInst{returned=DoubleT{val=0.0}}]}}]}";
        Parser parser = new Parser(Lexer.lexerFactory(data));

        var ASTree = parser.parseProgram();
        ExecuteVisitor visitor = new ExecuteVisitor(null);
        visitor.visit(ASTree);
    }

    @Test
    void test() throws Exception {
        String data = "(123 / 12) + (3 / 32) * 3";
        Parser parser = new Parser(Lexer.lexerFactory(data));
        var expression = parser.parseExpression();

        ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
        expression.accept(visitor);
    }
}