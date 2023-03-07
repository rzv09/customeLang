import java.util.*;
public class DExprNode implements JottTree{


    //SignNode sign

    //Digit

    // I AM WAITING FOR SIGN AND DIGIT BEFORE I DO THIS ONE

    public DExprNode(ArrayList<Token> tokens)throws Exception{
        Token tokenToCheck = tokens.get(0);

        //waiting until id and func call are skeletoned out before I do this one

      
    }
    
    @Override
    public String convertToJott() {
        // TODO Auto-generated method stub
        return "";
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
