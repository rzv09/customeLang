import java.util.ArrayList;
import java.util.HashMap;

/**
 * FuncDefParamsNode
 *
 * @author Raman Zatsarenko
 */
public class FuncDefParamsNode implements JottTree {
    private boolean hasFuncDefParams;
    private IdNode id;
    private TypeNode type;
    private FuncDefParamsTNode funcDefParamsT;

    public FuncDefParamsNode(ArrayList<Token> tokens) throws Exception {
        // func params is [], so list is empty
        if (tokens.get(0).getToken().equals("]")) {
            hasFuncDefParams = false;
        } else {
            hasFuncDefParams = true;
            // The bracket check should be in FuncDefNode
            // if(!tokens.get(0).getToken().equals("[")) {
            // throw new Exception("Token "+ tokens.get(0).getToken() + "cannot be parsed
            // into a [ at line " + tokens.get(0).getLineNum());
            // }
            // remove [
            // tokens.remove(0);
            id = new IdNode(tokens);
            if (!tokens.get(0).getToken().equals(":")) {
                throw new Exception("Syntax Error: Token " + tokens.get(0).getToken() + " cannot be parsed into a : at "
                        + tokens.get(0).getFilename() + " line " + tokens.get(0).getLineNum());
            }
            // remove ":"
            tokens.remove(0);
            type = new TypeNode(tokens);
            funcDefParamsT = new FuncDefParamsTNode(tokens);

        }
    }

    public void addToSymbolTable(HashMap<String, IdNode> symbolTable) {
        if (hasFuncDefParams) {
            this.id.setType(type.convertToJott());
            symbolTable.put(id.getId(), id);
            this.funcDefParamsT.addToSymbolTable(symbolTable);
        }
    }

    public TypeNode getType() {
        return type;
    }

    public FuncDefParamsTNode getFuncDefParamsT() {
        return funcDefParamsT;
    }

    @Override
    public String convertToJott() {
        if (!hasFuncDefParams)
            return "";
        else {
            return id.convertToJott() + ":" + type.convertToJott() + funcDefParamsT.convertToJott();
        }
    }

    public int getLength() {
        if (hasFuncDefParams) {
            return 1 + funcDefParamsT.getLength();
        }
        return 0;
    }

    @Override
    public String convertToJava() {
        if (!hasFuncDefParams)
            return "";
        return type.convertToJava() + " " + id.convertToJava() + funcDefParamsT.convertToJava();
    }

    @Override
    public String convertToC() {
        if (!hasFuncDefParams) {
            return "";
        }
        return type.convertToC() + " " + id.convertToC()
                + funcDefParamsT.convertToC();
    }

    @Override
    public String convertToPython(int t) {
        if (!hasFuncDefParams)
            return "";
        else {
            return id.convertToPython(t) + funcDefParamsT.convertToPython(t);
        }
    }

    @Override
    public boolean validateTree(HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {
        if (!hasFuncDefParams)
            return true;
        else
            return id.validateTree(functionTable, symbolTable) && type.validateTree(functionTable, symbolTable)
                    && funcDefParamsT.validateTree(functionTable, symbolTable);
    }
}
