package Parser.Model.Nodes;


import Parser.Model.Blocks.FunctionDeclaration;
import java.util.List;

public class Program {
    List<FunctionDeclaration> functions;

    public Program(List<FunctionDeclaration> functions){
        this.functions = functions;
    }
}
