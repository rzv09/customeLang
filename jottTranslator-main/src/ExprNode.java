import java.util.ArrayList;
import java.util.HashMap;

public class ExprNode implements JottTree {

    private IdNode id;
    private ValueNode value;
    private FuncCallNode funcCall;

    private ExprNode firstExpr;
    private OpNode op;
    private ExprNode secondExpr;

    private String fileName;
    private int lineNumber;

    public ExprNode(ArrayList<Token> tokens) throws Exception {
        // can be an id, funcCall, value, or expr op expr

        Token t0 = tokens.get(0);
        TokenType tt0 = (tokens.get(0).getTokenType());
        if ((tt0 != TokenType.ID_KEYWORD && tt0 != TokenType.NUMBER && tt0 != TokenType.STRING)
                // not a negative value
                && !(t0.getToken().equals("-") && tokens.get(1).getTokenType() == TokenType.NUMBER)) {
            throw new Exception(
                    "Syntax Error: Token " + t0.getToken() + " cannot be parsed into a value or id at "
                            + t0.getFilename() + " line "
                            + t0.getLineNum());
        }
        fileName = tokens.get(0).getFilename();
        lineNumber = tokens.get(0).getLineNum();

        firstExpr = new ExprNode(tokens, true);
        if (tokens.get(0).getTokenType() == TokenType.REL_OP || tokens.get(0).getTokenType() == TokenType.MATH_OP) {
            op = new OpNode(tokens);
            secondExpr = new ExprNode(tokens, true);
        }
    }

    private ExprNode(ArrayList<Token> tokens, boolean stop) throws Exception {
        Token t0 = tokens.get(0);
        TokenType tt0 = (tokens.get(0).getTokenType());

        if ((tt0 != TokenType.ID_KEYWORD && tt0 != TokenType.NUMBER && tt0 != TokenType.STRING)
                // not a negative value
                && !(t0.getToken().equals("-") && tokens.get(1).getTokenType() == TokenType.NUMBER)) {
            throw new Exception(
                    "Syntax Error: Token " + t0.getToken() + " cannot be parsed into a value or id at "
                            + t0.getFilename() + " line "
                            + t0.getLineNum());
        }
        TokenType tt1 = (tokens.get(1).getTokenType());

        if (tt0 == TokenType.ID_KEYWORD && tt1 != TokenType.L_BRACKET) { // if it's not a function call
            if (t0.getToken().equals("True") || t0.getToken().equals("False")) { // if it's a boolean
                value = new ValueNode(tokens);
            } else {
                id = new IdNode(tokens); // it's just an IdNode
            }

        } else if ((tt0 == TokenType.STRING) || (tt0 == TokenType.NUMBER) || (t0.getToken().equals("-") && tokens.get(1).getTokenType() == TokenType.NUMBER)) {
            value = new ValueNode(tokens);
            // System.out.println(value.convertToJott());
            // System.out.println(tt0);

        } else {
            funcCall = new FuncCallNode(tokens);
        }
    }

    public boolean isFuncCallNull() {
        return funcCall == null;
    }

    @Override
    public String convertToJott() {
        if (id != null) { // just an id_keyword
            return id.convertToJott();
        } else if (value != null) { // just a value/string/number
            return value.convertToJott();
        } else if (funcCall != null) { // just a function call
            return funcCall.convertToJott();
        } else if (secondExpr != null) { // if there is a second Expression, then it's an (expr op expr) expression
            return firstExpr.convertToJott() + " " + op.convertToJott() + " " + secondExpr.convertToJott();
        } else { // it's just the first expression, no (expr op expr) expression
            return firstExpr.convertToJott();
        }
    }

    @Override
    public String convertToJava() {
        if (id != null)
            return id.convertToJava();
        else if (value != null)
            return value.convertToJava();
        else if (funcCall != null)
            return funcCall.convertToJava();
        else if (secondExpr != null) {
            return firstExpr.convertToJava() + " " + op.convertToJava() + " " + secondExpr.convertToJava();
        } else {
            return firstExpr.convertToJava();
        }
    }

    // returns true if the operation is of the
    // expr (op) expr structure
    // used as helper function for convertToC
    public Boolean isOperationExpression() {
        return !(secondExpr == null);
    }

    // Prints the appropriate ?% for the first argument in C's printf
    public String convertToCPrint() {
        if (id != null) {
            return id.convertToCPrint();
        } else if (value != null) {
            return value.convertToCPrint();
        } else if (funcCall != null) {
            return funcCall.convertToCPrint();
        } else if (secondExpr != null) {
            return firstExpr.convertToCPrint() + " " + op.convertToC() + " " + secondExpr.convertToCPrint();
        } else {
            return firstExpr.convertToCPrint();
        }
    }

    @Override
    public String convertToC() {
        if (id != null) {
            return id.convertToC();
        } else if (value != null) {
            return value.convertToC();
        } else if (funcCall != null) {
            return funcCall.convertToC();
        } else if (secondExpr != null) {
            return firstExpr.convertToC() + " " + op.convertToC() + " " + secondExpr.convertToC();
        } else {
            return firstExpr.convertToC();
        }
    }

    @Override
    public String convertToPython(int t) {
        if (id != null) {
            return id.convertToPython(t);
        } else if (value != null) {
            return value.convertToPython(t);
        } else if (funcCall != null) {
            return funcCall.convertToPython(t);
        } else if (secondExpr != null) {
            return firstExpr.convertToPython(t) + " " + op.convertToPython(t) + " " + secondExpr.convertToPython(t);
        } else {
            return firstExpr.convertToPython(t);
        }
    }

    public FuncCallNode getFuncCall() {
        return funcCall;
    }

    public ExprNode getFirstExpr() {
        return firstExpr;
    }

    public IdNode getId() {
        return id;
    }

    public ValueNode getValue() {
        return value;
    }

    @Override
    public boolean validateTree(HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {

        // ALSO CHECK FOR TYPES AND THEIR EQUALITIES WHEN NEEDED!!! TODO!!!
        if (id != null) { // just an id_keyword - type don't matter.
            return id.validateTree(functionTable, symbolTable);
        } else if (value != null) { // just a value/string/number - type don't matter.
            return value.validateTree(functionTable, symbolTable);
        } else if (funcCall != null) { // just a function call - type don't matter.
            return funcCall.validateTree(functionTable, symbolTable);
        } else if (secondExpr != null) { // if there is a second Expression, then it's an (expr op expr) expression -
                                         // type DO matter!!!
            // make sure both trees are valid. then make sure their types equal each other.

            boolean isSameType = firstExpr.getType(functionTable, symbolTable)
                    .equals(secondExpr.getType(functionTable, symbolTable));

            if (!isSameType) {
                System.err.println(
                        "Semantic Error: Two expressions are operated upon, but have different types at file and line: "
                                + fileName + ":" + lineNumber);
                return false;
            }
            if (secondExpr.getValue() != null && op.getOperator().equals("/")) {    // If you are dividing
                ValueNode valueNode = secondExpr.getValue();
                if (valueNode.getType().equals("Integer") || valueNode.getType().equals("Double")) { // by a number
                    double zero = Double.parseDouble(valueNode.convertToJott());
                    if (zero == 0){ // and that number is 0, return false
                        System.err.println("Sematnic error: can not divide by 0 at file and line: "
                                + fileName + ":" + lineNumber);
                        return false;
                    }
                }
            }

            return (firstExpr.validateTree(functionTable, symbolTable) && op.validateTree(functionTable, symbolTable)
                    && secondExpr.validateTree(functionTable, symbolTable));
        } else { // it's just the first expression, no (expr op expr) expression - type don't
                 // matter
            return firstExpr.validateTree(functionTable, symbolTable);
        }
    }

    // this is pseduocode, prolly wont work until symbol table is up and running.
    public String getType(HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {
        if (id != null) { // just an id_keyword - type don't matter.

            if (!symbolTable.containsKey(id.getId())) {

                System.err.println("Semantic error: variable " + id.getId()
                        + " was not found to be defined at file and line: " + fileName + ":" + lineNumber);
                System.out.println(symbolTable.keySet());
                // todo uncomment this once others add exceptions?
                // throw new Exception("Semantic Error\nid " + id.getId() + " has not yet been
                // declared, yet is used as a variable.\n");
            }
            return symbolTable.get(id.getId()).getType();
        } else if (value != null) { // just a value/string/number - type don't matter.
            return value.getType();
        } else if (funcCall != null) { // just a function call - type don't matter.
            return functionTable.get(funcCall.getFuncName()).getType(functionTable);
        } else if (secondExpr == null) {
            return firstExpr.getType(functionTable, symbolTable);
        } else { // use op to determine which it should be a type of.
            if ((op.getOperator().equals(">")) || (op.getOperator().equals(">=")) || (op.getOperator().equals("<"))
                    || (op.getOperator().equals("<=")) || (op.getOperator().equals("=="))
                    || (op.getOperator().equals("!="))) {
                // if relop, then exprs can ONLY be bool exprs. todo make sure this matches our
                // string type format.
                return "Boolean";
            } else {
                // should be int or double if op is an math op
                return firstExpr.getType(functionTable, symbolTable);
            }

        }

    }
}
