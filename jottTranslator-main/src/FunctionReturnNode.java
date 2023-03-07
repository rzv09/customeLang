import java.util.ArrayList;
import java.util.HashMap;

public class FunctionReturnNode implements JottTree {

    private TypeNode returnType;
    private boolean returnTypeFlag = false;
    private boolean voidFlag = false;

    public FunctionReturnNode(ArrayList<Token> tokens) throws Exception {
        String t0 = tokens.get(0).getToken();
        if (t0.equals("Void")) {
            voidFlag = true;
            tokens.remove(0); // removes 'Void'
        } else {
            returnTypeFlag = true;
            returnType = new TypeNode(tokens);
        }
    }

    /**
     * for built-in functions
     */
    public FunctionReturnNode(String type) {
        if (type.equals("Void")) {
            voidFlag = true;
        } else {
            returnType = new TypeNode(type);
        }
    }

    public TypeNode getReturnType() {
        return returnType;
    }

    public boolean isVoid() {
        return voidFlag;
    }

    @Override
    public String convertToJott() {
        if (voidFlag) {
            return "Void";
        } else {
            return returnType.convertToJott();
        }
    }

    @Override
    public String convertToJava() {
        if (voidFlag)
            return "void";
        return returnType.convertToJava();
    }

    @Override
    public String convertToC() {
        if (voidFlag)
            return "void";
        return returnType.convertToC();
    }

    @Override
    public String convertToPython(int t) {
        // Function Return is not known in Python, and will not be printed
        return "";
    }

    @Override
    public boolean validateTree(HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {
        if (voidFlag) {
            return true;
        }
        return returnType.validateTree(functionTable, symbolTable);
    }
}
