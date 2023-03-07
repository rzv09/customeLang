import java.util.*;

public class ElseIfNode implements JottTree {

    private boolean hasElse;

    private BodyNode bodyNode;

    private ExprNode exprNode;

    private ElseIfNode elseIfNode;
    private String fileName;
    private int lineNumber;
    public ElseIfNode(ArrayList<Token> tokens, HashMap<String, IdNode> symbolTable) throws Exception {
        Token tokenToCheck = tokens.get(0);
        fileName = tokens.get(0).getFilename();
        lineNumber = tokens.get(0).getLineNum();
        if (tokenToCheck.getToken().equals("elseif")) {
            // get rid of the else
            tokens.remove(0);
            hasElse = true;

            // get rid of lbracket
            if (!tokens.get(0).getToken().equals("[")) {
                throw new Exception("Syntax Error: Token " + tokenToCheck.getToken() + " cannot be parsed into a [ at "
                        + tokenToCheck.getFilename() + " line " + tokenToCheck.getLineNum());
            }
            tokens.remove(0);

            exprNode = new ExprNode(tokens);

            // get rid of rbracket
            if (!tokens.get(0).getToken().equals("]")) {
                throw new Exception(
                        "Syntax Error: Token " + tokenToCheck.getToken() + "cannot be parsed into a ] at at "
                                + tokenToCheck.getFilename() + " line " + tokenToCheck.getLineNum());
            }
            tokens.remove(0);

            // get rid of lbracket
            if (!tokens.get(0).getToken().equals("{")) {
                throw new Exception("Syntax Error: Token " + tokenToCheck.getToken()
                        + "cannot be parsed into a { at line " + tokenToCheck.getLineNum());
            }
            tokens.remove(0);

            bodyNode = new BodyNode(tokens, symbolTable);

            // get rid of rbracket
            if (!tokens.get(0).getToken().equals("}")) {
                throw new Exception("Syntax Error: Token " + tokenToCheck.getToken()
                        + "cannot be parsed into a } at line " + tokenToCheck.getLineNum());
            }
            tokens.remove(0);

            // I just added this in phase 3 - didnt we need this to be in convert to jott
            // for phase 2?!?
            elseIfNode = new ElseIfNode(tokens, symbolTable);

        } else {
            hasElse = false;
            // TODO make sure I handle Empty string case correctly. Do I need to look ahead?
            // I think if the token is not else we assume it is the empty case. Or do I need
            // to use the follow set to see what can follow an elseIf?
            // throw new Exception("Token "+ tokenToCheck.toString() + "cannot be parsed
            // into a Else at line " + tokenToCheck.getLineNum());
        }
    }

    @Override
    public String convertToJott() {
        if (!hasElse) {
            return "";
        }
        return "elseif[" + exprNode.convertToJott() + "]{" + bodyNode.convertToJott() + "}"
                + elseIfNode.convertToJott();
    }

    @Override
    public String convertToJava() {
        if (!hasElse) return "";
        return "else if (" + exprNode.convertToJava() + ") {" + bodyNode.convertToJava()
                + "}" + elseIfNode.convertToJava();
    }

    @Override
    public String convertToC() {
        if (!hasElse) {
            return "";
        }
        return "else if(" + exprNode.convertToC() + "){" + bodyNode.convertToC() + "}" + elseIfNode.convertToC();
    }

    @Override
    public String convertToPython(int t) {
        if (!hasElse)
            return "";
        return "\n" + "\t".repeat(Math.max(0, t)) + "elif " + exprNode.convertToPython(t) + ": "
                + bodyNode.convertToPython(t + 1) + elseIfNode.convertToPython(t);
    }

    @Override
    public boolean validateTree(HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {

        /*
         * 
         * private boolean hasElse;
         * 
         * private BodyNode bodyNode;
         * 
         * private ExprNode exprNode;
         * 
         * private ElseIfNode elseIfNode;
         */
        if (!hasElse) {
            return true;
        }

        if(!exprNode.getType(functionTable, symbolTable).equals("Boolean")){
            System.err.println("Semantic Error: Elseif statement does not have a boolean type expression in its condition at file and line: " + fileName + ":" + lineNumber);
            return false;

        }

        return (bodyNode.validateTree(functionTable, symbolTable) & exprNode.validateTree(functionTable, symbolTable)
                && elseIfNode.validateTree(functionTable, symbolTable));

    }

    public boolean hasAnyReturns(){

        if (hasElse) {

            return bodyNode.hasAnyReturns() || elseIfNode.hasAnyReturns();
        }
        return false;
    }
    public boolean isReturnable(String type,HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {

        if (hasElse) {

            return bodyNode.isReturnable(type, functionTable, symbolTable) && elseIfNode.isReturnable(type, functionTable, symbolTable);
        }
        return true;
    }

}
