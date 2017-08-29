package calculator;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {

	@Test
	public void test() {
		//Addition
		Lexer addLex = new Lexer("((1.3 + 5.6))");
		Parser addPar = new Parser(addLex);
		Parser.Value addRes = addPar.evaluate();
		assertEquals("6.9",addRes.toString());
		//Subtraction
		Lexer minusLex = new Lexer("   6.7 -   2.3");
		Parser minusPar = new Parser(minusLex);
		Parser.Value minusRes = minusPar.evaluate();
		assertEquals(minusRes.toString(),"4.4");
		//Multiplication
		Lexer timesLex = new Lexer("1.2*1.2");
		Parser timesPar = new Parser(timesLex);
		Parser.Value timesRes = timesPar.evaluate();
		assertEquals(timesRes.toString(),"1.44");
		//Division
		Lexer divLex = new Lexer("64/2");
		Parser divPar = new Parser(divLex);
		Parser.Value divRes = divPar.evaluate();
		assertEquals(divRes.toString(),"32");
		//Brackets
		Lexer braLex = new Lexer("(1+2)*(6-3)");
		Parser braPar = new Parser(braLex);
		Parser.Value braRes = braPar.evaluate();
		assertEquals(braRes.toString(),"9");
		//Inches/Scalar = Inches
		Lexer IdivSLex = new Lexer("(50in / 2.5)");
		Parser IdivSPar = new Parser(IdivSLex);
		Parser.Value IdivSRes = IdivSPar.evaluate();
		assertEquals(IdivSRes.toString(),"20 in");
		//Inches*Scalar = Inches
		Lexer ItimSLex = new Lexer("50in * 2.5");
		Parser ItimSPar = new Parser(ItimSLex);
		Parser.Value ItimSRes = ItimSPar.evaluate();
		assertEquals(ItimSRes.toString(),"125 in");
		//Casting to inches
		Lexer IcastLex = new Lexer("(50in/1.25)in");
		Parser IcastPar = new Parser(IcastLex);
		Parser.Value IcastRes = IcastPar.evaluate();
		assertEquals(IcastRes.toString(),"40 in");
		//Inches/Inches = Scalar
		Lexer IdivILex = new Lexer("50in / 1.25in");
		Parser IdivIPar = new Parser(IdivILex);
		Parser.Value IdivIRes = IdivIPar.evaluate();
		assertEquals(IdivIRes.toString(),"40");
		//Inches/Points = Scalar
		Lexer IdivPLex = new Lexer("50in / 2.5pt");
		Parser IdivPPar = new Parser(IdivPLex);
		Parser.Value IdivPRes = IdivPPar.evaluate();
		assertEquals(IdivPRes.toString(),"1440");
		//Scalar/Inches = Inches
		Lexer SdivILex = new Lexer("100 / 12.5in");
		Parser SdivIPar = new Parser(SdivILex);
		Parser.Value SdivIRes = SdivIPar.evaluate();
		assertEquals(SdivIRes.toString(),"8 in");
		//Inches*Inches = Inches
		Lexer ItimILex = new Lexer("50in * 2.5in");
		Parser ItimIPar = new Parser(ItimILex);
		Parser.Value ItimIRes = ItimIPar.evaluate();
		assertEquals(ItimIRes.toString(),"125 in");
		//Scalar+Inches = Inches
		Lexer SaddILex = new Lexer("50in + 2.5  ");
		Parser SaddIPar = new Parser(SaddILex);
		Parser.Value SaddIRes = SaddIPar.evaluate();
		assertEquals(SaddIRes.toString(),"52.5 in");
		//Inches+Points = Inches
		Lexer IaddPLex = new Lexer("2.5in + 720pt");
		Parser IaddPPar = new Parser(IaddPLex);
		Parser.Value IaddPRes = IaddPPar.evaluate();
		assertEquals(IaddPRes.toString(),"12.5 in");
		//Mixed
		Lexer mixLex = new Lexer("(3in * 2.4) pt");
		Parser mixPar = new Parser(mixLex);
		Parser.Value mixRes = mixPar.evaluate();
		assertEquals(mixRes.toString(), "518.4 pt");
		//More tests
		Parser.Value test1 = (new Parser(new Lexer("(3+4) * 2.4)")).evaluate());
		assertEquals("16.8", test1.toString());
		Parser.Value test2 = (new Parser(new Lexer("3 + 2.4in")).evaluate());
		assertEquals("5.4 in", test2.toString());
		Parser.Value test3 = (new Parser(new Lexer("3pt * 2.4in")).evaluate());
		assertEquals("518.4 pt", test3.toString());
//		Parser.Value test4 = (new Parser(new Lexer("3 in * 2.4")).evaluate());
//		assertEquals("7.2 in", test4.toString());
		Parser.Value test5 = (new Parser(new Lexer("4pt + (3in*2.4)")).evaluate());
		assertEquals("522.4 pt", test5.toString());
		Parser.Value test6 = (new Parser(new Lexer("(3+2.4) in")).evaluate());
		assertEquals("5.4 in", test6.toString());
		
	}

}
