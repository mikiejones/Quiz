import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.NotBoundException;


public abstract class ClientConnection
{

	//constructor - not used
	public ClientConnection()
	{
		
	}
	
	/**
	 * Initial screen seen by client upon loading this class.
	 * @return ServerManager
	 * @throws NotBoundException
	 * @throws IOException 
	 */
	public ServerManager initialConnect() throws NotBoundException, IOException
	{
		System.out.println();
		System.out.println("Just Setting up the playerClient to Server connection...");		
		System.out.println();
		String ip = readLineViaBuffer("Insert the IP of the Server: ");
		String port = readLineViaBuffer("Insert the Port of the Server: ");
		System.out.println("Looking up " + "//"+ip+":"+port +"/MikesServer");		
		ServerManager service = (ServerManager)Naming.lookup("//"+ip+":"+port +"/MikesServer");
		System.out.println("Looked up " + "//"+ip+":"+port +"/MikesServer");
		//return the connected service - ServerManager	
		return service;
	}
	
	
	/**
	 * Reads and returns a line of strings 
	 * @param instructions
	 * @return
	 * @throws IOException
	 */
	public String readLineViaBuffer(String instructions) throws IOException  
	{
		BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
	    System.out.print(instructions);
	    String stringRead = br1.readLine();				
		return stringRead;
	}


}
