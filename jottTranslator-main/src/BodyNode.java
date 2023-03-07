import java.util.ArrayList;
import java.util.HashMap;

public class BodyNode implements JottTree {

    private ArrayList<BodyNode> bodyArrayList = new ArrayList<>();
    private ReturnNode rtrn;
    private boolean rtrnFlag = false;
    private boolean epsilonFlag = false;
    private BodyStmtNode bodyStatement;
    private boolean bodyStatementFlag = false;

    private String fileName;
    private int lineNumber;

    public BodyNode(ArrayList<Token> tokens, HashMap<String, IdNode> symbolTable) throws Exception {

        String t0 = tokens.get(0).getToken();
        fileName = tokens.get(0).getFilename();
        lineNumber = tokens.get(0).getLineNum();
        // System.out.println(t0);
        if (t0.equals("}")) {
            epsilonFlag = true;
            tokens.remove(0);
        } else if (t0.equals("return")) {
            rtrn = new ReturnNode(tokens);
            rtrnFlag = true;
            // tokens.remove(0);

        } else {
            bodyStatement = new BodyStmtNode(tokens, symbolTable);
            bodyStatementFlag = true;
            while ((tokens.size() != 0) && !tokens.get(0).getToken().equals("}")) {
                // if (tokens.get(0).getToken().equals("return")) {
                // rtrn = new ReturnNode(tokens);
                // rtrnFlag = true;
                // tokens.remove(0);
                // break;
                // } else {
                bodyArrayList.add(new BodyNode(tokens, symbolTable));

            }
        }
    }

    @Override
    public String convertToJott() {
        if (epsilonFlag) {
            return "";
        }
        if (rtrnFlag) {
            return rtrn.convertToJott();
        } else {
            String allBodies = "";
            for (int i = 0; i < bodyArrayList.size(); i++) {
                allBodies += (bodyArrayList.get(i).convertToJott());
            }
            return bodyStatement.convertToJott() + allBodies;
        }
    }

    @Override
    public String convertToJava() {
        if (epsilonFlag) return "";
        if (rtrnFlag) return rtrn.convertToJava();
        String allBodies = "";
        for (int i = 0; i < bodyArrayList.size(); i++) {
            allBodies += (bodyArrayList.get(i).convertToJava());
        }
        return bodyStatement.convertToJava() + allBodies;
    }

    @Override
    public String convertToC() {
        if (epsilonFlag) {
            return "";
        }
        if (rtrnFlag) {
            return rtrn.convertToC();
        } else {
            String allBodies = "";
            for (int i = 0; i < bodyArrayList.size(); i++) {
                allBodies += (bodyArrayList.get(i).convertToC());
            }
            return bodyStatement.convertToC() + allBodies;
        }
    }

    @Override
    public String convertToPython(int t) {
        if (epsilonFlag)
            return "";
        if (rtrnFlag)
            return rtrn.convertToPython(t);
        String allBodies = "";
        for (int i = 0; i < bodyArrayList.size(); i++) {
            allBodies += (bodyArrayList.get(i).convertToPython(t));
        }
        return bodyStatement.convertToPython(t) + allBodies;
    }

    @Override
    public boolean validateTree(HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {
        for (int i = 0; i < bodyArrayList.size(); i++) {
            if (!bodyArrayList.get(i).validateTree(functionTable, symbolTable)) {
                // would report an error, but if a child is not valid, that will report its
                // error itself.
                return false;
            }
        }

        if (epsilonFlag) {
            return true;
        }
        // again, when we are just return children validations, we can postpone error
        // reporting until the root cause is found.
        if (rtrnFlag) {
            return rtrn.validateTree(functionTable, symbolTable);
        } else {
            return bodyStatement.validateTree(functionTable, symbolTable);
        }
    }

    public boolean hasAnyReturns(){
        if(rtrnFlag){
             System.err.println("Semantic Error: Void function has one or more returns at file and line: " + fileName +":" + lineNumber );
            return true;
        }
        else if (bodyStatement.hasAnyReturns()){
            return true;
        }

        for (int i = 0; i < bodyArrayList.size(); i++) {
            if (bodyArrayList.get(i).hasAnyReturns()) {
                return true;
            }
        }

        return false;


    }

    public boolean isReturnable(String type,HashMap<String, FunctionDefNode> functionTable, HashMap<String, IdNode> symbolTable) {
        // Todo, go through all bodies and body statements checking if there is a single
        // return or not. Use that to determine this result. Used by FuncDef.
        // Type is passed down to verify the returned thing is of the correct type? may
        // not need it tbh

        // should we check all bodies before statements? feel like this may cause an
        // issue with the ifs and elses and outside returns?
        if (rtrnFlag) {
            if(type.equals("Void")){
                System.err.println("Semantic Error: Void function has return at file and line: " + fileName +":" +lineNumber);
                return true;
            }

            //todo. how do I gesymbol and func table here? guess I have to pass it ;_;
            String returnedType = rtrn.getReturnedType(functionTable, symbolTable);
            if(!type.equals(returnedType)){
                System.err.println("Semantic Error: Function return expected type " + type +" but instead got a return of type " +returnedType +" at file and line: " + fileName +":" +lineNumber);
                return false;
            }


            return true;
        } else if (bodyStatement.isReturnable(type, functionTable, symbolTable)) {
            return true;
        } else {

            for (int i = 0; i < bodyArrayList.size(); i++) {
                if (bodyArrayList.get(i).isReturnable(type, functionTable, symbolTable)) {
                    return true;
                }
            }
        }
        return false;
    }
}
