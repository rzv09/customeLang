import java.util.*;

public class FunctionDefNode implements JottTree {

    private IdNode idNode;

    private FuncDefParamsNode funcDefParamsNode;

    private FunctionReturnNode functionReturnNode;
    private BodyNode bodyNode;
    private String fileName;
    private int lineNumber;
    public HashMap<String, IdNode> symbolTable = new HashMap<>();

    public FunctionDefNode(ArrayList<Token> tokens) throws Exception {

        if (tokens.get(0).getTokenType() != TokenType.ID_KEYWORD) {
            throw new Exception("Syntax Error: Token " + tokens.get(0).getToken()
                    + " cannot be parsed into an id for FunctionDef at " + tokens.get(0).getFilename() + " line "
                    + tokens.get(0).getLineNum());
        }
        fileName = tokens.get(0).getFilename();
        lineNumber = tokens.get(0).getLineNum();
        idNode = new IdNode(tokens);

        // < function_def > -> <id >[ func_def_params ]: < function_return >{ < body >}

        if (tokens.get(0).getTokenType() != TokenType.L_BRACKET) {
            throw new Exception(
                    "Syntax Error: Token " + tokens.get(0).getToken() + " cannot be parsed into a [ for FunctionDef at "
                            + tokens.get(0).getFilename() + " line " + tokens.get(0).getLineNum());
        }
        tokens.remove(0);

        funcDefParamsNode = new FuncDefParamsNode(tokens);

        funcDefParamsNode.addToSymbolTable(this.symbolTable);

        if (tokens.get(0).getTokenType() != TokenType.R_BRACKET) {

            throw new Exception(
                    "Syntax Error: Token " + tokens.get(0).getToken() + " cannot be parsed into a ] for FunctionDef at "
                            + tokens.get(0).getFilename() + " line " + tokens.get(0).getLineNum());
        }
        tokens.remove(0);

        if (tokens.get(0).getTokenType() != TokenType.COLON) {
            throw new Exception(
                    "Syntax Error: Token " + tokens.get(0).getToken() + " cannot be parsed into a : for FunctionDef at "
                            + tokens.get(0).getFilename() + " line " + tokens.get(0).getLineNum());
        }
        tokens.remove(0);

        functionReturnNode = new FunctionReturnNode(tokens);

        if (tokens.get(0).getTokenType() != TokenType.L_BRACE) {
            throw new Exception(
                    "Syntax Error: Token " + tokens.get(0).getToken() + " cannot be parsed into a { for FunctionDef at "
                            + tokens.get(0).getFilename() + " line " + tokens.get(0).getLineNum());
        }
        tokens.remove(0);

        bodyNode = new BodyNode(tokens, symbolTable);

        if (tokens.get(0).getTokenType() != TokenType.R_BRACE) {
            throw new Exception(
                    "Syntax Error: Token " + tokens.get(0).getToken() + " cannot be parsed into a } for FunctionDef at "
                            + tokens.get(0).getFilename() + " line " + tokens.get(0).getLineNum());
        }
        tokens.remove(0);
        // System.out.println(symbolTable);

    }

    /**
     * constructor for predefined functions
     * 
     * @param functionName
     */
    public FunctionDefNode(String functionName) throws Exception {
        switch (functionName) {
            case "print":
                // here the ID type refers to the type of the arguments that can be passed to a
                // function
                this.idNode = new IdNode("print", "any");
                this.functionReturnNode = new FunctionReturnNode("Void");
                break;
            case "concat":
                this.idNode = new IdNode("concat", "String");
                this.functionReturnNode = new FunctionReturnNode("String");
                break;
            case "length":
                this.idNode = new IdNode("length", "String");
                this.functionReturnNode = new FunctionReturnNode("Integer");
                break;
            case "input":
                this.idNode = new IdNode("input", "String");
                this.functionReturnNode = new FunctionReturnNode("String");
                break;
            default:
                throw new Exception("Unrecognized function " + functionName);
        }
    }

    private Exception syntaxError(Token token, String parseString) {
        String errorMessage = "Syntax Error: Token " + token.getToken() + " cannot be parsed into a " + parseString
                + " for FunctionDef at " + token.getFilename() + " line " + token.getLineNum();
        return new Exception(errorMessage);
    }

    public IdNode getIdNode() {
        return idNode;
    }

    public FunctionReturnNode getFunctionReturnNode() {
        return functionReturnNode;
    }

    public FuncDefParamsNode getFuncDefParamsNode() {
        return funcDefParamsNode;
    }

    @Override
    public String convertToJott() {
        // < function_def > -> <id >[ func_def_params ]: < function_return >{ < body >}
        return idNode.convertToJott() + "[" + funcDefParamsNode.convertToJott() + "]" + ":"
                + functionReturnNode.convertToJott() + "{" + bodyNode.convertToJott() + "}";
    }

    @Override
    public String convertToJava() {
        if (idNode.getId().equals("main")) {
            return "public static " + functionReturnNode.convertToJava() + " " + idNode.convertToJava()
                    + "(String[] args) {" + bodyNode.convertToJava() + "}";
        }
        return "public static " + functionReturnNode.convertToJava() + " " + idNode.convertToJava() + "("
                + funcDefParamsNode.convertToJava() + ") {" + bodyNode.convertToJava() + "}";
    }

    @Override
    public String convertToC() {
        return functionReturnNode.convertToC() + " " + idNode.convertToC() + "(" + funcDefParamsNode.convertToC() + ")"
                + "{" + bodyNode.convertToC() + "}";
    }

    @Override
    public String convertToPython(int t) {
        return "def " + idNode.convertToPython(t) + "(" + funcDefParamsNode.convertToPython(t) + "):"
                + bodyNode.convertToPython(t + 1);
    }

    @Override
    public boolean validateTree(HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {

        if (!functionReturnNode.isVoid()) {
            if (!bodyNode.isReturnable(this.getType(functionTable), functionTable, this.symbolTable)) {

                System.err.println(
                        "Semantic Error: Function body does not have valid returns in all control flows for function "
                                + idNode.getId() + " at file and line: " + fileName + ":" + lineNumber
                                + ". See additional output if return types do not match the expected types.");
                return false;
            }
            // else{
            // if(functionReturnNode.isVoid()){

            // return false;

            // }
        } else if (bodyNode.hasAnyReturns()) {
            // it is void but has a return. this is INVALID!
            // System.err.println("Semantic Error: Function body has one or more returns at
            // " +idNode.getId() + " starting file and line: " + fileName +":" + lineNumber
            // + ". See additional output if return types do not match the expected
            // types.");
            return false;
        }
        // }
        return /*
                * idNode.validateTree(functionTable, this.symbolTable)
                * &&
                */ functionReturnNode.validateTree(functionTable, this.symbolTable)
                && funcDefParamsNode.validateTree(functionTable, this.symbolTable)
                && bodyNode.validateTree(functionTable, this.symbolTable);
    }

    public String getType(HashMap<String, FunctionDefNode> functionTable) {

        if (!functionTable.containsKey(this.idNode.convertToJott())) {
            // throw new Exception("This function is not defined");
            // TODO idk throw an exception or something
        }
        if (functionTable.get(this.idNode.convertToJott()).getFunctionReturnNode().isVoid()) {
            return "Void";
        } else {
            return functionTable.get(this.idNode.convertToJott()).getFunctionReturnNode().getReturnType()
                    .convertToJott();
        }
    }

}
