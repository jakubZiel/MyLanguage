package Interpreter;

import ExceptionHandler.Exceptions.InterpreterException;
import Parser.Model.Blocks.FunctionDeclaration;
import Parser.Model.Expressions.Expression;
import Parser.Model.Expressions.ListDef;
import Parser.Model.Nodes.Program;

import java.util.HashMap;

public class Scope {
    //TODO should be removed from global scope. Functions can be attached to every ExecuteVisitor as a reference field
    public static HashMap<String, FunctionDeclaration> functions = new HashMap<>();

    Scope parent;
    HashMap<String, Variable> variables = new HashMap<>();

    public Scope(Scope parent){
        this.parent = parent;
    }

    public static void setFunctions(Program program){
        for (var function : program.getFunctions())
            functions.put(function.getIdentifier(), function);
    }

    public boolean contains(String identifier){
        Scope current = this;
        while (current != null) {
            if (current.variables.containsKey(identifier))
                return true;
            current = current.parent;
        }
        return false;
    }

    public boolean setVariable(String identifier, Object value){
         Scope current = this;
         while (current != null) {
             if (current.variables.containsKey(identifier)) {
                 current.variables.put(identifier, new Variable(identifier, value));
                return true;
             }
             current = current.parent;
         }
         return false;
    }

    public boolean setVariable(String identifier, Expression value, int index) throws InterpreterException {
        Scope current = this;
        while (current != null) {
            if (current.variables.containsKey(identifier)) {

                var list = (Variable) getVariable(identifier);

                if (!(list.getValue() instanceof ListDef))
                    throw new InterpreterException("Object is not an array", null);

                ListDef listObj = (ListDef) list.getValue();
                listObj.val.set(index, value);
                return true;
            } else
                current = current.parent;
        }
        return false;
    }

    public Object getVariable(String identifier, int index) throws InterpreterException {
        Scope current = this;

        while (current != null) {
            if (current.variables.containsKey(identifier)){
                var list = (Variable) getVariable(identifier);

                if (!(list.getValue() instanceof ListDef))
                    throw new InterpreterException("Object is not an array", null);
                    ListDef listObj = (ListDef) list.getValue();

                    return listObj.val.get(index);

            } else
                current = current.parent;
        }
        return false;
    }

    public Object getVariable(String identifier){
        Scope current = this;
        while (current != null) {
            if (current.variables.containsKey(identifier))
                return current.variables.get(identifier);
            current = current.parent;
        }
        return false;
    }

    public boolean addVariable(String identifier, Object value){
        if (contains(identifier))
            return false;
        variables.put(identifier, new Variable(identifier, value));
        return true;
    }

    public void remove(String identifier){
        variables.remove(identifier);
    }
}
