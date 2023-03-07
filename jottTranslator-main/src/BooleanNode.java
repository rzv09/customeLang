/**
 * This is the boolean node
 * Contains the boolean given
 *
 * @author Aaron Oshiro
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class BooleanNode implements JottTree {

    private String boolNode;

    private HashSet<String> boolOps = new HashSet<>();

    public BooleanNode(ArrayList<Token> tokens) throws Exception {
        createBooleans();
        if (boolOps.contains(tokens.get(0).getToken())) {
            Token newBoolNode = tokens.remove(0);
            boolNode = newBoolNode.getToken();
        } else {
            throw new Exception("Syntax Error: Token can not be parsed into a Boolean at " +tokens.get(0).getFilename() + " line " + tokens.get(0).getLineNum());
        }
    }

    private void createBooleans(){
        boolOps.add("True");
        boolOps.add("False");
    }

    @Override
    public String convertToJott() {
        return boolNode;
    }

    @Override
    public String convertToJava() {
        return null;
    }

    @Override
    public String convertToC() {
        return null;
    }

    @Override
    public String convertToPython(int t) {
        return null;
    }

    @Override
    public boolean validateTree(HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {
        return true;
    }
}
