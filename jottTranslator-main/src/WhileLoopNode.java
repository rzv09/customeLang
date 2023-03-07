import java.util.ArrayList;
import java.util.HashMap;

public class WhileLoopNode implements JottTree {

    private ExprNode expr;
    private BodyNode body;

    private String fileName;
    private int lineNumber;

    public WhileLoopNode(ArrayList<Token> tokens, HashMap<String, IdNode> symbolTable) throws Exception {
        fileName = tokens.get(0).getFilename();
        lineNumber = tokens.get(0).getLineNum();

        if (!tokens.get(0).getToken().equals("while")) {
            throw new Exception("Syntax Error: Token " + tokens.get(0).getToken() + " cannot be parsed into 'while' at "
                    + tokens.get(0).getFilename() + " line " + tokens.get(0).getLineNum());
        } else {
            tokens.remove(0); // remove while
            if (!tokens.get(0).getToken().equals("[")) {
                throw new Exception("Syntax Error: Token " + tokens.get(0).getToken() + " cannot be parsed into a [ at "
                        + tokens.get(0).getFilename() + " line " + tokens.get(0).getLineNum());
            }
            tokens.remove(0); // remove [

            expr = new ExprNode(tokens);

            if (!tokens.get(0).getToken().equals("]")) {
                throw new Exception("Syntax Error: Token " + tokens.get(0).getToken() + " cannot be parsed into a ] at "
                        + tokens.get(0).getFilename() + " line " + tokens.get(0).getLineNum());
            }
            tokens.remove(0); // remove ]
            if (!tokens.get(0).getToken().equals("{")) {
                throw new Exception("Syntax Error: Token " + tokens.get(0).getToken() + " cannot be parsed into a { at "
                        + tokens.get(0).getFilename() + " line " + tokens.get(0).getLineNum());
            }
            tokens.remove(0); // remove {
            body = new BodyNode(tokens, symbolTable);
            if (!tokens.get(0).getToken().equals("}")) {
                throw new Exception("Syntax Error: Token " + tokens.get(0).getToken() + " cannot be parsed into a } at "
                        + tokens.get(0).getFilename() + " line " + tokens.get(0).getLineNum());
            }
            tokens.remove(0); // remove }
        }
    }

    @Override
    public String convertToJott() {
        return "while[" + expr.convertToJott() + "]" + "{" + body.convertToJott() + "}";
    }

    @Override
    public String convertToJava() {
        return "while (" + expr.convertToJava() + ") {" + body.convertToJava() + "}";
    }

    @Override
    public String convertToC() {
        return "while (" + expr.convertToC() + ") {" + body.convertToC() + "}";
    }

    @Override
    public String convertToPython(int t) {
        return "while " + expr.convertToPython(t) + ": " + body.convertToPython(t + 1);
    }

    public boolean hasAnyReturns() {
        return body.hasAnyReturns();
    }

    public boolean isReturnable(String type, HashMap<String, FunctionDefNode> functionTable,
            HashMap<String, IdNode> symbolTable) {

        return body.isReturnable(type, functionTable, symbolTable);
    }

    @Override
    public boolean validateTree(HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {

        if (!expr.getType(functionTable, symbolTable).equals("Boolean")) {
            System.err.println(
                    "Semantic Error: While statement does not have a boolean type expression in its condition at file and line: "
                            + fileName + ":" + lineNumber);
            return false;

        }

        return ((expr.validateTree(functionTable, symbolTable)) && (body.validateTree(functionTable, symbolTable)));
    }
}
