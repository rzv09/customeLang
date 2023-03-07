import java.util.*;

public class TypeNode implements JottTree {

    private String type;

    public TypeNode(ArrayList<Token> tokens) throws Exception {
        Token tokenToCheck = tokens.get(0);

        if (tokenToCheck.getToken().equals("Double") || tokenToCheck.getToken().equals("Integer")
                || tokenToCheck.getToken().equals("String") || tokenToCheck.getToken().equals("Boolean")) {
            type = tokenToCheck.getToken();
            tokens.remove(0);
        } else {
            throw new Exception("Syntax Error: Token " + tokenToCheck.getToken() + " cannot be parsed into a Type at "
                    + tokenToCheck.getFilename() + " line " + tokenToCheck.getLineNum());
        }
    }

    /**
     * for built-in functions
     * 
     * @param input
     */
    public TypeNode(String input) {
        type = input;
    }

    public String getType() {
        return type;
    }

    @Override
    public String convertToJott() {
        return type;
    }

    @Override
    public String convertToJava() {
        switch (type) {
            case "Integer":
                return "int";
            case "Double":
                return "double";
            case "String":
                return "String";
            default:
                return "boolean";
        }
    }

    @Override
    public String convertToC() {
        if (type.equals("Integer")) {
            return "int";
        } else if (type.equals("Double")) {
            return "double";
        } else if (type.equals("String")) {
            return "char *";
        } else {
            return "bool";
        }
    }

    @Override
    public String convertToPython(int t) {
        return type;
    }

    @Override
    public boolean validateTree(HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {
        // TODO Auto-generated method stub
        return true;
    }

}
