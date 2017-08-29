package piwords;

import static org.junit.Assert.*;

import org.junit.Test;

public class PiGeneratorTest {
    @Test
    public void basicPowerModTest() {
        // 5^7 mod 23 = 17
        assertEquals(17, PiGenerator.powerMod(5, 7, 23));
    }
    
    public void negPowerModTest() {
    	assertEquals(-1, PiGenerator.powerMod(5,7,-3));
    	assertEquals(-1, PiGenerator.powerMod(-5,7,23));
    	assertEquals(-1, PiGenerator.powerMod(5,-7,23));
    }
    
    public void aPowerModTest() {
    	assertEquals(1, PiGenerator.powerMod(1,7,23));
    	assertEquals(0, PiGenerator.powerMod(0,7,23));
    	assertEquals(3, PiGenerator.powerMod(2,7,5));
    }
    
    public void bPowerModTest() {
    	
    }
    
    public void cPowerModTest() {
    	
    }

    // TODO: Write more tests (Problem 1.a, 1.c)
}
