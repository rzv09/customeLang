import java.util.ArrayList;
import java.util.HashMap;

public class EndStmtNode implements JottTree {

    public EndStmtNode(ArrayList<Token> tokens) throws Exception {
        Token t0 = tokens.get(0);
        if (t0.getToken().equals(";")) {
            tokens.remove(0);
        } else {
            throw new Exception(
                    "Syntax Error: Token " + t0.getToken() + " cannot be parsed into a ; at " + t0.getFilename()
                            + " line " + t0.getLineNum());
        }
    }

    @Override
    public String convertToJott() {
        return ";";
    }

    @Override
    public String convertToJava() {
        return ";";
    }

    @Override
    public String convertToC() {
        return ";";
    }

    @Override
    public String convertToPython(int t) {
        return "";
    }

    @Override
    public boolean validateTree(HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {
        return true;
    }
}
