import java.util.ArrayList;
import java.util.HashMap;

public class ValueNode implements JottTree {

    private String value;
    private String type;

    private boolean negative;

    public ValueNode(ArrayList<Token> tokens) throws Exception {
        negative = false;
        if (tokens.get(0).getToken().equals("-")) {
            this.value = "-"  + tokens.get(1).getToken();
            negative = true;
        }
        else this.value = tokens.get(0).getToken();

        TokenType typeToCheck = tokens.get(0).getTokenType();

        if (typeToCheck == TokenType.STRING) {
            type = "String";
        } else if (typeToCheck == TokenType.ID_KEYWORD) {
            // i think it can only be a boolean if the token is a keyword... right?!?
            type = "Boolean";
        } else {
                if (value.contains(".")) {
                    type = "Double";
                } else {
                    type = "Integer";
                }

        }
        tokens.remove(0);
        if (negative) tokens.remove(0);
    }

    public boolean isNegative() {
        return negative;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String convertToJott() {
        return value;
    }

    @Override
    public String convertToJava() {
        if (type.equals("Boolean"))
            return value.toLowerCase(); // change True and False to true and False
        return value;
    }

    public String convertToCPrint() {
        if (type.equals("Integer") || type.equals("Double")) {
            return "%d";
        } else if (type.equals("String")) {
            return "%s";
        } else {
            return "%b";
        }
    }

    @Override
    public String convertToC() {
        return value;
    }

    @Override
    public String convertToPython(int t) {
        return value;
    }

    @Override
    public boolean validateTree(HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {
        return true;
    }

    @Override
    public String toString() {
        return "ValueNode{" +
                "value='" + value + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
