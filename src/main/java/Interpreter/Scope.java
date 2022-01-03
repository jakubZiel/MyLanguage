package Interpreter;

import Parser.Model.Blocks.FunctionDeclaration;
import Parser.Model.Nodes.Program;

import java.util.HashMap;

public class Scope {

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
}
