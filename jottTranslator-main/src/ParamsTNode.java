import java.util.ArrayList;
import java.util.HashMap;

public class ParamsTNode implements JottTree {

    private boolean isEmpty;
    private ExprNode expressionNode; // requires an expression node to be implemented
    private ParamsTNode paramsTNode;

    public ParamsTNode(ArrayList<Token> tokens) throws Exception {

        if (tokens.get(0).getToken().equals(",")) { // we assume it is not an empty params_t
            this.isEmpty = false;
            tokens.remove(0);

            expressionNode = new ExprNode(tokens);

            this.paramsTNode = new ParamsTNode(tokens);

        } else {
            this.isEmpty = true;
        }
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public ExprNode getExpressionNode() {
        return expressionNode;
    }

    public ParamsTNode getParamsTNode() {
        return paramsTNode;
    }

    public int getLength() {
        if (isEmpty) {
            return 0;
        }
        return 1 + paramsTNode.getLength();
    }

    @Override
    public String convertToJott() {
        if (this.isEmpty) {
            return "";
        }
        return ", " + this.expressionNode.convertToJott() + this.paramsTNode.convertToJott();
    }

    @Override
    public String convertToJava() {
        if (this.isEmpty)
            return "";
        return ", " + this.expressionNode.convertToJava() + this.paramsTNode.convertToJava();
    }

    public String convertToCPrint() {
        if (this.isEmpty)
            return "";
        return this.expressionNode.convertToCPrint() + this.paramsTNode.convertToCPrint();
    }

    @Override
    public String convertToC() {
        if (this.isEmpty)
            return "";
        return ", " + this.expressionNode.convertToC() + this.paramsTNode.convertToC();
    }

    @Override
    public String convertToPython(int t) {
        if (this.isEmpty)
            return "";
        return ", " + this.expressionNode.convertToPython(t) + this.paramsTNode.convertToPython(t);
    }

    @Override
    public boolean validateTree(HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {
        if (isEmpty)
            return true;
        return (this.expressionNode.validateTree(functionTable, symbolTable)
                && this.paramsTNode.validateTree(functionTable, symbolTable));
    }
}
