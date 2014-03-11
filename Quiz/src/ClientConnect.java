

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.net.MalformedURLException;

public class ClientConnect
{
	public static void main (String[] args) throws MalformedURLException, RemoteException, NotBoundException
	{
		new ClientConnect().launch();	
	}

	
	public void launch() throws MalformedURLException, RemoteException, NotBoundException
	{
	
		System.out.print("Insert word to Send: ");
		String str = System.console().readLine();
		
		System.out.print("Insert IP: ");
		String ip = System.console().readLine();
		
		System.out.print("Insert Port: ");
		String port = System.console().readLine();
		
		
	
		System.out.println("Looking up " + "//"+ip+":"+port +"/echo");
		
		
		//Obtain a reference to the object from the
		//registry and type cast it into the appropriate
		//type�		
		TestService service = (TestService)Naming.lookup("//"+ip+":"+port +"/echo");
		
		System.out.println("Looked up " + "//"+ip+":"+port +"/echo");
			
		//cast the returned service to TestService Object
		TestService echoService = service; 
		//Remote echoService = (TestService) service; 
		
		//calls method on server and returns the set word
		String receivedWord = echoService.returnWord();		
		
		//send STR and return same string and place in receivedEcho string
		String receivedEcho = echoService.echo(str);	
		
		System.out.println("Server: " + receivedWord);
		System.out.println("Word Sent and recieved: " + receivedEcho);
		
	
		
		//System.out.println("Returned word from Server: " + receivedEcho + " Mike says: " + receivedWord);
		
		System.out.print("Insert Your Name: ");
		String tempName = System.console().readLine();
		
		service.createNewPlayer(tempName);
		
		
		System.out.println("Players on Server list: ");
		ArrayList<Player> playerList = service.getPlayers();
		for (int i=0; i<playerList.size(); i++)
		{
			Player temp = playerList.get(i);
			System.out.println("ID: "	+ temp.getId());
			System.out.println("Name: "+ temp.getName());
			System.out.println("Score: " + temp.getScore());
		}
		
	}

	
//to run client// java -Djava.security.policy=client.policy EchoClient
}