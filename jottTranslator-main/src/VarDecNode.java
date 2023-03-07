import java.util.ArrayList;
import java.util.HashMap;

public class VarDecNode implements JottTree {
    private TypeNode type;
    private IdNode id;
    private EndStmtNode endStmt;

    private String fileName;
    private int lineNumber;

    public VarDecNode(ArrayList<Token> tokens, HashMap<String, IdNode> symbolTable) throws Exception {
        type = new TypeNode(tokens);

        fileName = tokens.get(0).getFilename();
        lineNumber = tokens.get(0).getLineNum();

        id = new IdNode(tokens);
        endStmt = new EndStmtNode(tokens);
        if (symbolTable.containsKey(id.convertToJott())) {
            throw new Exception("Semantic Error: variable " + id.getId() +
                    " is already declared and line: \"" + fileName + "\":" + lineNumber);
        } else {
            id.setType(type.convertToJott());
            id.setNull(true);
            symbolTable.put(id.convertToJott(), id);
        }
    }

    @Override
    public String convertToJott() {
        return type.convertToJott() + id.convertToJott() + endStmt.convertToJott();
    }

    @Override
    public String convertToJava() {
        return type.convertToJava() + id.convertToJava() + endStmt.convertToJava();
    }

    @Override
    public String convertToC() {
        return null;
    }

    @Override
    public String convertToPython(int t) {
        return "";
    }

    @Override
    public boolean validateTree(HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {
        return true;
    }
}
