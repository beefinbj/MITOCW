package calculator;

import java.util.ArrayList;

import javax.swing.tree.DefaultTreeModel;

import calculator.Lexer;

/*
 * S ::= NUMBER
 * 
 * BINARY OPERATIONS
 * S ::= S PLUS S
 * S ::= S MINUS S
 * S ::= S TIMES S
 * S ::= S DIVIDE S
 * 
 * PARENTHESES
 * S ::= LPAREN S RPAREN
 * 
 * UNITS WORK AS UNARY OPERATIONS
 * S ::= S INCHES
 * S ::= S POINTS
 * 
 */

/**
 * Calculator parser. All values are measured in pt.
 */
class Parser {
	
	@SuppressWarnings("serial")
	static class ParserException extends RuntimeException {
	}

	/**
	 * Type of values.
	 */
	private enum ValueType {
		POINTS, INCHES, SCALAR
	};

	/**
	 * Internal value is always in points.
	 */
	public class Value {
		final double value;
		final ValueType type;

		Value(double value, ValueType type) {
			this.value = value;
			this.type = type;
		}

		@Override
		public String toString() {
			switch (type) {
			case INCHES:
				if (value/PT_PER_IN == (long) (value/PT_PER_IN)) {
					return String.format("%d", (long) (value/PT_PER_IN)) + " in";
				}
				else {
					return String.format("%s", value/PT_PER_IN) + " in";
				}
			case POINTS:
				if (value == (long) value) {
					return String.format("%d", (long)value) + " pt";
				}
				else {
					return String.format("%s", value) + " pt";
				}
			default:
				if (value == (long) value) {
					return "" + String.format("%d", (long)value);
				}
				else {
					return "" + String.format("%s", value);
				}
			}
		}
	}
	
	public class ParseNode<Token>{
		private Lexer.Token data;
		private ParseNode<Lexer.Token> parent;
		private ArrayList<ParseNode<Lexer.Token>> children;
		
		ParseNode(Lexer.Token rootData) {
			this.data = rootData;
			children = new ArrayList<ParseNode<Lexer.Token>>();
		}
	}
	
	
	private static final double PT_PER_IN = 72;
	private final Lexer lexer;
	private ParseNode<Lexer.Token> _parse_root;
	
	// Takes in tokens from a lexer, and parses them by storing the result in a parse tree
	//@param lexer, collection of tokens
	//@returns nothing
	Parser(Lexer lexer) {
		this.lexer = lexer;
		_parse_root = new ParseNode<Lexer.Token>(null);
		ParseNode<Lexer.Token> current_node = _parse_root;
		ParseNode<Lexer.Token> mainLeftChild = new ParseNode<Lexer.Token>(null);
		mainLeftChild.parent = current_node;
		current_node.children.add(mainLeftChild);
		current_node = mainLeftChild;
		while (this.lexer.hasNext()) {
			Lexer.Token tok = this.lexer.next();
			switch(tok.type) {
			case LPAREN:
				ParseNode<Lexer.Token> leftChild = new ParseNode<Lexer.Token>(null);
				leftChild.parent = current_node;
				current_node.children.add(leftChild);
				current_node = leftChild;
				break;
			case PLUS:
			case MINUS:
			case TIMES:
			case DIVIDE:
				current_node = current_node.parent;
				current_node.data = tok;
				ParseNode<Lexer.Token> rightChild = new ParseNode<Lexer.Token>(null);
				rightChild.parent = current_node;
				current_node.children.add(rightChild);
				current_node = rightChild;
				break;
			case RPAREN:
				current_node = current_node.parent;
				break;
			case NUMBER:
				current_node.data = tok;
				//current_node = current_node.parent;
				break;
			case INCHES:
			case POINTS:
				ParseNode<Lexer.Token> newInsert = new ParseNode<Lexer.Token>(tok);
				newInsert.children.add(current_node);
				newInsert.parent = current_node.parent;
				for (int i = 0; i < current_node.parent.children.size(); i++) {
					if (current_node.parent.children.get(i) == current_node) {
						current_node.parent.children.set(i, newInsert);
					}
				}
				current_node.parent = newInsert;
				current_node = newInsert;
				break;
			}
		}
		current_node = current_node.parent;
		
	}

	// Recursively walks though the parse tree to evaluate expression
	//@param nothing
	//@returns value, result from evaluating expression in parse tree
	public Value evaluate() {
		return walk_parse_tree(_parse_root);
	}
	
	public Value walk_parse_tree(ParseNode<Lexer.Token> node) {
		ParseNode<Lexer.Token> cur_node = node;
		while (cur_node.data == null) {
			cur_node = cur_node.children.get(0);
		}
		//Compute stuff, operator with two operands
		if (cur_node.children.size() == 2) {
			Value leftRes = walk_parse_tree(cur_node.children.get(0));
			Value rightRes = walk_parse_tree(cur_node.children.get(1));
			switch (cur_node.data.type) {
			case PLUS:
				if (leftRes.type == rightRes.type) {
					return new Value((double) (leftRes.value+rightRes.value), leftRes.type);
				}
				else if ((leftRes.type == ValueType.SCALAR && rightRes.type == ValueType.POINTS) || (leftRes.type == ValueType.POINTS && rightRes.type == ValueType.SCALAR)) {
					return new Value((double) (leftRes.value+rightRes.value), ValueType.POINTS);
				}
				else if (leftRes.type == ValueType.INCHES && rightRes.type == ValueType.SCALAR) {
					return new Value((double) (leftRes.value+rightRes.value*72), ValueType.INCHES);
				}
				else if (rightRes.type == ValueType.INCHES && leftRes.type == ValueType.SCALAR) {
					return new Value((double) (rightRes.value+leftRes.value*72), ValueType.INCHES);
				}
				else {
					return new Value((double) (leftRes.value+rightRes.value), leftRes.type);
				}
			case MINUS:
				if (leftRes.type == rightRes.type) {
					return new Value((double) (leftRes.value-rightRes.value), leftRes.type);
				}
				else if ((leftRes.type == ValueType.SCALAR && rightRes.type == ValueType.POINTS) || (leftRes.type == ValueType.POINTS && rightRes.type == ValueType.SCALAR)) {
					return new Value((double) (leftRes.value-rightRes.value), ValueType.POINTS);
				}
				else if (leftRes.type == ValueType.INCHES && rightRes.type == ValueType.SCALAR) {
					return new Value((double) (leftRes.value-rightRes.value*72), ValueType.INCHES);
				}
				else if (rightRes.type == ValueType.INCHES && leftRes.type == ValueType.SCALAR) {
					return new Value((double) (rightRes.value-leftRes.value*72), ValueType.INCHES);
				}
				else {
					return new Value((double) (leftRes.value-rightRes.value), leftRes.type);
				}
			case TIMES:
				double prod = (double) leftRes.value*rightRes.value;
				if ((leftRes.type == ValueType.SCALAR && rightRes.type == ValueType.SCALAR) || (leftRes.type == ValueType.POINTS && rightRes.type == ValueType.POINTS)) {
					return new Value(prod, leftRes.type);
				}
				else if (leftRes.type == ValueType.SCALAR) {
					return new Value(prod, rightRes.type);
				}
				else if (rightRes.type == ValueType.SCALAR) {
					return new Value(prod, leftRes.type);
				}
				else if (leftRes.type == ValueType.INCHES && rightRes.type == ValueType.INCHES) {
					return new Value(prod/72, leftRes.type);
				}
				else {
					return new Value(prod, leftRes.type);
				}
			case DIVIDE:
				if (leftRes.type == rightRes.type) {
					return new Value(leftRes.value/rightRes.value, ValueType.SCALAR);
				}
				else if (leftRes.type == ValueType.SCALAR && rightRes.type == ValueType.INCHES) {
					return new Value(leftRes.value/rightRes.value*72*72, rightRes.type);
				}
				else if (rightRes.type == ValueType.SCALAR) {
					return new Value(leftRes.value/rightRes.value, leftRes.type);
				}
				else if (leftRes.type == ValueType.SCALAR) {
					return new Value(leftRes.value/rightRes.value, rightRes.type);
				}
				else {
					return new Value(leftRes.value/rightRes.value, ValueType.SCALAR);
				}
			default:
				return null;
			}
		}
		//Account for inches/points, evaluate the bottom and convert to relevant units
		else if (cur_node.children.size() == 1) {
			Value res = walk_parse_tree(cur_node.children.get(0));
			switch (cur_node.data.type) {
			case INCHES:
				switch(res.type) {
				case INCHES:
					return new Value(res.value, ValueType.INCHES);
				case SCALAR:
					return new Value(res.value*PT_PER_IN, ValueType.INCHES);
				case POINTS:
					return new Value(res.value/PT_PER_IN, ValueType.INCHES);
				}
			case POINTS:
				return new Value(res.value, ValueType.POINTS);
			}
		}
		//Atomic case
		else {
			return new Value(Double.parseDouble(cur_node.data.text),ValueType.SCALAR);
		}
		return null;
	}
	
	
}
