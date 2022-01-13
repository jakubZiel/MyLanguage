package Interpreter;

public class Variable {
    Object value;
    private final String identifier;

    public Variable(String identifier, Object value) {
        this.value = value;
        this.identifier = identifier;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getIdentifier() {
        return identifier;
    }
}
