import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


/**
 * PlayerClient is the connection file to the Server which allows a user to play quizzes
 * java -Djava.security.policy=client.policy PlayerClient
 * This file requests the IP and port of the serve to connect to
 * On successful engagement with server - this file then loads up the PlayGame class
 * @author mikieJ
 *
 */
public class PlayerClient extends ClientConnection implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public static void main (String[] args) throws MalformedURLException, RemoteException, NotBoundException
	{
		try {
				new PlayerClient().launch();
			}
	  catch (IOException e) 
			{
				e.printStackTrace();
			}	
	}
	
	/**
	 * Launch method - ServerManager is obtained and new PlayGame object created
	 * Loads the Welcome menu in PlayGame Class
	 * @throws NotBoundException
	 * @throws IOException
	 */
	public void launch() throws NotBoundException, IOException
	{
		//call method to connect to server		
		ServerManager serverConnect = initialConnect();	
		
		PlayGame newPlayer = new PlayGame(serverConnect);
		newPlayer.welcomeMenu();		
	}
		
	
}
