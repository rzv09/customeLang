
/**
 * params node
 *
 * @author Aaron Oshiro
 */

import java.util.ArrayList;
import java.util.HashMap;

public class ParamsNode implements JottTree {

    private boolean isEmpty;
    private ExprNode expressionNode;
    private ParamsTNode paramsTNode;

    public ParamsNode(ArrayList<Token> tokens) throws Exception {

        TokenType thisToken = tokens.get(0).getTokenType();
        // also check if value is negative
        if (thisToken == TokenType.STRING || thisToken == TokenType.NUMBER || thisToken == TokenType.ID_KEYWORD || tokens.get(0).getToken().equals("-") && tokens.get(1).getTokenType() == TokenType.NUMBER) {
            this.isEmpty = false;
            this.expressionNode = new ExprNode(tokens);

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

    // Used with convertToC -->
    // returns the prompt given asking for user input
    // (returns to first parameter)
    public String getScanPrompt() {
        return this.expressionNode.convertToC();
    }

    @Override
    public String convertToJott() {
        if (this.isEmpty) {
            return "";
        }
        return this.expressionNode.convertToJott() + this.paramsTNode.convertToJott();
    }

    @Override
    public String convertToJava() {
        if (this.isEmpty)
            return "";
        return this.expressionNode.convertToJava() + this.paramsTNode.convertToJava();
    }

    @Override
    public String convertToC() {
        if (this.isEmpty) {
            return "";
        }
        return this.expressionNode.convertToC() + this.paramsTNode.convertToC();
    }

    public String convertToCPrint() {
        if (this.isEmpty) {
            return "";
        }
        return "\"" + this.expressionNode.convertToCPrint() + this.paramsTNode.convertToCPrint() + "\\n\", "
                + this.expressionNode.convertToC() + this.paramsTNode.convertToC();

    }

    @Override
    public String convertToPython(int t) {
        if (this.isEmpty)
            return "";
        return this.expressionNode.convertToPython(t) + this.paramsTNode.convertToPython(t);
    }

    @Override
    public boolean validateTree(HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {
        if (this.isEmpty) {
            return true;
        }
        return (this.expressionNode.validateTree(functionTable, symbolTable)
                && this.paramsTNode.validateTree(functionTable, symbolTable));
    }
}
