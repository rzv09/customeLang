import java.util.*;

public class ProgramNode implements JottTree {

    private FunctionListNode functionListNode;

    private HashMap<String, FunctionDefNode> functionTable = new HashMap<>();

    public ProgramNode(ArrayList<Token> tokens) throws Exception {

        functionTable.put("print", new FunctionDefNode("print"));
        functionTable.put("concat", new FunctionDefNode("concat"));
        functionTable.put("length", new FunctionDefNode("length"));
        functionTable.put("input", new FunctionDefNode("input"));

        functionListNode = new FunctionListNode(tokens, functionTable);
    }

    @Override
    public String convertToJott() {
        return functionListNode.convertToJott();
    }

    @Override
    public String convertToJava() {
        String fileName = Jott.FILENAME;
        return "import java.util.Scanner;\n\npublic class " + fileName + " {\n" + functionListNode.convertToJava()
                + "\n}";
    }

    @Override
    public String convertToC() {
        return "#include <stdio.h>\n#include <string.h>\n" +
                "#include <stdlib.h>\n\n" + functionListNode.convertToC();
    }

    @Override
    public String convertToPython(int t) {
        String functionList = functionListNode.convertToPython(t);
        return functionList + "main()";
    }

    @Override
    public boolean validateTree(HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {
        if (!functionListNode.validateTree(this.functionTable, null))
            return false;
        // System.out.println(this.functionTable);
        if (!this.functionTable.containsKey("main")) {
            System.err.println("Semantic Error\nmain function is not defined");
            return false;
        }
        if (!this.functionTable.get("main").getFunctionReturnNode().isVoid()) {
            System.err.println("Semantic Error\nmain function does not return Void");
            return false;
        } else if (this.functionTable.get("main").getFuncDefParamsNode().getLength() != 0) {
            System.err.println("Semantic Error\nmain function can not take in parameters");
            return false;
        }
        return true;
    }

}
