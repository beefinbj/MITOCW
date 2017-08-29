package factors.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.*;
import java.util.*;
import util.BigMath;

/**
 *  PrimeFactorsServer performs the "server-side" algorithm 
 *  for counting prime factors.
 *
 *  Your PrimeFactorsServer should take in a single Program Argument 
 *  indicating which port your Server will be listening on.
 *      ex. arg of "4444" will make your Server listen on 4444.
 *      
 *  Your server will only need to handle one client at a time.  If the 
 *  connected client disconnects, your server should go back to listening for
 *  future clients to connect to.
 *  
 *  The client messages that come in will indicate the value that is being
 *  factored and the range of values this server will be processing over.  
 *  Your server will take this in and message back all factors for our value.
 */
public class PrimeFactorsServer {
        
    /** Certainty variable for BigInteger isProbablePrime() function. */
    private final static int PRIME_CERTAINTY = 10;
    private final static String CLIENT_MESSAGE = "factor\\s\\d+\\s\\d+\\s\\d+";
    
    
    private static boolean validateRequest(String readLine) {
		Pattern p = Pattern.compile(CLIENT_MESSAGE);
		Matcher m = p.matcher(readLine);
		if (m.find()) {
			String[] args = readLine.split("\\s");
			BigInteger N = new BigInteger(args[1]);
			// N must be > 1
			if (N.compareTo(BigInteger.ONE) != 1) {
				return false;
			}
			BigInteger low = new BigInteger(args[2]);
			BigInteger hi = new BigInteger(args[3]);
			// low must be > 1
			if (low.compareTo(BigInteger.ONE) != 1) {
				return false;
			}
			return true;
		}
		else {
			return false;
		}
	}
    
    /**
     * @param args String array containing Program arguments.  It should only 
     *      contain one String indicating the port it should connect to.
     *      Defaults to port 4444 if no Program argument is present.
     */
    public static void main(String[] args) {
    	if (args.length > 1 ) {
			System.exit(1);
		}
		
		int portNumber;
		
		if (args.length == 0) {
			portNumber = 4444;
		}
		else {
			portNumber = Integer.parseInt(args[0]);
		}
		

		try (ServerSocket serverSocket = new ServerSocket(portNumber);
			Socket clientSocket = serverSocket.accept();
	    	PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
	    	BufferedReader in = new BufferedReader (new InputStreamReader(clientSocket.getInputStream()));) {
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				if (!validateRequest(inputLine)) {
					out.println("invalid" + "\n");
					out.flush();
				}
				else {
					String[] nums = inputLine.split("\\s");
					BigInteger N = new BigInteger(nums[1]);
					BigInteger low = new BigInteger(nums[2]);
					BigInteger hi = new BigInteger(nums[3]);
					List<BigInteger> facs = BigMath.primeFactorize(N, low, hi, PRIME_CERTAINTY);
					for (int i = 0; i < facs.size(); i++) {
						out.println("found " + N.toString() + " " + facs.get(i).toString());
						out.flush();
					}
					out.println("done " + N.toString() + " " + low.toString() + " " + hi.toString());
				}
			}
			out.close();
			in.close();
			clientSocket.close();
		}
		
		catch (IOException e) {
	    	System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
	    	System.out.println(e.getMessage());
		}
		
    }
}
