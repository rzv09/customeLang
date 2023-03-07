
/**
 * This is the REL_OP node
 * Contains the relationship operator given
 *
 * @author Aaron Oshiro
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class RelOpNode implements JottTree {

    private String relationshipOperator;

    private HashSet<String> relOps = new HashSet<>();

    public RelOpNode(ArrayList<Token> tokens) throws Exception {
        createRelOps();
        if (relOps.contains(tokens.get(0).getToken())) {
            Token newRelOp = tokens.remove(0);
            relationshipOperator = newRelOp.getToken();
        } else {
            throw new Exception("Syntax Error: Token " + tokens.get(0).getToken() + " is not of type REL_OP at"
                    + tokens.get(0).getFilename() + " line " + tokens.get(0).getLineNum());
        }
    }

    private void createRelOps() {
        relOps.add(">");
        relOps.add(">=");
        relOps.add("<");
        relOps.add("<=");
        relOps.add("==");
        relOps.add("!=");
    }

    @Override
    public String convertToJott() {
        return this.relationshipOperator;
    }

    @Override
    public String convertToJava() {
        return this.relationshipOperator;
    }

    @Override
    public String convertToC() {
        return this.relationshipOperator;
    }

    @Override
    public String convertToPython(int t) {
        return this.relationshipOperator;
    }

    @Override
    public boolean validateTree(HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {
        return true;
    }
}
