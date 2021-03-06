package Parser.Model.Expressions;

import java.util.LinkedList;
import java.util.List;

public class Arguments {
    public List<Expression> arguments;

    public Arguments() {
        arguments = new LinkedList<>();
    }
    public void addArgument(Expression argument){
        arguments.add(argument);
    }

    @Override
    public String toString() {
        return "Arguments{" +
                "arguments=" + arguments +
                '}';
    }

}
