import java.util.*;

public class ElseNode implements JottTree {

    private boolean hasElse;

    private BodyNode bodyNode;

    public ElseNode(ArrayList<Token> tokens, HashMap<String, IdNode> symbolTable) throws Exception {
        Token tokenToCheck = tokens.get(0);

        if (tokenToCheck.getToken().equals("else")) {
            // get rid of the else
            tokens.remove(0);
            hasElse = true;

            // get rid of lbracket
            if (!tokens.get(0).getToken().equals("{")) {
                throw new Exception("Syntax Error: Token " + tokenToCheck.getToken() + " cannot be parsed into a { at "
                        + tokenToCheck.getFilename() + " line " + tokenToCheck.getLineNum());
            }
            tokens.remove(0);

            bodyNode = new BodyNode(tokens, symbolTable);

            // get rid of rbracket
            if (!tokens.get(0).getToken().equals("}")) {
                throw new Exception("Syntax Error: Token " + tokenToCheck.getToken() + "cannot be parsed into a } at "
                        + tokenToCheck.getFilename() + " line " + tokenToCheck.getLineNum());
            }
            tokens.remove(0);

        } else {
            hasElse = false;
            // TODO handle Empty string case. Do I need to look ahead? I think if the token
            // is not else we assume it is the empty case.

            // throw new Exception("Token "+ tokenToCheck.toString() + "cannot be parsed
            // into a Else at line " + tokenToCheck.getLineNum());
        }
    }

    @Override
    public String convertToJott() {
        if (!hasElse) {
            return "";
        }
        return "else{" + bodyNode.convertToJott() + "}";
    }

    @Override
    public String convertToJava() {
        if (!hasElse) return "";
        return "else {" + bodyNode.convertToJava() + "}";
    }

    @Override
    public String convertToC() {
        if (!hasElse) {
            return "";
        }
        return "else{" + bodyNode.convertToC() + "}";
    }

    @Override
    public String convertToPython(int t) {
        if (!hasElse)
            return "";
        return "\n" + "\t".repeat(Math.max(0, t)) + "else: " + bodyNode.convertToPython(t + 1);
    }

    @Override
    public boolean validateTree(HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {
        if (!hasElse) {
            return true;
        }

        return (bodyNode.validateTree(functionTable, symbolTable));
    }

    public boolean hasAnyReturns(){

        if (hasElse) {
            return bodyNode.hasAnyReturns();
        }
        return false;
    }

    public boolean isReturnable(String type,HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {
        if (hasElse) {
            return bodyNode.isReturnable(type, functionTable, symbolTable);
        }
        return false;
    }

}
