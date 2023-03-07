import java.util.*;

public class MathOpNode implements JottTree {

    private String operation;

    public MathOpNode(ArrayList<Token> tokens) throws Exception {
        Token tokenToCheck = tokens.get(0);

        if (tokenToCheck.getToken().equals("+") || tokenToCheck.getToken().equals("-")
                || tokenToCheck.getToken().equals("/") || tokenToCheck.getToken().equals("*")) {
            operation = tokenToCheck.getToken();
            tokens.remove(0);
        } else {
            throw new Exception("Syntax Error: Token " + tokenToCheck.toString() + " cannot be parsed into a MathOp at "
                    + tokens.get(0).getFilename() + " line " + tokens.get(0).getLineNum());
        }
    }

    @Override
    public String convertToJott() {
        // TODO Auto-generated method stub
        return operation;
    }

    @Override
    public String convertToJava() {
        return operation;
    }

    @Override
    public String convertToC() {
        return operation;
    }

    @Override
    public String convertToPython(int t) {
        return operation;
    }

    @Override
    public boolean validateTree(HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {
        // TODO Auto-generated method stub
        return true;
    }

}
