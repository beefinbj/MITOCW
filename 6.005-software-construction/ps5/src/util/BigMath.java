package util;

import java.math.BigInteger;
import java.util.*;

public class BigMath {

    /**
     * Given a BigInteger input n, where n >= 0, returns the largest BigInteger r such that r*r <= n.
     * 
     * For n < 0, returns 0.
     * 
     * 
     * details: http://faruk.akgul.org/blog/javas-missing-algorithm-biginteger-sqrt
     * 
     * @param n BigInteger input.
     * @return for n >= 0: largest BigInteger r such that r*r <= n.
     *             n <  0: BigInteger 0
     */
    public static BigInteger sqrt(BigInteger n) {
        BigInteger a = BigInteger.ONE;
        BigInteger b = new BigInteger(n.shiftRight(5).add(new BigInteger("8")).toString());
        while(b.compareTo(a) >= 0) {
          BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
          if (mid.multiply(mid).compareTo(n) > 0) 
              b = mid.subtract(BigInteger.ONE);
          else 
              a = mid.add(BigInteger.ONE);
        }
        return a.subtract(BigInteger.ONE);
    }
    
    public static List<BigInteger> primeFactorize(BigInteger n, BigInteger low, BigInteger hi, int primeCert) {
    	List<BigInteger> output = new ArrayList<BigInteger>(); 
    	BigInteger N = n;
    	BigInteger x = low;
    	while (x.compareTo(hi) != 1) {
    		if (x.isProbablePrime(primeCert)) {
    			while (N.mod(x).equals(BigInteger.ZERO)) {
    				output.add(x);
    				N = N.divide(x);
    			}
    		}
    		x = x.add(BigInteger.ONE);
    	}
    	return output;
    }
    
}
