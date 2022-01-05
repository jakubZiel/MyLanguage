package Parser;

import ExceptionHandler.Exceptions.ParserException;
import Lexer.Token;
import Lexer.Lexer;
import static Lexer.TokenType.*;
import Lexer.TokenType;
import Parser.Model.Blocks.*;
import Parser.Model.Conditions.*;
import Parser.Model.Expressions.*;
import Parser.Model.Instructions.*;
import Parser.Model.Expressions.Type.*;
import Parser.Model.Nodes.*;
import Parser.Model.Statements.*;
import java.util.LinkedList;
import java.util.List;

public class Parser {
    private final Lexer lexer;
    private final List<Token> peekedTokens;

    public Parser (Lexer lexer){
        this.lexer = lexer;
        this.peekedTokens = new LinkedList<>();
    }

    public Program parseProgram() throws Exception {
        List<FunctionDeclaration> functions = new LinkedList<>();

        while(!peekToken(0).tokenIs(END_T)) {
            functions.add(parseFunctionDeclaration());
        }
        return new Program(functions);
    }

    protected FunctionDeclaration parseFunctionDeclaration() throws Exception {
        Token token = peekToken(0);
        if (token.tokenIs(INT, DOUBLE, VOID)){
            getToken();
            match(peekToken(0), IDENTIFIER, "Expected function name");
            Token identifier = getToken();
            Parameters parameters = parseParameters();
            Block body = parseBlock();
            return new FunctionDeclaration(token.type, (String) identifier.getValue(), parameters, body);
        } else
            throw new ParserException("Expected function type", token.position);
    }

    protected Parameters parseParameters() throws Exception {
        Token opening = getToken();

        if (peekToken(0).tokenIs(PAREN_R)){
            getToken();
            return new Parameters();
        }
        Parameters parameters = new Parameters();
        parameters.addSignature(parseSignature());
        while (peekToken(0).type == COMA){
            getToken();
            parameters.addSignature(parseSignature());
        }
        match(getToken(), PAREN_R, "Unclosed function parameters");
        return parameters;
    }

    protected Signature parseSignature() throws Exception {
        Token type = getToken();
        if (type.tokenIs(INT, DOUBLE, LIST)){
            match(peekToken(0), IDENTIFIER, "Expected identifier");
            Token identifier = getToken();
            return new Signature(type.type, identifier);
        } else
            throw new ParserException("Expected type INT, DOUBLE, LIST", type.position);
    }

    protected Block parseBlock() throws Exception {
        Token opening = getToken();
        match(opening, CURLY_L, "Expected {");

        Block block = new Block();

        while (!peekToken(0).tokenIs(CURLY_R) ){
            addStatement(block);
            match(getToken(), SEMICOLON, "Expected  ';' after instruction");
        }
        match(getToken(), CURLY_R, "Expected  }");
        return block;
    }

    protected void addStatement(Block block) throws Exception {
        switch (peekToken(0).type){
            case IF:
                block.addInstruction(parseIfStatement());
                break;
            case WHILE:
                block.addInstruction(parseWhileStatement());
                break;
            default:
                block.addInstruction(parseInstruction());
        }
    }

    protected Instruction parseInstruction() throws Exception {
        Token token = peekToken(0);
        switch (token.type){
            case RETURN:
                return parseReturnInstr();
            case IDENTIFIER:
                return parseIdentifierInstr();
            case LIST:
                return parseListInitInstr();
            default:
                if (token.tokenIs(INT, DOUBLE)){
                    return parseInitInstr();
                } else
                    throw new ParserException("Expected instruction", token.position);
        }
    }

    protected ReturnInst parseReturnInstr() throws Exception {
        match(getToken(), RETURN, "Expected 'return' ");
        return new ReturnInst(parseExpression());
    }

    protected AssignInst parseAssignInstr() throws Exception{
        Token identifier = getToken();
        match(getToken(), ASSIGN, "Expected  assignment token");
        Expression assignedValue = parseExpression();

        return new AssignInst(identifier, assignedValue);
    }

    protected Instruction parseIdentifierInstr() throws Exception {
        Token nextToken = peekToken(1);

        if (nextToken.type == ASSIGN){
            return parseAssignInstr();
        }
        switch (nextToken.type){
            case DOT:
                return new CallInstr((ListOppCall) parseIdentified());
            case SQUARE_L:
                ArrayCall arrayCall = (ArrayCall) parseIdentified();
                match(getToken(), ASSIGN, "Expected = ");
                arrayCall.setAssignedValue(parseExpression());
                return new CallInstr(arrayCall);
            case PAREN_L:
                return new CallInstr((FunctionCall) parseIdentified());
            default:
                throw new ParserException("Wrong instruction", nextToken.position);
        }
    }

    protected ListInitInstr parseListInitInstr() throws Exception {
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

    protected InitInstr parseInitInstr() throws Exception {
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

    protected IfStatement parseIfStatement() throws Exception {
        Token ifToken = getToken();
        match(getToken(), PAREN_L, "Expected (");
        Condition condition = parseCondition();
        match(getToken(), PAREN_R, "Expected )");
        Block block = parseBlock();
        Block elseBlock = null;

        boolean elseFound = false;
        List<Condition> elifConditions = new LinkedList<>();
        List<Block> elifBlocks = new LinkedList<>();

        while (!elseFound && peekToken(0).tokenIs(ELSE, ELSEIF)){
            Token token = getToken();
            switch (token.type){
                case ELSEIF:
                    match(getToken(), PAREN_L, "Expected condition opening for elif");
                    elifConditions.add(parseCondition());
                    match(getToken(), PAREN_R, "Expected condition closing for elif" );
                    elifBlocks.add(parseBlock());
                    break;
                case ELSE:
                    elseBlock = parseBlock();
                    elseFound = true;
                    break;
                default:
                    throw new ParserException("if statement error", token.position);
            }
        }
        return new IfStatement(condition, block, elifConditions, elifBlocks, elseBlock);
    }

    protected WhileStatement parseWhileStatement() throws Exception {
        Token whileToken = getToken();
        match(getToken(), PAREN_L, "Expected '(");
        Condition condition = parseCondition();
        match(getToken(), PAREN_R, "Expected )");

        Block body = parseBlock();
        return new WhileStatement(condition, body);
    }

    public Condition parseCondition() throws Exception {
        Condition condition = new Condition();
        Condition left = parseAndCond();
        condition.addOperand(left);

        while (peekToken(0).type == OR){
            condition.addOperator(OR);
            getToken();
            condition.addOperand(parseAndCond());
        }

        if (condition.conditions() == 1)
            return left;

        return condition;
    }

    protected Condition parseAndCond() throws Exception {
        Condition condition = new Condition();
        Condition left = parseBaseCond();
        condition.addOperand(left);

        while (peekToken(0).type == AND){
            condition.addOperator(AND);
            getToken();
            condition.addOperand(parseBaseCond());
        }

        if (condition.conditions() == 1)
            return left;

        return condition;
    }

    public Condition parseBaseCond() throws Exception {
        Expression left = parseExpression();

        Token token = peekToken(0)
;
        if (token.tokenIs(EQUAL, N_EQUAL, GREATER_OR_EQUAL, LESS_OR_EQUAL, ANGLE_L, ANGLE_R)){
            Token operator = getToken();
            Expression right = parseExpression();
            return new Comparison(operator.type, left, right);
        } else
            throw new ParserException("Expected comparison operatar", token.position);
    }

    public Expression parseExpression() throws Exception {
        Expression expression = new Expression();
        Expression left = parseMultExpr();
        expression.addOperand(left);

        Token token;
        while ((token = peekToken(0)).tokenIs(ADD, SUBTRACT)){
            getToken();
            expression.addOperator(token.type);
            expression.addOperand(parseMultExpr());
        }
        if (expression.operands() == 1)
            return left;

        return expression;
    }

    protected Expression parseMultExpr() throws Exception {
        Expression expression = new Expression();
        Expression left = parseBaseExpr();
        expression.addOperand(left);

        Token token;
        while ((token = peekToken(0)).tokenIs(MULTIPLY, DIVIDE, MODULO)){
            getToken();
            expression.addOperator(token.type);
            expression.addOperand(parseBaseExpr());
        }
        if (expression.operands() == 1)
            return left;
        return expression;
    }

    protected Expression parseBaseExpr() throws Exception {
        Token token = peekToken(0);
        switch (token.type){
            case PAREN_L:
                getToken();
                Expression inner = parseExpression();
                token = getToken();
                match(token, PAREN_R, "Expected ) ");
                return inner;
            case IDENTIFIER:
                return parseIdentified();
            case SQUARE_L:
                return parseListDef();
            default:
                return parseLiteral();
        }
    }

    public Expression parseListDef() throws Exception {
        Token opening = getToken();
        if (peekToken(0).tokenIs(SQUARE_R)) {
            getToken();
            return new ListDef();
        }
        List<Expression> elements = new LinkedList<>();
        elements.add(parseExpression());
        while (peekToken(0).tokenIs(COMA)) {
            getToken();
            elements.add(parseExpression());
        }
        match(getToken(), SQUARE_R, "Unclosed array definition");
        return new ListDef(elements);
    }

    protected ListT parseListType() throws Exception {
        Token type = new Token();
        return new ListT(checkListNesting(type) - 1, type.type);
    }

    protected int checkListNesting(Token type) throws Exception {
        match(getToken(), LIST, "list type expected");
        match(getToken(), ANGLE_L, "list type opening expected");

        int nesting;
        if (peekToken(0).tokenIs(INT, DOUBLE)) {
            type.type = getToken().type;
            match(getToken(), ANGLE_R,"Unclosed list type");
            return 1;
        } else
            nesting = checkListNesting(type) + 1;

        match(getToken(), ANGLE_R, "Unclosed list type");
        return nesting;
    }

    protected Expression parseIdentified() throws Exception {
        Token identifier = getToken();
        Token nextToken = peekToken(0);
        switch (nextToken.type){
            case DOT:
                return parseListOpp(identifier);
            case SQUARE_L:
                getToken();
                Expression index = parseExpression();
                match(peekToken(0), SQUARE_R, "Expected ]");
                getToken();
                return new ArrayCall(identifier, index);
            case PAREN_L:
                return parseFunctionCall(identifier);
            default:
                return new Identifier(identifier);
        }
    }

    protected ListOppCall parseListOpp(Token identifier) throws Exception {
        Token dot = getToken();
        Token operation = getToken();

        if (operation.tokenIs(FOREACH, ADD_LIST, REMOVE_LIST)){
            return new ListOppCall(identifier, operation, parseArrowExpression());
        } else if (operation.tokenIs(FILTER))
            return new ListOppCall(identifier, operation, parseArrowPredicate());

        throw new ParserException("Expected function operation", operation.position);
    }

    protected ArrowExpression parseArrowExpression() throws Exception {
        match(getToken(), PAREN_L, "Expected (");
        Token identifier = getToken();
        match(getToken(), ARROW, "Expected '->'");
        Expression expression = parseExpression();
        match(getToken(), PAREN_R, "Expected )");
        return new ArrowExpression(identifier, expression);
    }

    protected ArrowExpression parseArrowPredicate() throws Exception{
        match(getToken(), PAREN_L, "Expected (");
        Token identifier = getToken();
        match(getToken(), ARROW, "Expected '->'");
        Condition condition = parseCondition();
        match(getToken(), PAREN_R, "Expected (");
        return new ArrowExpression(identifier, condition);
    }

    protected FunctionCall parseFunctionCall(Token identifier) throws Exception {
        Arguments arguments = parseArguments();
        return new FunctionCall((String) identifier.getValue(), arguments);
    }

    protected Arguments parseArguments() throws Exception {
        Token opening = getToken();
        if (peekToken(0).tokenIs(PAREN_R)) {
            getToken();
            return new Arguments();
        }
        Arguments arguments = new Arguments();
        arguments.addArgument(parseExpression());
        while (peekToken(0).tokenIs(COMA)) {
            getToken();
            arguments.addArgument(parseExpression());
        }
        match(getToken(), PAREN_R, "Unclosed function call");
        return arguments;
    }

    protected Literal parseLiteral() throws Exception {
        Token token = peekToken(0);
        switch (token.type){
            case ADD:
                getToken();
                return parseNumber(1);
            case SUBTRACT:
                getToken();
                return parseNumber(-1);
            case NUMBER_T:
                return parseNumber(1);
            case STRING_T:
                return parseString();
        }
        return new IntegerT((int) token.getValue());
    }

    protected Literal parseNumber(int sign) throws Exception {
        Token token = getToken();

        if (token.getValue() instanceof Double)
            return new DoubleT((double) token.getValue() * sign);
        else if (token.getValue() instanceof Integer)
            return new IntegerT((int) token.getValue() * sign);
        else
            throw new ParserException("Expected number", token.position);
    }

    protected Literal parseString() throws Exception {
        Token string = getToken();
        return new StringT(string);
    }

    protected Token peekToken(int offset) throws Exception {
        if (offset <= peekedTokens.size() - 1)
            return peekedTokens.get(offset);

        while (offset != peekedTokens.size() - 1){
            peekedTokens.add(lexer.scanToken());
        }
        return peekedTokens.get(offset);
    }

    protected Token getToken() throws Exception {
        if (peekedTokens.size() > 0)
            return peekedTokens.remove(0);
        return lexer.scanToken();
    }

    protected void match(Token token, TokenType type, String errMsg) throws ParserException {
        if (token.type != type)
            throw new ParserException(errMsg, token.position);
    }
}
