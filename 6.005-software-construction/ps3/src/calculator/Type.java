package calculator;

/*
 * Numerals: 0,1,2,3,4,5,6, etc. in other words [0-9]+, also decimal numbers, so [0-9]+.[0-9]*
 * Four arithmetic operations: +, -, *, /
 * Units: in, pt
 * Parentheses: ()
 * Decimal: .
 */

/**
 * Token type.
 */
enum Type {
	PLUS,
	MINUS,
	TIMES,
	DIVIDE,
	LPAREN,
	RPAREN,
	INCHES,
	POINTS,
	NUMBER,
}