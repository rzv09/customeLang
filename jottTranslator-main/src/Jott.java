import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Jott {

    public static String FILENAME;

    public static int SCANNERINT = 0;

    public static void main(String[] args) {
        // java Jott input.jott output.<> <language>
        if (args.length != 3) {
            System.err.println("Jott arguments must include an input file, output file, and translation language");
            return;
        }
        String inputName = args[0];
        String outputName = args[1];
        FILENAME = outputName.split("\\.")[0];
        String language = args[2].toLowerCase();
        ArrayList<Token> tokens = new ArrayList<>(JottTokenizer.tokenize(inputName));
        // System.out.println(tokens);
        // tokenizer takes care of if the input.jott file does not exist
        JottTree tree = JottParser.parse(tokens);

        if (tree != null) {

            if (!tree.validateTree(null, null)) {
                return;
            }

            String newLanguage;

            switch (language) {
                case "jott":
                    newLanguage = tree.convertToJott();
                    break;
                case "python":
                    newLanguage = tree.convertToPython(0);
                    break;
                case "java":
                    newLanguage = tree.convertToJava();
                    break;
                case "c":
                    newLanguage = tree.convertToC();
                    break;
                default:
                    System.err.println("Language given has to be either Jott, Python, Java, or C");
                    newLanguage = null;
            }

            try {
                File outputFile = new File(outputName);
                outputFile.createNewFile();

                if (outputFile.canWrite() && newLanguage != null) {
                    FileWriter outputWriter = new FileWriter(outputName);
                    outputWriter.write(newLanguage);
                    outputWriter.close();
                }
            } catch (IOException e) {
                System.err.println("Something went wrong");
                e.printStackTrace();
            }
        }

    }
}
