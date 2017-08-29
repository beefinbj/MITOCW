package piwords;

import java.lang.Math;;

public class PiGenerator {
    /**
     * Returns precision hexadecimal digits of the fractional part of pi.
     * Returns digits in most significant to least significant order.
     * 
     * If precision < 0, return null.
     * 
     * @param precision The number of digits after the decimal place to
     *                  retrieve.
     * @return precision digits of pi in hexadecimal.
     */
    public static int[] computePiInHex(int precision) {
        int k = 0;
        int[] output = new int[precision]; 
        double sum_1 = 0;
        double sum_2 = 0;
        double sum_3 = 0;
        double sum_4 = 0;
        while (k < precision) {
        	double num_1 = powerMod(16,precision-k,8*k+1);
        	double den_1 = 8*k+1;
        	double num_2 = powerMod(16,precision-k,8*k+4);
        	double den_2 = 8*k+4;
        	double num_3 = powerMod(16,precision-k,8*k+5);
        	double den_3 = 8*k+5;
        	double num_4 = powerMod(16,precision-k,8*k+6);
        	double den_4 = 8*k+6;
        	sum_1 += num_1/den_1;
        	sum_2 += num_2/den_2;
        	sum_3 += num_3/den_3;
        	sum_4 += num_4/den_4;
        	k++;
        }
        System.out.println(sum_1);
        System.out.println(sum_2);
        System.out.println(sum_3);
        System.out.println(sum_4);
        double raw = (4*sum_1 - 2*sum_2 - sum_3 - sum_4) % 1;
        
        int j = 0;
        while (j < precision) {
        	double dig = raw*16 - ((raw*16)%1);
        	raw = (raw*16)%1;
        	output[j] = (int) dig;
        	j++;
        }
        
        return output;
    }

    /**
     * Computes a^b mod m
     * 
     * If a < 0, b < 0, or m < 0, return -1.
     * 
     * @param a
     * @param b
     * @param m
     * @return a^b mod m
     */
    public static int powerMod(int a, int b, int m) {
        if (a < 0 || b < 0 || m <= 0) {
        	return -1;
        }
        int output = 1;
        for (int i = 0; i < b; i++) {
        	output = output*a;
        	if (output > m) {
        		output = output % m;
        	}
        }
    	return output;
    }
    
    /**
     * Computes the nth digit of Pi in base-16.
     * 
     * If n < 0, return -1.
     * 
     * @param n The digit of Pi to retrieve in base-16.
     * @return The nth digit of Pi in base-16.
     */
    public static int piDigit(int n) {
        if (n < 0) return -1;
        
        n -= 1;
        double x = 4 * piTerm(1, n) - 2 * piTerm(4, n) -
                   piTerm(5, n) - piTerm(6, n);
        x = x - Math.floor(x);
        
        return (int)(x * 16);
    }
    
    private static double piTerm(int j, int n) {
        // Calculate the left sum
        double s = 0;
        for (int k = 0; k <= n; ++k) {
            int r = 8 * k + j;
            s += powerMod(16, n-k, r) / (double) r;
            s = s - Math.floor(s);
        }
        
        // Calculate the right sum
        double t = 0;
        int k = n+1;
        // Keep iterating until t converges (stops changing)
        while (true) {
            int r = 8 * k + j;
            double newt = t + Math.pow(16, n-k) / r;
            if (t == newt) {
                break;
            } else {
                t = newt;
            }
            ++k;
        }
        
        return s+t;
    }
}
