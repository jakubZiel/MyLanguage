package Parser.Model.Instructions;

import ExceptionHandler.Exceptions.InterpreterException;
import Interpreter.Scope;
import Lexer.Token;
import Parser.Model.Expressions.Expression;

public class AssignInst extends Instruction{
    private String identifier;
    private Expression assingedValue;
    public AssignInst(Token identifier, Expression assingedValue) {
        this.assingedValue = assingedValue;
        this.identifier = (String) identifier.getValue();
    }

    @Override
    public String toString() {
        return "AssignInst{" +
                "identifier='" + identifier + '\'' +
                ", assingedValue=" + assingedValue +
                '}';
    }

    @Override
    public void execute(Scope scope) throws InterpreterException {
        if (!scope.setVariable(identifier, assingedValue.execute(scope)))
            throw new InterpreterException("Variable " + identifier + " doesn't exist in this context", null);
    }


}


