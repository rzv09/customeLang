import java.util.*;

public class FunctionListNode implements JottTree {

    private FunctionDefNode functionDefNode;
    private FunctionListNode functionListNode;
    private boolean hasFunctionDefinition;

    public FunctionListNode(ArrayList<Token> tokens, HashMap<String, FunctionDefNode> functionTable) throws Exception {

        if (tokens.size() == 0) {
            // case where func list = epsilon
            hasFunctionDefinition = false;
        } else {
            // case where func list is not epsilon. It is then func def and another func
            // list
            hasFunctionDefinition = true;
            functionDefNode = new FunctionDefNode(tokens);
            if (functionTable.containsKey(functionDefNode.getIdNode().convertToJott())) {
                throw new Exception("Semantic Error: " + functionDefNode.getIdNode().convertToJott()
                        + " function is already defined");
            } else {
                functionTable.put(functionDefNode.getIdNode().convertToJott(), functionDefNode);
            }

            functionListNode = new FunctionListNode(tokens, functionTable);
        }

    }

    @Override
    public String convertToJott() {
        if (!hasFunctionDefinition) {
            return "";
        }
        return functionDefNode.convertToJott() + "\n" + functionListNode.convertToJott();
    }

    @Override
    public String convertToJava() {
        if (!hasFunctionDefinition)
            return "";
        return functionDefNode.convertToJava() + "\n" + functionListNode.convertToJava();
    }

    @Override
    public String convertToC() {
        if (!hasFunctionDefinition)
            return "";
        return functionDefNode.convertToC() + "\n" + functionListNode.convertToC();
    }

    @Override
    public String convertToPython(int t) {
        if (!hasFunctionDefinition)
            return "";
        return functionDefNode.convertToPython(t) + "\n\n" + functionListNode.convertToPython(t);
    }

    @Override
    public boolean validateTree(HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {
        if (!hasFunctionDefinition) {
            return true;
        }
        return ((functionDefNode.validateTree(functionTable, symbolTable))
                && (functionListNode.validateTree(functionTable, symbolTable)));
    }

}
