package Parser.Model.Expressions;

import Lexer.Token;

public class ArrayCall extends Expression {
    String identifier;
    Expression index;
    Expression assignedValue;

    public ArrayCall(Token token, Expression index) {
        this.identifier = (String) token.getValue();
        this.index = index;
    }

    public void setAssignedValue(Expression assignedValue) {
        this.assignedValue = assignedValue;
    }

    @Override
    public String toString() {
        return "ArrayCall{" +
                "identifier='" + identifier + '\'' +
                ", index=" + index +
                '}';
    }

    public String getIdentifier() {
        return identifier;
    }

    public Expression getIndex() {
        return index;
    }

    public Expression getAssignedValue() {
        return assignedValue;
    }
}
