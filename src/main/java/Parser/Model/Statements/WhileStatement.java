package Parser.Model.Statements;

import Parser.Model.Blocks.Block;
import Parser.Model.Conditions.Condition;

public class WhileStatement {
    private Condition condition;
    private Block body;

    public WhileStatement(Condition condition, Block body) {
        this.body = body;
        this.condition = condition;
    }
}
