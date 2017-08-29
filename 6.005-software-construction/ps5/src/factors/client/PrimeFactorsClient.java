package factors.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.BigMath;

/**
 *  PrimeFactorsClient class for PrimeFactorsServer.  
 *  
 *  Your PrimeFactorsClient class should take in Program arguments space-delimited
 *  indicating which PrimeFactorsServers it will connect to.
 *      ex. args of "localhost:4444 localhost:4445 localhost:4446"
 *          will connect the client to PrimeFactorsServers running on
 *          localhost:4444, localhost:4445, localhost:4446 
 *
 *  Your client should take user input from standard input.  The appropriate input
 *  that can be processed is a number.  If your input is not of the correct format,
 *  you should ignore it and continue to the next one.
 *  
 *  Your client should distribute to each server the appropriate range of values
 *  to look for prime factors through.
 */
public class PrimeFactorsClient {
	
	private Socket primeFactorSocket;
	private PrintWriter out;
	private BufferedReader in;
	
	public PrimeFactorsClient(String hostName, int portNumber) throws IOException {
		primeFactorSocket = new Socket(hostName, portNumber);
		out = new PrintWriter(primeFactorSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(primeFactorSocket.getInputStream()));
	}
	
	private static boolean validateRequest(String readLine) {
		Pattern p = Pattern.compile("\\s*\\d+\\s*");
		Matcher m = p.matcher(readLine);
		if (m.find()) {
			BigInteger N = new BigInteger(readLine.trim());
			if (N.compareTo(BigInteger.ONE) == 1) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	private List<BigInteger> getFactors() throws IOException {
		List<BigInteger> output = new ArrayList<BigInteger>();
		String line;
		while ((line = in.readLine()) != null) {
			if (line.startsWith("done")) {
				return output;
			}
			else {
				BigInteger fac = new BigInteger(line.split("\\s")[2]);
				output.add(fac);
			}
		}
		return getFactors();
	}
    
    /**
     * @param args String array containing Program arguments.  Each String indicates a 
     *      PrimeFactorsServer location in the form "host:port"
     *      If no program arguments are inputted, this Client will terminate.
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
    	int numServs = args.length;
    	//Connect to the servers
    	PrimeFactorsClient[] clients = new PrimeFactorsClient[numServs];
    	for (int i = 0; i < numServs; i++) {
    		String[] server = args[i].split(":");
    		String hostName = server[0];
    		int portNumber = Integer.parseInt(server[1]);
    		clients[i] = new PrimeFactorsClient(hostName, portNumber);
    	}
				
//		try {
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String fromUser;
		
		while ((fromUser = stdIn.readLine()) != null) {
			// Check for valid input
			if (!validateRequest(fromUser)) {
				System.out.println(">>> " + "invalid" + "\n");
			}
			else {
				BigInteger N = new BigInteger(fromUser.trim());
				BigInteger band = BigMath.sqrt(N).divide(BigInteger.valueOf(numServs));
				BigInteger low, hi;
				// Distribute task over connected servers
				for (int j = 0; j < numServs; j++) {
					if (j == 0) {
						low = BigInteger.valueOf(2);
						hi = band;
					}
					else if (j == numServs-1) {
						low = band.multiply(BigInteger.valueOf(j)).add(BigInteger.ONE);
						hi = BigMath.sqrt(N);
					}
					else {
						low = band.multiply(BigInteger.valueOf(j)).add(BigInteger.ONE);
						hi = band.multiply(BigInteger.valueOf(j+1));
					}
					String msg = "factor "+N.toString()+" "+low.toString()+" "+hi.toString();
					clients[j].out.print(msg + "\n");
					clients[j].out.flush();
				}
				// Accumulate found factors
				List<BigInteger> primeFactors = new ArrayList<BigInteger>();
				BigInteger N_copy = N;
				for (int k = 0; k < numServs; k++) {
					List<BigInteger> facs = clients[k].getFactors();					
					for (int r = 0; r < facs.size(); r++) {
						N_copy = N_copy.divide(facs.get(r));
					}
					primeFactors.addAll(facs);


				}
				// If there are extra large prime factors/If N is prime
				if (N_copy.compareTo(BigInteger.ONE) == 1) {
					primeFactors.add(N_copy);
				}
				// Build final message
				StringBuilder msg = new StringBuilder(N.toString() + "=");
				for (int m = 0; m < primeFactors.size()-1; m++) {
					msg.append(primeFactors.get(m).toString()+"*");
				}
				msg.append(primeFactors.get(primeFactors.size()-1).toString());
				System.out.println(">>> " + msg.toString() + "\n");
			}
		}
		
		for (int p = 0; p < numServs; p++) {
			clients[p].out.close();
			clients[p].in.close();
			clients[p].primeFactorSocket.close();
		}
//		}
//		
//		catch (UnknownHostException e) {
//			System.err.println("Don't know about host " + hostName);
//			System.exit(1);
//		}
//		
//		catch (IOException e) {
//			System.err.println("Couldn't get I/O for the connection to " + hostName);
//			System.exit(1);
//		}
    }
    
}
