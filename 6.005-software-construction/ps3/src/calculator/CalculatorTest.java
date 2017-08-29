package calculator;

import org.junit.Assert;
import org.junit.Test;

public class CalculatorTest {

	// TODO write tests for MultiUnitCalculator.evaluate
	MultiUnitCalculator calc = new MultiUnitCalculator();
	@Test
	public void tests() {
		Assert.assertTrue(approxEquals(calc.evaluate("((1.3 + 5.6))"),calc.evaluate("6.9"),true));
		Assert.assertTrue(approxEquals(calc.evaluate("     6.7 - 2.3"), calc.evaluate("4.4"),true));
		Assert.assertTrue(approxEquals(calc.evaluate("1.2*1.2"),calc.evaluate("1.44"),true));
		Assert.assertTrue(approxEquals(calc.evaluate("64/2"),calc.evaluate("32"),true));
		Assert.assertTrue(approxEquals(calc.evaluate("(1+2)*(6-3)"),calc.evaluate("9"),true));
		Assert.assertTrue(approxEquals(calc.evaluate("(50in / 2.5)"),calc.evaluate("20 in"),true));
		Assert.assertTrue(approxEquals(calc.evaluate("(50in /1.25) in"),calc.evaluate("40 in"),true));
		Assert.assertTrue(approxEquals(calc.evaluate("50in / 1.25in"),calc.evaluate("40"),true));
		Assert.assertTrue(approxEquals(calc.evaluate("50in / 2.5 pt"),calc.evaluate("1440"),true));
		Assert.assertTrue(approxEquals(calc.evaluate("100 / 12.5in"),calc.evaluate("8 in"),true));
		Assert.assertTrue(approxEquals(calc.evaluate("50 in * 2.5 in"),calc.evaluate("125 in"),true));
		Assert.assertTrue(approxEquals(calc.evaluate("52.5 in"),calc.evaluate("50in + 2.5"),true));
		Assert.assertTrue(approxEquals(calc.evaluate("2.5in + 720pt"),calc.evaluate("12.5 in"),true));
		Assert.assertTrue(approxEquals(calc.evaluate("(3in * 2.4)pt"),calc.evaluate("518.4 pt"),true));
		Assert.assertTrue(approxEquals(calc.evaluate("(3+4) * 2.4)"),calc.evaluate("16.8"),true));
		Assert.assertTrue(approxEquals(calc.evaluate("3 + 2.4in"),calc.evaluate("5.4 in"),true));
		Assert.assertTrue(approxEquals(calc.evaluate("3pt * 2.4in"),calc.evaluate("518.4 pt"),true));
		Assert.assertTrue(approxEquals(calc.evaluate("3in * 2.4"),calc.evaluate("7.2 in"),true));
		Assert.assertTrue(approxEquals(calc.evaluate("4pt + (3in*2.4)"),calc.evaluate("522.4 pt"),true));
		Assert.assertTrue(approxEquals(calc.evaluate("(3+2.4) in"),calc.evaluate("5.4 in"),true));
		
	}
	

	boolean approxEquals(String expr1, String expr2, boolean compareUnits) {
		return new Value(expr1).approxEquals(new Value(expr2), compareUnits);
	}

	static class Value {
		static float delta = 0.001f;
 
		enum Unit {
			POINT, INCH, SCALAR
		}

		Unit unit;
		// in points if a length
		float value;

		Value(String value) {
			value = value.trim();
			if (value.endsWith("pt")) {
				unit = Unit.POINT;
				this.value = Float.parseFloat(value.substring(0,
						value.length() - 2).trim());
			} else if (value.endsWith("in")) {
				unit = Unit.INCH;
				this.value = 72 * Float.parseFloat(value.substring(0,
						value.length() - 2).trim());
			} else {
				unit = Unit.SCALAR;
				this.value = Float.parseFloat(value);
			}
		}

		boolean approxEquals(Value that, boolean compareUnits) {
			return (this.unit == that.unit || !compareUnits)
					&& Math.abs(this.value - that.value) < delta;
		}
	}

}
