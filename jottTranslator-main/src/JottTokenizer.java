
/**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author I added a test comment
 * @author Aaron Oshiro
 * @author Raman Zatsarenko
 * @author Michael Driscoll (mkd5329)
 * @author Camille Decker (cid3081)
 **/

import java.util.ArrayList;
import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;

public class JottTokenizer {

	/**
	 * Takes in a filename and tokenizes that file into Tokens
	 * based on the rules of the Jott Language
	 *
	 * @param filename the name of the file to tokenize; can be relative or absolute
	 *                 path
	 * @return an ArrayList of Jott Tokens
	 */
	public static ArrayList<Token> tokenize(String filename) {

		ArrayList<Token> tokens = new ArrayList<>();
		try {
			Scanner jottFile = new Scanner(new File(filename));
			int lineNumber = 0;
			while (jottFile.hasNextLine()) {
				lineNumber++;
				String jottLine = jottFile.nextLine();
				ArrayList<Token> newTokens = createTokensPerLine(jottLine, filename, lineNumber);
				tokens.addAll(newTokens);

			}
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			return null;
		} catch (Exception e) {
//			e.printStackTrace();
			System.err.println(e);
			return null;
		}

		return tokens;
	}

	private static ArrayList<Token> createTokensPerLine(String jottLine, String fileName, int lineNum)
			throws Exception {

		ArrayList<Token> tokensInCurrentLine = new ArrayList<>();

		for (int i = 0; i < jottLine.length(); i++) {

			char currentCharacter = jottLine.charAt(i);

			// go through if statements - do we want to put this in a helper method? idk

			if (currentCharacter == ';') {
				tokensInCurrentLine.add(new Token("" + currentCharacter, fileName, lineNum, TokenType.SEMICOLON));
			} else if (currentCharacter == ':') {
				tokensInCurrentLine.add(new Token("" + currentCharacter, fileName, lineNum, TokenType.COLON));
			} else if (currentCharacter == '.') {
				if (i + 1 < jottLine.length() && Character.isDigit(jottLine.charAt(i + 1))) {
					String fullNumber = ".";

					// go through each char until we find a non digit.
					i++;
					char nextCharacter = jottLine.charAt(i);
					while (i < jottLine.length() && Character.isDigit(jottLine.charAt(i))) {
						nextCharacter = jottLine.charAt(i);
						fullNumber = fullNumber + nextCharacter;
						i++;
					}
					if (i < jottLine.length()) {
						// go back to not skip a character. we want to start here next.
						i--;
					}
					tokensInCurrentLine.add(new Token(fullNumber, fileName, lineNum, TokenType.NUMBER));
				} else {
					// NON TERMINAL STATE - THROW ERROR HERE. This is a placeholder until we
					// implement the error reporting.
					throw new Exception("Syntax Error on line " + lineNum + ": Expected digit after '.'");
					// execution should halt here, so we need to throw an error I think. To do
					// later.
				}

			} else if (currentCharacter == '/' || currentCharacter == '*' || currentCharacter == '-'
					|| currentCharacter == '+') {

				tokensInCurrentLine.add(new Token("" + currentCharacter, fileName, lineNum, TokenType.MATH_OP));

			} else if (Character.isDigit(currentCharacter)) { // if it's a number
				String fullNumber = "" + currentCharacter;
				i++;
				while (i < jottLine.length() && Character.isDigit(jottLine.charAt(i))) {
					currentCharacter = jottLine.charAt(i);
					fullNumber += currentCharacter;
					i++;
				}
				if (i < jottLine.length()) { // if there's more past the number
					currentCharacter = jottLine.charAt(i);
					if (currentCharacter == '.') {
						fullNumber += ".";
						i++;
						while (i < jottLine.length() && Character.isDigit(jottLine.charAt(i))) {
							currentCharacter = jottLine.charAt(i);
							fullNumber += currentCharacter;
							i++;
						}
					}
				}
				if (i < jottLine.length()) {
					i--;
				}
				tokensInCurrentLine.add(new Token(fullNumber, fileName, lineNum, TokenType.NUMBER));

			} else if (Character.isAlphabetic(currentCharacter)) { // if it's a letter
				String idOrKeyword = "" + currentCharacter;
				i++;
				while (i < jottLine.length()
						&& (Character.isAlphabetic(jottLine.charAt(i)) || Character.isDigit(jottLine.charAt(i)))) {
					currentCharacter = jottLine.charAt(i);
					idOrKeyword += currentCharacter;
					i++;
				}
				if (i < jottLine.length()) {
					i--;
				}
				tokensInCurrentLine.add(new Token(idOrKeyword, fileName, lineNum, TokenType.ID_KEYWORD));

			} else if (currentCharacter == '!') {
				if (i + 1 < jottLine.length() && jottLine.charAt(i + 1) == '=') { // if '!' if followed by '='
					i++;
					tokensInCurrentLine.add(new Token("!=", fileName, lineNum, TokenType.REL_OP));
				} else {
					// Non-terminal state, requires an '=' after an '!'
					throw invalidTokenException("!", fileName, lineNum);
				}

			} else if (currentCharacter == '"') { // if it's a "

				String string = "\"";
				i++;
				while (i < jottLine.length() && isAlphaDigitOrSpace(jottLine.charAt(i))) {
					currentCharacter = jottLine.charAt(i);
					string += currentCharacter;
					i++;
				}
				if (i < jottLine.length() && jottLine.charAt(i) == '"') {
					i++;
					tokensInCurrentLine.add(new Token(string + "\"", fileName, lineNum, TokenType.STRING));
				} else {
					throw new Exception("String token must have ending ' \" ': " + string); // Error handling needs
																							// improvement
				}
				if (i < jottLine.length()) {
					i--;
				}

			} else if (currentCharacter == ' ' || currentCharacter == '\t') {
			} else if (currentCharacter == '#') {
				return tokensInCurrentLine;
			} else if (currentCharacter == ',') {
				tokensInCurrentLine.add(new Token("" + currentCharacter, fileName, lineNum, TokenType.COMMA));
			} else if (currentCharacter == '[') {
				tokensInCurrentLine.add(new Token("" + currentCharacter, fileName, lineNum, TokenType.L_BRACKET));
			} else if (currentCharacter == ']') {
				tokensInCurrentLine.add(new Token("" + currentCharacter, fileName, lineNum, TokenType.R_BRACKET));
			} else if (currentCharacter == '{') {
				tokensInCurrentLine.add(new Token("" + currentCharacter, fileName, lineNum, TokenType.L_BRACE));
			} else if (currentCharacter == '}') {
				tokensInCurrentLine.add(new Token("" + currentCharacter, fileName, lineNum, TokenType.R_BRACE));
			} else if (currentCharacter == '=') {
				if (i + 1 < jottLine.length() && jottLine.charAt(i + 1) == '=') {
					i++;
					String relOp = currentCharacter + "=";
					tokensInCurrentLine.add(new Token(relOp, fileName, lineNum, TokenType.REL_OP));
				} else
					tokensInCurrentLine.add(new Token("" + currentCharacter, fileName, lineNum, TokenType.ASSIGN));
			} else if (currentCharacter == '<' || currentCharacter == '>') {
				if (i + 1 < jottLine.length() && jottLine.charAt(i + 1) == '=') {
					i++;
					String relOp = currentCharacter + "=";
					tokensInCurrentLine.add(new Token(relOp, fileName, lineNum, TokenType.REL_OP));
				} else {
					tokensInCurrentLine.add(new Token("" + currentCharacter, fileName, lineNum, TokenType.REL_OP));
				}
			}

			else {
				// throw error. for now i just print so I can see when this triggers
				throw new Exception("Syntax Error on line " + lineNum
						+ ": Invalid character detected: " + currentCharacter);
			}

		}
		return tokensInCurrentLine;
	}

	/**
	 * Helper function for string token
	 *
	 * @param character character to be checked
	 * @return if it's either a letter, number, or space
	 */
	private static Boolean isAlphaDigitOrSpace(char character) {
		return Character.isAlphabetic(character) || Character.isDigit(character) || character == ' ';
	}

	private static Exception invalidTokenException(String token, String fileName, int lineNumber) {
		return new Exception("\nSyntax Error\nInvalid token \"" + token + "\"\n" + fileName + ":" + lineNumber);
	}
}