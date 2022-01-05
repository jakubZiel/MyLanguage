package Parser.Model.Instructions;

import ExceptionHandler.Exceptions.InterpreterException;
import Interpreter.Visitor;
import Parser.Model.Expressions.ArrayCall;
import Parser.Model.Expressions.FunctionCall;
import Parser.Model.Expressions.ListOppCall;
import Parser.Model.Expressions.Literal;

public class CallInstr extends Instruction{
    private FunctionCall functionCall;
    private ListOppCall listOppCall;
    private ArrayCall arrayCall;

    public CallInstr(FunctionCall functionCall) {
        this.functionCall = functionCall;
    }

    public CallInstr(ListOppCall listOppCall) {
        this.listOppCall = listOppCall;
    }

    public CallInstr(ArrayCall arrayCall){
        this.arrayCall = arrayCall;
    }

    public FunctionCall getFunctionCall() {
        return functionCall;
    }

    public ListOppCall getListOppCall() {
        return listOppCall;
    }

    public ArrayCall getArrayCall() {
        return arrayCall;
    }

    @Override
    public <T> Literal<T> accept(Visitor visitor) throws InterpreterException {
         return visitor.visit(this);
    }
}
