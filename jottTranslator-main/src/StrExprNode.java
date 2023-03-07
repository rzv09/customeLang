import java.util.*;
public class StrExprNode implements JottTree{



    private IdNode idNode;

    private FuncCallNode funcCallNode;

    private ValueNode strLiteralNode;

    public StrExprNode(ArrayList<Token> tokens)throws Exception{
        Token tokenToCheck = tokens.get(0);

        //waiting until str literal, id and func call are done before I do this one
        
        //< s_expr > -> < str_literal > | <id > | < func_call >

        //This can start with three things. We conduct our checks  based on the first token.

        //if first token is a ", we know it is a string literal. If it has a type of ID, we need to look ahead one. if the next token is a [, it is a func call. otherwise it is an id.

       if(tokenToCheck.getTokenType()==TokenType.STRING){ 
            //string literal
            strLiteralNode = new ValueNode(tokens);

       }
       else if (tokenToCheck.getTokenType() == TokenType.ID_KEYWORD){

        //id or func call. check if next token is a [

        if(tokens.size() <2){
            //just for safety, ensuring next token exists.
            throw new Exception("Syntax Error: Unexpected end in the code at " + tokens.get(0).getFilename() + " line " + tokens.get(0).getLineNum());
        }

        if(tokens.get(1).getTokenType() != TokenType.L_BRACKET){
            //id, not a func call.
            idNode = new IdNode(tokens);
        }
        else{
            //func call, not an id.
            funcCallNode = new FuncCallNode(tokens);

        }

       }
       else{
        //not an id or string literal. Throw error.
        throw new Exception("Syntax Error: Token "+ tokenToCheck.toString() + " was expected to be a string literal or id, but wasn't at " + tokens.get(0).getFilename() + " line " + tokens.get(0).getLineNum());
       }
    }
    
    @Override
    public String convertToJott() {
        if(strLiteralNode != null){
            return strLiteralNode.convertToJott();
        }
        else if(funcCallNode != null){
            return funcCallNode.convertToJott();
        }
        else if(idNode != null){
            return idNode.convertToJott();
        }
        else{
            //should never get here. one of the nodes must be populated.
            return "";
        }
    }

    @Override
    public String convertToJava() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String convertToC() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String convertToPython(int t) {
        return null;
    }

    @Override
    public boolean validateTree(HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {
        // TODO Auto-generated method stub
        return false;
    }

    
    
}
