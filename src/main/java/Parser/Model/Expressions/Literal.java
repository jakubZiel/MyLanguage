package Parser.Model.Expressions;


import ExceptionHandler.Exceptions.InterpreterException;
import Interpreter.Scope;
import Interpreter.Visited;
import Interpreter.Visitor;

public class Literal<T> extends Expression implements Visited {
    public T val;

    public Literal(T val) {
        this.val = val;
    }

    public Literal(){
    }

    @Override
    public Literal<T> accept(Visitor visitor) throws InterpreterException {
        return visitor.visit(this);
    }

    public Literal<T> add(Literal<T> operand){return null;}
    public Literal<T> subtract(Literal<T> operand) throws InterpreterException{return null;}
    public Literal<T> multiply(Literal<T> operand) throws InterpreterException{return null;}
    public Literal<T> divide(Literal<T> operand) throws InterpreterException{return null;}
    public Literal<T> modulo(Literal<T> operand) throws InterpreterException{return null;}

    public int compare(Literal<T> operand){
        Comparable<T> value = (Comparable<T>) val;
        return value.compareTo(operand.val);
    }

    public boolean less(Literal<T> operand){
        return compare(operand) == -1;
    }

    public boolean lessEqual(Literal<T> operand){
        return compare(operand) <= 0;
    }

    public boolean more(Literal<T> operand){
        return compare(operand) == 1;
    }

    public boolean moreEqual(Literal<T> operand){
        return compare(operand) >= 0;
    }

    public boolean equal(Literal<T> operand){
        return compare(operand) == 0;
    }

    public boolean notEqual(Literal<T> operand){
        return compare(operand) != 0;
    }
}
