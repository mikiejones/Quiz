

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

public class ClientConnect
{
	public static void main (String[] args) throws MalformedURLException, RemoteException, NotBoundException
	{
		try {
				new ClientConnect().launch();
			}
	  catch (IOException e) 
			{
				e.printStackTrace();
			}	
	}

	
	public void launch() throws NotBoundException, IOException
	{
		//call method to connect to server		
		ServerManager serverConnect = initialConnect();	
	
		//get Setup menu from server
		SetUPMenu newSetup = new SetUPMenu(serverConnect);
		newSetup.welcomeMenu();
		//System.out.println("quiz 1 = " + serverConnect.getQuizFromID(1).getQuizName());
		
		
		
	
	//Saves repeating the request for input - requires the instructions of what your are asked to type
	//require the throws IOException to allow buffer reader to work 

	
		/*
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
		*/
	}

	public ServerManager initialConnect() throws MalformedURLException, RemoteException, NotBoundException
	{
		
		System.out.print("Insert word to Send: ");
		String str = System.console().readLine();
		
		System.out.print("Insert IP: ");
		String ip = System.console().readLine();
		
		System.out.print("Insert Port: ");
		String port = System.console().readLine();		
	
		System.out.println("Looking up " + "//"+ip+":"+port +"/echo");
		
		ServerManager service = (ServerManager)Naming.lookup("//"+ip+":"+port +"/echo");
		System.out.println("Looked up " + "//"+ip+":"+port +"/echo");
			
		return service;
	}
	
	

//to run client// java -Djava.security.policy=client.policy EchoClient
}