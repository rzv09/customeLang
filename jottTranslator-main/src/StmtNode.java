import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author Raman Zatsarenko
 *         I should probably do assignment before stmt
 */
public class StmtNode implements JottTree {

    private ArrayList<String> types = new ArrayList<>(Arrays.asList("Double", "String", "Integer", "Boolean"));
    private AsmtNode asmt;
    private FuncCallNode funcCall;
    private EndStmtNode endStmt;
    private VarDecNode varDec;

    public StmtNode(ArrayList<Token> tokens, HashMap<String, IdNode> symbolTable) throws Exception {
        // <type> <id> = ... (asmt)
        if (types.contains(tokens.get(0).getToken()) && tokens.get(2).getToken().equals("=") ||
                (tokens.get(0).getTokenType() == TokenType.ID_KEYWORD && tokens.get(1).getToken().equals("="))) {
            asmt = new AsmtNode(tokens, symbolTable);
        } else if (tokens.get(1).getToken().equals("[")) {
            funcCall = new FuncCallNode(tokens);
            endStmt = new EndStmtNode(tokens);

        } else {
            varDec = new VarDecNode(tokens, symbolTable);
        }

    }

    @Override
    public String convertToJott() {
        if (asmt != null)
            return asmt.convertToJott();
        else if (varDec != null)
            return varDec.convertToJott();
        else if (funcCall != null)
            return funcCall.convertToJott() + endStmt.convertToJott();
        else
            return "Could not convert to Jott";
    }

    @Override
    public String convertToJava() {
        if (asmt != null)
            return asmt.convertToJava();
        else if (varDec != null)
            return varDec.convertToJava();
        else if (funcCall != null)
            return funcCall.convertToJava() + endStmt.convertToJava();
        else
            return "Could not convert to Java";
    }

    @Override
    public String convertToC() {
        if (asmt != null)
            return asmt.convertToC();
        else if (varDec != null)
            return varDec.convertToC();
        else if (funcCall != null)
            return funcCall.convertToC() + endStmt.convertToC();
        else
            return "Could not convert to C";
    }

    @Override
    public String convertToPython(int t) {
        if (asmt != null)
            return asmt.convertToPython(t);
        else if (varDec != null)
            return varDec.convertToPython(t);
        else if (funcCall != null)
            return funcCall.convertToPython(t) + endStmt.convertToPython(t);
        else
            return "Could not convert to Python";
    }

    @Override
    public boolean validateTree(HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {
        if (asmt != null)
            return asmt.validateTree(functionTable, symbolTable);
        else if (varDec != null)
            return varDec.validateTree(functionTable, symbolTable);
        else
            return funcCall.validateTree(functionTable, symbolTable)
                    && endStmt.validateTree(functionTable, symbolTable);
    }
}
