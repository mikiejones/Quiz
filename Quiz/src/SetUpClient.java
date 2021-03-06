
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * This is the client class to load in order to setUp new Quizzes
 * New Quizzes are given Ids that the player can locate on the server when using PLayerClient
 * java -Djava.security.policy=client.policy SetUpClient
 * @author mikieJ
 *
 */
public class SetUpClient extends ClientConnection  implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main (String[] args) throws MalformedURLException, RemoteException, NotBoundException
	{
		try {
				new SetUpClient().launch();
			}
	  catch (IOException e) 
			{
				e.printStackTrace();
			}	
	}

	/**
	 * Launch method to initiate the connection to the server
	 * @throws NotBoundException
	 * @throws IOException
	 */
	public void launch() throws NotBoundException, IOException
	{
		//call method to connect to server		
		ServerManager serverConnect = initialConnect();	
	
		//get Setup menu from server
		SetUpMenu newSetup = new SetUpMenu(serverConnect);
		newSetup.welcomeMenu();
	}

}