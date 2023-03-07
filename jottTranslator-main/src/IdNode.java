import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Raman Zatsarenko
 */
public class IdNode implements JottTree {

    private String id;
    private String type;
    private boolean isNull;
    private String fileName;
    private int lineNumber;

    public IdNode(ArrayList<Token> tokens) throws Exception {
        this.id = tokens.get(0).getToken();
        fileName = tokens.get(0).getFilename();
        lineNumber = tokens.get(0).getLineNum();
        HashSet<String> keywords = new HashSet<>(
                Arrays.asList("while", "if", "default", "this", "new", "class", "try", "this"));
        if (keywords.contains(this.id)) {
            throw new Exception("Semantic Error\nid " + this.id + " is a keyword and cannot be used\n" +
                    tokens.get(0).getFilename() + ":" + tokens.get(0).getLineNum());
        }

        tokens.remove(0);
    }

    // Id's type is the expected type of the input; not the output type!
    public IdNode(String id, String type) {
        this.id = id;
        this.isNull = false;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isNull() {
        return isNull;
    }

    // USED with convertToC functionality.
    // when scanning, we want to acess the adress
    // of the varibale where the input is going.
    // however, when that varibale is already a char *
    // we don't want a double-pointer
    public boolean isCharPointer() {
        return type.equals("String");
    }

    // returns true if the id is the string "print"
    // used as helper function for convertToC
    public boolean isPrint() {
        return id.equals("print");
    }

    public void setNull(boolean aNull) {
        isNull = aNull;
    }

    @Override
    public String convertToJott() {
        return id;
    }

    @Override
    public String convertToJava() {
        return id;
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
        return id;
    }

    @Override
    public String convertToPython(int t) {
        return id;
    }

    @Override
    public boolean validateTree(HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {
        if (!symbolTable.containsKey(id)) {
            System.err.println("Error: undefined id " + id + " at file and line : " + fileName + ":" + lineNumber);
            return false;
        }
        this.setType(symbolTable.get(id).getType());
        return true;
    }

    @Override
    public String toString() {
        return "IdNode{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", isNull=" + isNull +
                '}';
    }
}
