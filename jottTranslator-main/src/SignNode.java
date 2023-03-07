import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
public class SignNode implements JottTree{
    private String sign;

    public SignNode(ArrayList<Token> tokens) throws Exception{

        Token tokenToCheck = tokens.get(0);

        if (tokenToCheck.getToken().equals("-")||tokenToCheck.getToken().equals("+")||tokenToCheck.getToken().equals("")) {
            sign = tokenToCheck.getToken();
            tokens.remove(0);
        }
        else {
            throw new Exception("Syntax Error: Token "+ tokenToCheck.toString() + " cannot be parsed into a sign at " + tokens.get(0).getFilename() + " line " + tokens.get(0).getLineNum());
        }
    }

    @Override
    public String convertToJott() {

        return sign;
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
        return false;
    }
}
