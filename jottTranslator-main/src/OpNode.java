import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class OpNode implements JottTree {

    private String operator;
    private String fileName;
    private int lineNumber;

    public OpNode(ArrayList<Token> tokens) throws Exception {
        this.operator = tokens.get(0).getToken();
        fileName = tokens.get(0).getFilename();
        lineNumber = tokens.get(0).getLineNum();
        tokens.remove(0);
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public String convertToJott() {
        return operator;
    }

    @Override
    public String convertToJava() {
        return operator;
    }

    @Override
    public String convertToC() {
        return operator;
    }

    @Override
    public String convertToPython(int t) {
        return operator;
    }

    @Override
    public boolean validateTree(HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {
        HashSet<String> operators = new HashSet<>(Arrays.asList(">", ">=", "<", "<=", "==", "!=", "+", "-", "/", "*"));

        if (operators.contains(operator)) {
            return true;
        } else {
            System.err
                    .println("Math operator is not a valid operator at file and line: " + fileName + ":" + lineNumber);
            return false;
        }
    }
}
