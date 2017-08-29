package echo.server;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * A simple server that will echo client inputs.
 */
public class EchoServer {
	
    /**
     * @param args String array containing Program arguments.  It should only 
     *      contain at most one String indicating the port it should connect to.
     *      The String should be parseable into an int.  
     *      If no arguments, we default to port 4444.
     */
	public static void main(String[] args) throws IOException {
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

		ServerSocket serverSocket = new ServerSocket(portNumber);
		Socket clientSocket = serverSocket.accept();
    	PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
    	BufferedReader in = new BufferedReader (new InputStreamReader(clientSocket.getInputStream()));

		try {
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				out.print(inputLine + "\n");
				out.flush();
			}
		}
		
		catch (IOException e) {
	    	System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
	    	System.out.println(e.getMessage());
		}
		
		finally {
			out.close();
			in.close();
			clientSocket.close();
		}
    }
}