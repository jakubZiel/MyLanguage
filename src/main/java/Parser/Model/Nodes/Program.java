package Parser.Model.Nodes;


import Parser.Model.Blocks.FunctionDeclaration;
import java.util.List;

public class Program {
    List<FunctionDeclaration> functions;

    public Program(List<FunctionDeclaration> functions){
        this.functions = functions;
    }

    public List<FunctionDeclaration> getFunctions() {
        return functions;
    }

    public void setFunctions(List<FunctionDeclaration> functions) {
        this.functions = functions;
    }

    @Override
    public String toString() {
        return "Program{" +
                "functions=" + functions +
                '}';
    }
}
