package Parser.Model.Nodes;

import java.util.LinkedList;
import java.util.List;

public class Parameters {
    private final List<Signature> signatures;

    public Parameters(){
        signatures = new LinkedList<>();
    }

    public void addSignature(Signature signature){
        signatures.add(signature);
    }

    @Override
    public String toString() {
        return "Parameters{" +
                "signatures=" + signatures +
                '}';
    }

    public List<Signature> getSignatures() {
        return signatures;
    }
}
