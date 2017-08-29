package calculator;

import java.util.ArrayList;


import calculator.Type;

/**
 * Calculator lexical analyzer.
 */
public class Lexer {

	/**
	 * Token in the stream.
	 */
	public static class Token {
		final Type type;
		final String text;

		Token(Type type, String text) {
			this.type = type;
			this.text = text;
		}

		Token(Type type) {
			this(type, null);
		}
	}

	@SuppressWarnings("serial")
	static class TokenMismatchException extends Exception {
	}
	
	private ArrayList<Token> _tokens;
	private int _counter;

	//Take in an input string and build a Lexer
	//@param input an arithmetic expression in String form
	//@return None

	public Lexer(String input) {
		_tokens = new ArrayList<Token>();
		_counter = 0;
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == '(') {
				_tokens.add(new Token(Type.LPAREN,"("));
			}
			else if (input.charAt(i) == ')') {
				_tokens.add(new Token(Type.RPAREN,")"));
			}
			else if (input.charAt(i) == '+') {
				_tokens.add(new Token(Type.PLUS, "+"));
			}
			else if (input.charAt(i) == '-') {
				_tokens.add(new Token(Type.MINUS, "-"));
			}
			else if (input.charAt(i) == '*') {
				_tokens.add(new Token(Type.TIMES, "*"));
			}
			else if (input.charAt(i) == '/') {
				_tokens.add(new Token(Type.DIVIDE, "/"));
			}
			else if (Character.toString(input.charAt(i)).matches("i")) {
				if (Character.toString(input.charAt(i+1)).matches("n")) {
					_tokens.add(new Token(Type.INCHES, "in"));
					i++;
				}
			}
			else if (Character.toString(input.charAt(i)).matches("p")) {
				if (Character.toString(input.charAt(i+1)).matches("t")) {
					_tokens.add(new Token(Type.POINTS, "pt"));
					i++;
				}
			}
			else if (Character.toString(input.charAt(i)).matches("[0-9]")) {
				String newNum = addNumber(input,i);
				i += newNum.length()-1;
			}
			else if (Character.isWhitespace(input.charAt(i))) {
			}
		}
	}
	
	public String addNumber(String input, int start) {
		StringBuilder newNum = new StringBuilder();
		for (int j = start; j < input.length(); j++) {
			if (Character.toString(input.charAt(j)).matches("[0-9.]")) {
				newNum.append(input.charAt(j));
			}
			else {
				break;
			}
		}
		_tokens.add(new Token(Type.NUMBER, newNum.toString()));
		return newNum.toString();
	}
	
	public Token next() {
		if (_counter < _tokens.size()) {
			Token output = _tokens.get(_counter); 
			_counter++;
			return output;
		}
		else {
			return null;
		}
		
	}
	
	public boolean hasNext() {
		return _counter < _tokens.size();
	}
}
