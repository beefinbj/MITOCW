package calculator;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import calculator.Lexer.Token;
import calculator.Lexer.TokenMismatchException;

public class LexerTest {


	@Test
	public void numTest() throws TokenMismatchException {
		Lexer lex = new Lexer("12345");
		Lexer.Token tok = lex.next();
		
		String expectedText = "12345";
		Type expectedType = Type.NUMBER;
		assertEquals(expectedType, tok.type);
		assertEquals(expectedText, tok.text);	
	}
	
	@Test
	public void plusTest() throws TokenMismatchException {
		Lexer lex = new Lexer("+");
		Lexer.Token tok = lex.next();
		String expectedText = "+";
		Type expectedType = Type.PLUS;
		assertEquals(expectedType, tok.type);
		assertEquals(expectedText, tok.text);	
	}
	
	@Test
	public void minusTest() throws TokenMismatchException {
		Lexer lex = new Lexer("-");
		Lexer.Token tok = lex.next();
		String expectedText = "-";
		Type expectedType = Type.MINUS;
		assertEquals(expectedType, tok.type);
		assertEquals(expectedText, tok.text);	
	}
	
	@Test
	public void timesTest() throws TokenMismatchException {
		Lexer lex = new Lexer("*");
		Lexer.Token tok = lex.next();
		String expectedText = "*";
		Type expectedType = Type.TIMES;
		assertEquals(expectedType, tok.type);
		assertEquals(expectedText, tok.text);	
	}
	
	@Test
	public void divideTest() throws TokenMismatchException {
		Lexer lex = new Lexer("/");
		Lexer.Token tok = lex.next();
		String expectedText = "/";
		Type expectedType = Type.DIVIDE;
		assertEquals(expectedType, tok.type);
		assertEquals(expectedText, tok.text);	
	}
	
	@Test
	public void LParenTest() throws TokenMismatchException {
		Lexer lex = new Lexer("(");
		Lexer.Token tok = lex.next();
		String expectedText = "(";
		Type expectedType = Type.LPAREN;
		assertEquals(expectedType, tok.type);
		assertEquals(expectedText, tok.text);	
	}
	
	@Test
	public void RParenTest() throws TokenMismatchException {
		Lexer lex = new Lexer(")");
		Lexer.Token tok = lex.next();
		String expectedText = ")";
		Type expectedType = Type.RPAREN;
		assertEquals(expectedType, tok.type);
		assertEquals(expectedText, tok.text);	
	}
	
	@Test
	public void decimalTest() throws TokenMismatchException {
		Lexer lex = new Lexer("123.45");
		Lexer.Token tok = lex.next();
		String expectedText = "123.45";
		Type expectedType = Type.NUMBER;
		assertEquals(expectedType, tok.type);
		assertEquals(expectedText, tok.text);	
	}
	
	@Test
	public void inchesTest() throws TokenMismatchException {
		Lexer lex = new Lexer("in");
		Lexer.Token tok = lex.next();
		String expectedText = "in";
		Type expectedType = Type.INCHES;
		assertEquals(expectedType, tok.type);
		assertEquals(expectedText, tok.text);	
	}
	
	@Test
	public void pointTest() throws TokenMismatchException {
		Lexer lex = new Lexer("pt");
		Lexer.Token tok = lex.next();
		String expectedText = "pt";
		Type expectedType = Type.POINTS;
		assertEquals(expectedType, tok.type);
		assertEquals(expectedText, tok.text);	
	}
	
	@Test
	public void complexTest() throws TokenMismatchException {
		Lexer lex = new Lexer("1+2");
		Token[] expected = {new Token(Type.NUMBER,"1"),new Token(Type.PLUS,"+"),new Token(Type.NUMBER,"2")};
		for (int i = 0; i < expected.length; i++) {
			Token tok = lex.next();
			assertEquals(expected[i].type, tok.type);
			assertEquals(expected[i].text, tok.text);
		}
	}
	
	@Test
	public void symbolsTest() throws TokenMismatchException {
		Lexer lex = new Lexer("+*-");
		Token[] expected = {new Token(Type.PLUS,"+"),new Token(Type.TIMES,"*"),new Token(Type.MINUS,"-")};
		for (int i = 0; i < expected.length; i++) {
			Token tok = lex.next();
			assertEquals(expected[i].type, tok.type);
			assertEquals(expected[i].text, tok.text);
		}
	}
	
	@Test
	public void whiteSpaceTest() throws TokenMismatchException {
		Lexer lex = new Lexer("(3 +   2.4) in");
		Token[] expected = {new Token(Type.LPAREN,"("), new Token(Type.NUMBER, "3"), new Token(Type.PLUS,"+"), new Token(Type.NUMBER,"2.4"), new Token(Type.RPAREN, ")"), new Token(Type.INCHES, "in")};
		for (int i = 0; i < expected.length; i++) {
			Token tok = lex.next();
			assertEquals(expected[i].type, tok.type);
			assertEquals(expected[i].text, tok.text);
		}
	}
	
}
