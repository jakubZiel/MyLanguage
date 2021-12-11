package Parser;

import DataSource.DataSourceString;
import DataSource.IDataSource;
import ExceptionHandler.Exceptions.ParserException;
import Lexer.Token;
import Lexer.Lexer;
import static Lexer.TokenType.*;
import Lexer.TokenType;
import Parser.Model.Blocks.*;
import Parser.Model.Conditions.*;
import Parser.Model.Expressions.*;
import Parser.Model.Instructions.*;
import Parser.Model.Node;
import Parser.Model.Expressions.Type.*;
import Parser.Model.Nodes.*;
import Parser.Model.Statements.*;

import java.util.LinkedList;
import java.util.List;


public class Parser {
    private Token peeked;
    private final Lexer lexer;

    public Parser (Lexer lexer){
        this.lexer = lexer;
    }

    private Expression parseListDef() throws Exception {
        Token opening = getToken();
        if (peekToken().tokenIs(SQUARE_R)) {
            getToken();
            return new ListDef();
        }
        List<Expression> elements = new LinkedList<>();
        elements.add(parseExpression());
        while (peekToken().tokenIs(COMA)) {
            getToken();
            elements.add(parseExpression());
        }
        match(getToken(), SQUARE_R, "Unclosed array definition");
        return new ListDef(elements);
    }

    private ListT parseListType() throws Exception {
        Token type = new Token();
        return new ListT(checkListNesting(type), type.type);
    }

    private int checkListNesting(Token type) throws Exception {

        Token list = getToken();
        Token opening = getToken();
        Token closing;
        int nesting;
        if (peekToken().tokenIs(INT, DOUBLE)) {
            type.type = getToken().type;
            closing = getToken();
            return 1;
        } else
            nesting = checkListNesting(type) + 1;

        closing = getToken();
        return nesting;
    }

    public Program parseProgram() throws Exception {
        List<FunctionDeclaration> functions = new LinkedList<>();

        while(!peekToken().tokenIs(END_T)) {
            functions.add(parseFunctionDeclaration());
        }

        return new Program(functions);
    }

    private FunctionDeclaration parseFunctionDeclaration() throws Exception {
        Token token = peekToken();
        if (token.tokenIs(INT, DOUBLE, VOID)){
            getToken();
            match(peekToken(), IDENTIFIER, "Expected function name");
            Token identifier = getToken();
            Parameters parameters = parseParameters();
            Block body = parseBlock();
            return new FunctionDeclaration(token.type, (String) identifier.getValue(), parameters, body);
        } else
            throw new ParserException("Expected function type", token.position);
    }

    private Parameters parseParameters() throws Exception {
        Token opening = getToken();

        if (peekToken().tokenIs(PAREN_R)){
            getToken();
            return new Parameters();
        }
        Parameters parameters = new Parameters();
        parameters.addSignature(parseSignature());
        Token token;
        while ((token = peekToken()).type == COMA){
            getToken();
            parameters.addSignature(parseSignature());
        }
        match(getToken(), PAREN_R, "Unclosed function parameters");
        return parameters;
    }

    private Signature parseSignature() throws Exception {
        Token type = getToken();
        if (type.tokenIs(INT, DOUBLE, LIST)){
            match(peekToken(), IDENTIFIER, "Expected identifier");
            Token identifier = getToken();
            return new Signature(type.type, identifier);
        } else{
            return null;
        }
    }

    private Block parseBlock() throws Exception {
        Token opening = getToken();
        match(opening, CURLY_L, "Expected {");

        Block block = new Block();
        block.addInstruction(parseInstruction());
        match(getToken(), SEMICOLON, "Expected  ';' after instruction");

        while (!peekToken().tokenIs(CURLY_R) ){
            block.addInstruction(parseInstruction());
            match(getToken(), SEMICOLON, "Expected  ';' after instruction");
        }
        match(getToken(), CURLY_R, "Expected  }");
        return block;
    }

    private Instruction parseInstruction() throws Exception {
        Token token = peekToken();
        switch (token.type){
            case RETURN:
                return parseReturnInstr();
            case IDENTIFIER:
                return parseAssignInstr();
            case LIST:
                default:
                    if (token.tokenIs(INT, DOUBLE)){
                        return parseInitInstr();
                    } else
                        throw new ParserException("Expected instruction", token.position);
        }
    }

    private ListInitInstr parseListInitInstr() throws Exception {
        ListT type = parseListType();
        Token identifier = getToken();
        Token next = getToken();
        Expression initVal;

        if (next.tokenIs(ASSIGN))
            initVal = parseExpression();
        else if (next.tokenIs(SEMICOLON))
            initVal = null;
        else
            throw new ParserException("Unexpected token after init instruction", next.position);

        return new ListInitInstr(type, identifier, initVal);
    }

    private InitInstr parseInitInstr() throws Exception {
        Token type = getToken();
        Token identifier = getToken();
        Token next  = getToken();
        Expression initVal;

        if (next.tokenIs(ASSIGN))
            initVal = parseExpression();
        else if (next.tokenIs(SEMICOLON))
             initVal = null;
        else
            throw new ParserException("Unexpected token after init instruction", next.position);

        return new InitInstr(type.type, identifier, initVal);
    }

    private IfStatement parseIf(){
        //TODO
        return null;
    }

    private WhileStatement parseWhileStatement() throws Exception {

        Token whileToken = getToken();
        match(getToken(), PAREN_L, "Expected '(");
        Condition condition = parseCondition();
        match(getToken(), PAREN_R, "Expected )");

        Block body = parseBlock();
        return new WhileStatement(condition, body);
    }

    private ReturnInst parseReturnInstr() throws Exception {
        match(peekToken(), RETURN, "Expected 'return' ");
        getToken();
        return new ReturnInst(parseExpression());
    }

    private AssignInst parseAssignInstr() throws Exception{
        Token identifier = getToken();
        match(getToken(), ASSIGN, "Expected  assignment token");
        Expression assignedValue = parseExpression();

        return new AssignInst(identifier, assignedValue);
    }

    private Condition parseCondition() throws Exception {
        Condition condition = new Condition();

        condition.addOperand(parseAndCond());

        while (peekToken().type == OR){
            condition.addOperator(OR);
            getToken();
            condition.addOperand(parseAndCond());
        }
        return condition;
    }

    private Condition parseAndCond() throws Exception {
        Condition condition = new Condition();

        condition.addOperand(parseBaseCond());
        while (peekToken().type == AND){
            condition.addOperator(AND);
            getToken();
            condition.addOperand(parseBaseCond());
        }
        return condition;
    }

    private Condition parseBaseCond() throws Exception {
        Expression left = parseExpression();

        Token token = peekToken();
        if (token.tokenIs(EQUAL, N_EQUAL, GREATER_OR_EQUAL, LESS_OR_EQUAL, ANGLE_L, ANGLE_R)){
            Token operator = getToken();
            Expression right = parseExpression();
            return new Comparison(operator.type, left, right);
        } else
            throw new ParserException("Expected comparison operatar", token.position);
    }

    private Expression parseExpression() throws Exception {
        Expression expression = new Expression();
        Expression left = parseMultExpr();
        expression.addOperand(left);

        Token token;
        while ((token = peekToken()).tokenIs(ADD, SUBTRACT)){
            switch (token.type){
                case ADD:
                case SUBTRACT:
                    getToken();
                    expression.addOperator(token.type);
                    expression.addOperand(parseMultExpr());
                    break;
                default:
                    throw new ParserException("Expected + or -", token.position);
            }
        }
        if (expression.operands() == 1)
            return left;

        return expression;
    }

    private Expression parseMultExpr() throws Exception {
        Expression expression = new Expression();
        Expression left = parseBaseExpr();
        expression.addOperand(left);

        Token token;
        while ((token = peekToken()).tokenIs(MULTIPLY, DIVIDE, MODULO)){
            switch (token.type){
                case MULTIPLY:
                case DIVIDE:
                    getToken();
                    expression.addOperator(token.type);
                    expression.addOperand(parseBaseExpr());
                    break;
                default:
                    throw new ParserException("Expected %, /, *", token.position);
            }
        }
        if (expression.operands() == 1)
            return left;

        return expression;
    }

    private Expression parseBaseExpr() throws Exception {
        Expression expression = new Expression();
        Token token = peekToken();
        switch (token.type){
            case PAREN_L:
                getToken();
                Expression inner = parseExpression();
                token = getToken();
                match(token, PAREN_R, "Expected ) ");
                return inner;
            case IDENTIFIER:
                expression.addOperand(parseIdentified());
                return expression;
            case SQUARE_L:
                return parseListDef();
            default:
                return parseLiteral();
        }
    }

    private Node parseIdentified() throws Exception {
        Token identifier = getToken();
        Token nextToken = peekToken();
        switch (nextToken.type){
            case DOT:
            case SQUARE_L:
                getToken();
                Expression index = parseExpression();
                match(peekToken(), SQUARE_R, "Expected ]");
                getToken();
                return new ArrayCall(identifier, index);
            case PAREN_L:
                Arguments arguments = parseArguments();
                return new FunctionCall(identifier, arguments);
            default:
                return new Identifier(identifier);
        }
    }

    private FunctionCall parseFunctionCall() throws Exception {
        Token identifier = getToken();
        Arguments arguments = parseArguments();
        return new FunctionCall(identifier, arguments);
    }

    private Arguments parseArguments() throws Exception {
        Token opening = getToken();
        if (peekToken().tokenIs(PAREN_R)) {
            getToken();
            return new Arguments();
        }
        Arguments arguments = new Arguments();
        arguments.addArgument(parseExpression());
        while (peekToken().tokenIs(COMA)) {
            getToken();
            arguments.addArgument(parseExpression());
        }
        match(getToken(), PAREN_R, "Unclosed function call");
        return arguments;
    }

    private Literal parseLiteral() throws Exception {

        Token token = peekToken();
        switch (token.type){
            case ADD:
                getToken();
                return parseNumber(1);
            case SUBTRACT:
                getToken();
                return parseNumber(-1);
            case NUMBER_T:
                return parseNumber(1);
        }

        return new IntegerT((int) token.getValue());
    }

    private Literal parseNumber(int sign) throws Exception {
        Token token = getToken();

        if (token.getValue() instanceof Double)
            return new DoubleT((double) token.getValue() * sign);
        else if (token.getValue() instanceof Integer)
            return new IntegerT((int) token.getValue() * sign);
        else
            throw new ParserException("Expected number", token.position);
    }

    private Node parseString(){
        //TODO
        return new StringT();
    }

    private Token getToken() throws Exception {
        if (peeked != null){
            Token token = peeked;
            peeked = null;
            return  token;
        }
        return lexer.scanToken();
    }

    private Token peekToken() throws Exception {
        if (peeked == null){
            peeked = lexer.scanToken();
        }
        return peeked;
    }

    private void match(Token token, TokenType type, String errMsg) throws ParserException {
        if (token.type != type)
            throw new ParserException(errMsg, token.position);
    }

    public static void main(String[] args) throws Exception {
        String source = "((a * (a + 2)) * (2 + 3)) - (24) + 12)";
        String easyExpression = "(a + 3) * 2";
        String condition = "a + 3 < 3 || a > 0 && y < 10";
        String functionCall = "helloWorld(f, 2 + 3, f, [1, 2, 3, 4 * g, 5]) !";
        String initInst = "int a = (a + 34) * 2 ";
        String assignInst = "a = (1234 * 3) + f";
        String returnInst = "return a * 123 + 3";
        String blockOfInstructions = "{" +
                "int a = (a + 34) * 2;" +
                "a = (1234 * 3) + f;" +
                "return a * 123 + 3;" +
                "}";
        String whileStatement = "while(a < 12 && u == 2)" + blockOfInstructions;
        String parameters = "(int a, int b, int c, double f)";

        String functionDeclaration = "int helloWorld" + parameters + blockOfInstructions;
        String program = functionDeclaration + functionDeclaration + functionDeclaration;

        String parseListType = "list<list<list<int>>>";
        String parseListDef = "[1, 2, 3, 4 * g, 5]";
        String parseListInit = parseListType + "tab=" + parseListDef;

        IDataSource ds = new DataSourceString(functionCall);
        Lexer lexer = new Lexer(ds);
        Parser parser = new Parser(lexer);
        var cond = parser.parseFunctionCall();


        System.out.println(parser.getToken().type);

    }
}
