package Parser.Model.Instructions;

import Lexer.Token;
import Parser.Model.Expressions.Expression;

public class AssignInst extends Instruction{
    private String identifier;
    private Expression assingedValue;
    public AssignInst(Token identifier, Expression assingedValue) {
        this.assingedValue = assingedValue;
        this.identifier = (String) identifier.getValue();
    }
}


