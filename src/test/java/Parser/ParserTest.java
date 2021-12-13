package Parser;


import Lexer.Lexer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    @Test
    void parseProgram() throws Exception {
        String data = "void fun(int x, double f){} int main(){return 0;}";
        String expected = "Program{functions=[FunctionDeclaration{returnedType=VOID, identifier='fun', parameters=Parameters{signatures=[Signature{type=INT, identifier='x'}, Signature{type=DOUBLE, identifier='f'}]}, body=Block{instructions=[]}}, FunctionDeclaration{returnedType=INT, identifier='main', parameters=Parameters{signatures=[]}, body=Block{instructions=[ReturnInst{returned=DoubleT{val=0.0}}]}}]}";
        Parser parser = new Parser(Lexer.lexerFactory(data));

        var ASTree = parser.parseProgram().toString();
        assertEquals(expected, ASTree);
    }

    @Test
    void parseFunctionDeclarationFail() {
        String data = "int helloWorld(int a, int b, int c, f){}";
        Parser parser = new Parser(Lexer.lexerFactory(data));

        assertThrows(Exception.class, parser::parseFunctionDeclaration);
    }

        @Test
    void parseFunctionDeclaration() throws Exception {
        String data = "int helloWorld(int a, int b, int c, double f){}";
        String expected = "FunctionDeclaration{returnedType=INT, identifier='helloWorld', parameters=Parameters{signatures=[Signature{type=INT, identifier='a'}, Signature{type=INT, identifier='b'}, Signature{type=INT, identifier='c'}, Signature{type=DOUBLE, identifier='f'}]}, body=Block{instructions=[]}}";
        Parser parser = new Parser(Lexer.lexerFactory(data));

        var ASTree = parser.parseFunctionDeclaration();
        assertEquals(expected, ASTree.toString());
    }

    @Test
    void parseIfStatement() throws Exception {
        String data = "if(a < 3 && a > 2 || u < 10){ int b = 123;} elseif(a < 3){ if(a > 23){double u = 123;}; }elseif(b < 3){int u = 123;}else {int k = 123;}";
        Parser parser = new Parser(Lexer.lexerFactory(data));

        var ASTree = parser.parseIfStatement();
    }

    @Test
    void parseWhileStatement() throws Exception {
        String ifStatement = "if(a < 3 && a > 2 || u < 10){ int b = 123;} elseif(a < 3){ if(a > 23){double u = 123;}; }elseif(b < 3){int u = 123;}else {int k = 123;}";
        String parseListType = "list<list<list<int>>>";
        String parseListDef = "[1, 2, 3, 4 * g, 5]";
        String parseListInit = parseListType + "tab=" + parseListDef;
        String blockOfInstructions = "{" +
                "int a = (a + 34) * 2;" +
                "a = (1234 * 3) + f;" +
                ifStatement + ";" +
                parseListInit + ";" +
                "return a * 123 + 3;" +
                "}";
        String data = "while(a < 12 && u == 2)" + blockOfInstructions;
        Parser parser = new Parser(Lexer.lexerFactory(data));

        var ASTree = parser.parseWhileStatement();
    }

    @Test
    void parseConditionFail(){
        String data = "a < 12 && u == 2 || a != 3 ||";
        Parser parser = new Parser(Lexer.lexerFactory(data));

        assertThrows(Exception.class, parser::parseCondition);
    }

    @Test
    void parseCondition() throws Exception {
        String data = "a < 12 && u == 2 || a != 3";
        Parser parser = new Parser(Lexer.lexerFactory(data));

        parser.parseCondition();
    }

    @Test
    void parseExpressionFail(){
        String data = "((a + 3) + (3 + k) * 3";
        Parser parser = new Parser(Lexer.lexerFactory(data));

        assertThrows(Exception.class, parser::parseExpression);
    }

    @Test
    void parseExpression() throws Exception {
        String data = "(a + 3) + (3 + k) * 3";
        Parser parser = new Parser(Lexer.lexerFactory(data));
        parser.parseExpression();
    }

    @Test
    void parseListDefFail() {
        String data = "[1, 2,]";
        Parser parser = new Parser(Lexer.lexerFactory(data));

        assertThrows(Exception.class, parser::parseListDef);
    }

    @Test
    void parseListDef() throws Exception {
        String data = "[1, 2, 3, 4, f * f]";
        Parser parser = new Parser(Lexer.lexerFactory(data));

        parser.parseListDef();
    }

    @Test
    void parseListTypeFail() {
        String data = "list<list<list<int>>";
        Parser parser = new Parser(Lexer.lexerFactory(data));

        assertThrows(Exception.class, parser::parseListType);
    }

    @Test
    void parseListType() throws Exception {
        String data = "list<list<list<int>>>";
        Parser parser = new Parser(Lexer.lexerFactory(data));

        parser.parseListType();
    }

    @Test
    void parseListOpp() throws Exception {
        String data = "tab.foreach(x -> x * x + 3)";
        Parser parser = new Parser(Lexer.lexerFactory(data));

        parser.parseIdentified();
    }

    @Test
    void parseArrowExpression() throws Exception {
        String data = "(x -> x * x + 3)";
        Parser parser = new Parser(Lexer.lexerFactory(data));
        parser.parseArrowExpression();
    }

    @Test
    void parseArrowPredicate() throws Exception {
        String data = "(y -> y * x < 10 && y > 2)";
        Parser parser = new Parser(Lexer.lexerFactory(data));

        parser.parseArrowPredicate();
    }

    @Test
    void parseArrowPredicateFail() {
        String data = "(y -> y * x < 10 && y > 2";
        Parser parser = new Parser(Lexer.lexerFactory(data));

        assertThrows(Exception.class, parser::parseArrowPredicate);
    }

    @Test
    void parseFunctionCall() throws Exception {
        String data = "hello(a, 12, 3)";
        Parser parser = new Parser(Lexer.lexerFactory(data));
        var ASTree = parser.parseIdentified();
    }

    @Test
    void parseFunctionCallFail() {
        String data = "hello(a, 3,)";
        Parser parser = new Parser(Lexer.lexerFactory(data));

        assertThrows(Exception.class, parser::parseIdentified);
    }
}