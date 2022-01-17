package Interpreter;

import Lexer.Lexer;
import Parser.Model.Expressions.Literal;
import Parser.Parser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExecuteVisitorTest {

    public <T>Literal<T> executeProgram(String program) throws Exception {
        Parser parser = new Parser(Lexer.lexerFactory(program));
        var ASTree = parser.parseProgram();
        ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
        return  visitor.visit(ASTree);
    }

    @Test
    void visitExpressionSumInt() throws Exception {
        String data = "1 + 2 + 3";
        Parser parser = new Parser(Lexer.lexerFactory(data));
        var expression = parser.parseExpression();

        ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
        assertEquals(6, visitor.visit(expression).val);
    }

    @Test
    void visitExpressionMultiplyInt() throws Exception {
        String data = "1 * 2 * 3";
        Parser parser = new Parser(Lexer.lexerFactory(data));
        var expression = parser.parseExpression();

        ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
        assertEquals(6, visitor.visit(expression).val);
    }

    @Test
    void visitExpressionComplexInt() throws Exception {
        String data = "(123 / 12) * (3 + 32) * 1";
        Parser parser = new Parser(Lexer.lexerFactory(data));
        var expression = parser.parseExpression();

        ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
        assertEquals(350, visitor.visit(expression).val);
    }


    @Test
    void visitExpressionSumDouble() throws Exception {
        String data = "1.5 + 2.5 + 3.5";
        Parser parser = new Parser(Lexer.lexerFactory(data));
        var expression = parser.parseExpression();

        ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
        assertEquals(7.5, visitor.visit(expression).val );
    }

    @Test
    void visitExpressionMultiplyDouble() throws Exception {
        String data = "1.0 * 2.0 * 3.0";
        Parser parser = new Parser(Lexer.lexerFactory(data));
        var expression = parser.parseExpression();

        ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
        assertEquals(6.0, visitor.visit(expression).val);
    }

    @Test
    void visitExpressionComplexDouble() throws Exception {
        String data = "(15.0 / 2.0) * (3.0 + 32.0) * 1.0";
        Parser parser = new Parser(Lexer.lexerFactory(data));
        var expression = parser.parseExpression();

        ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
        assertEquals(262.5, visitor.visit(expression).val);
    }

    @Test
    void visitListDefInt() throws Exception {
        String data = "[2, 0, 5, 12]";
        Parser parser = new Parser(Lexer.lexerFactory(data));

        var ASTree = parser.parseListDef();
        ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
        var res = ASTree.accept(visitor);
        assertEquals("[2, 0, 5, 12]", res.val.toString());
    }

    @Test
    void visitListDefIntComplex() throws Exception {
        String data = "[2 * 2 + 3, 1 / 3, 5 + 2, 12 * 1 * (1  + 2)]";
        Parser parser = new Parser(Lexer.lexerFactory(data));

        var ASTree = parser.parseListDef();
        ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
        var res = ASTree.accept(visitor);
        assertEquals("[7, 0, 7, 36]", res.val.toString());
    }

    @Test
    void visitListDefString() throws Exception {
        String data = "[\"hello\", \"world\"]";
        Parser parser = new Parser(Lexer.lexerFactory(data));

        var ASTree = parser.parseListDef();
        ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
        var res = ASTree.accept(visitor);
        assertEquals("[hello, world]", res.val.toString());
    }

    @Test
    void visitListDefDouble() throws Exception {
        String data = "[0.3, 0.2, 0.1]";
        Parser parser = new Parser(Lexer.lexerFactory(data));

        var ASTree = parser.parseListDef();
        ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
        var res = ASTree.accept(visitor);
        assertEquals("[0.3, 0.2, 0.1]", res.val.toString());
    }

    @Test
    void visitListSum() throws Exception {
        String data = "[1, 2, 3] + [4, 5, 6] + [7, 8, 9]";
        Parser parser = new Parser(Lexer.lexerFactory(data));

        var ASTree = parser.parseExpression();
        ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
        var res = ASTree.accept(visitor);
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9]", res.val.toString());
    }

    @Test
    void visitListAdd() throws Exception {
        String program = "int main(){" +
                "list<int> array = [0];" +
                "array.add(1);" +
                "array.add(2);" +
                "return array;" +
                "}";
        var result = executeProgram(program);

        assertEquals(result.val.toString(), "[0, 1, 2]");
    }

    @Test
    void visitListRemove() throws Exception {
        String program = "int main(){" +
                "list<int> array = [0, 1, 2, 3];" +
                "array.remove(0);" +
                "array.remove(0);" +
                "return array;" +
                "}";
        var result = executeProgram(program);

        assertEquals(result.val.toString(), "[2, 3]");
    }

    @Test
    void visitListFilter() throws Exception {
        String program = "int main(){" +
                "list<int> array = [0, 1, 2, 3, 4, 5];" +
                "array.filter(x -> x > 3);" +
                "return array;" +
                "}";
        var result = executeProgram(program);

        assertEquals(result.val.toString(), "[4, 5]");
    }

    @Test
    void visitListFilterString() throws Exception {
        String program = "string main(){" +
                "list<string> array = [\"aaa\", \"bbb\", \"aaa\"];" +
                "array.filter(x -> x == \"aaa\");" +
                "return array;" +
                "}";
        var result = executeProgram(program);

        assertEquals(result.val.toString(), "[aaa, aaa]");
    }

    @Test
    void visitListForeach() throws Exception {
        String program = "int main(){" +
                "list<int> array = [0, 1, 2, 3];" +
                "array.foreach(y -> y * y * y);" +
                "return array;" +
                "}";
        var result = executeProgram(program);

        assertEquals(result.val.toString(), "[0, 1, 8, 27]");
    }

    @Test
    void visitListForeachString() throws Exception {
        String program = "string main(){" +
                "list<string> array = [\"aaa\", \"bbb\", \"ccc\"];" +
                "array.foreach(x -> x + \"xxx\");" +
                "return array;" +
                "}";
        var result = executeProgram(program);

        assertEquals(result.val.toString(), "[aaaxxx, bbbxxx, cccxxx]");
    }

    @Test
    void visitListIndexAssignment() throws Exception {
        String program = "int main(){" +
                "list<int> array = [0, 1, 2];" +
                "array[0] = 2;" +
                "array[1] = 1;" +
                "array[2] = 0;" +
                "return array;" +
                "}";
        var result = executeProgram(program);

        assertEquals(result.val.toString(), "[2, 1, 0]");
    }

    @Test
    void visitListIndexing() throws Exception {
        String program = "int main(){" +
                "list<int> array = [0, 1, 2];" +
                "return array[0] + array[1] + array[2];" +
                "}";
        var result = executeProgram(program);

        assertEquals(result.val.toString(),  "3");
    }

    @Test
    void visitInequality() throws Exception{
        String data = "41 + 23 != (41 + 1) * 21";
        Parser parser = new Parser(Lexer.lexerFactory(data));
        var ASTree = parser.parseBaseCond();
        ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
        assertTrue(visitor.visit(ASTree));
    }

    @Test
    void visitEquality() throws Exception{
        String data = "(41 + 23) * 3 == (41 + 1) * 21";
        Parser parser = new Parser(Lexer.lexerFactory(data));
        var ASTree = parser.parseBaseCond();
        ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
        assertFalse(visitor.visit(ASTree));
    }

    @Test
    void visitComparison() throws Exception{
        String data = "\"aaaa\" < \"bbbb\"";
        Parser parser = new Parser(Lexer.lexerFactory(data));
        var ASTree = parser.parseBaseCond();
        ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
        assertTrue(visitor.visit(ASTree));
    }

    @Test
    void visitAndCondition() throws Exception{
        String data = "15 > 12 && 0 < 2";
        Parser parser = new Parser(Lexer.lexerFactory(data));
        var ASTree = parser.parseCondition();
        ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
        assertTrue(visitor.visit(ASTree));
    }

    @Test
    void visitOrCondition() throws Exception{
        String data = "15 > 12 || 0 > 2";
        Parser parser = new Parser(Lexer.lexerFactory(data));
        var ASTree = parser.parseCondition();
        ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
        assertTrue(visitor.visit(ASTree));
    }

    @Test
    void visitCondition() throws Exception{
        String data = "15 > 12 && 0 < 2 || 200 > 199";
        Parser parser = new Parser(Lexer.lexerFactory(data));
        var ASTree = parser.parseCondition();
        ExecuteVisitor visitor = new ExecuteVisitor(new Scope(null));
        assertTrue(visitor.visit(ASTree));
    }

    @Test
    void visitIfStatementFirstBlock() throws Exception {
        String program =
                "int main(){" +
                    "if (0 < 1){" +
                        "return 0;" +
                        "};" +
                    "return -1;" +
                "}";

        assertEquals("0",  executeProgram(program).val.toString());
    }

    @Test
    void visitIfStatementElseBlock() throws Exception {
        String program =
                "int main(){" +
                        "if (1 < 0){" +
                            "return -1;" +
                        "}else{" +
                            "return 0;" +
                        "};" +
                        "return -1;" +
                        "}";

        assertEquals("0",  executeProgram(program).val.toString());
    }

    @Test
    void visitIfStatementElseIfBlock() throws Exception {
        String program =
                "int main(){" +
                        "if (1 < 0){" +
                            "return -1;" +
                        "}elseif(0 < 1){" +
                            "return 0;" +
                        "};" +
                        "return -1;" +
                        "}";
        assertEquals("0",  executeProgram(program).val.toString());
    }

    @Test
    void visitWhileStatement() throws Exception {
        String program =
                "int main(){" +
                            "int i = 0;" +
                            "while(i < 10){" +
                                "i = i + 1;" +
                            "};" +
                            "return i;" +
                        "}";
        assertEquals("10",  executeProgram(program).val.toString());
    }

    @Test
    void visitNestedWhileStatement() throws Exception {
        String program =
                "int main(){" +
                        "int counter = 0;" +
                        "int i = 0;" +
                        "while(i < 10){" +
                            "int j = 0;" +
                            "while(j < 10){" +
                                    "counter = counter + 1;" +
                                    "j = j + 1;"+
                                "};" +
                            "i = i + 1;" +
                        "};" +
                        "return counter;" +
                        "}";
        assertEquals("100",  executeProgram(program).val.toString());
    }

    @Test
    void visitInitInstruction() throws Exception {
        String program =
                "int main(){" +
                            "int i = 10;" +
                            "return i;" +
                        "}";

        assertEquals("10",  executeProgram(program).val.toString());
    }

    @Test
    void visitAssignInstruction() throws Exception {
        String program =
                "int main(){" +
                            "int i = 10;" +
                            "i = 14;" +
                            "return  i;" +
                        "}";

        assertEquals("14",  executeProgram(program).val.toString());
    }

    @Test
    void visitReturnInstruction() throws Exception {
        String program =
                "int main(){" +
                            "return 0;" +
                        "}";

        assertEquals("0",  executeProgram(program).val.toString());
    }

        @Test
    void visitFunctionDeclaration()  throws Exception {
        String program =
                "string someString(){" +
                            "return \"word\";" +
                        "}" +
                "int fun(){" +
                        "return 0;" +
                        "}" +
                "void hello(){" +
                        "}" +
                "int main(){" +
                        "return 0;" +
                        "}";

        assertEquals("0",  executeProgram(program).val.toString());
    }

    @Test
    void visitFunctionCallInt() throws Exception {
        String program =
                "int sum(int a, int b){" +
                            "return a + b;" +
                        "}" +
                "int main(){" +
                        "int result = sum(10, 15);" +
                            "return result;" +
                        "}";

        assertEquals("25",  executeProgram(program).val.toString());
    }

    @Test
    void visitFunctionCallDouble() throws Exception {
        String program =
                "double sum(){" +
                            "return 0.53;" +
                        "}" +
                        "double main(){" +
                            "return sum();" +
                        "}";

        assertEquals("0.53",  executeProgram(program).val.toString());
    }

    @Test
    void visitFunctionCallString() throws Exception {
        String program =
                "string sum(string a){" +
                           "return a + a;" +
                        "}" +
                        "string main(){" +
                            "return sum(\"hello \");" +
                        "}";

        assertEquals("hello hello ",  executeProgram(program).val.toString());
    }

    @Test
    void visitRecursiveFunctionCall() throws Exception {
        String program =
                "int fib(int n){" +
                        "if (n == 0 || n == 1){"+
                        "   return n;" +
                        "};" +
                            "return fib(n - 1) + fib(n - 2);" +
                        "}" +
                "int main(){" +
                        "return fib(5);" +
                        "}";

        assertEquals("5",  executeProgram(program).val.toString());
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