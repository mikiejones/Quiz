import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * PlayGame is the class which acts as the main menu for playing the quizzes available on the server.
 * You can also view all quizzes on the server, the client scores and the Top scores, for either a particular quiz or all quizzes.
 * All score and data are collected locally where possible and then sent back to the server on completion.
 * 
 * @author mikieJ
 *
 */

public class PlayGame implements java.io.Serializable
{

private ServerManager serverConnect;
private Player player;


//temp measure to me updated 
private int playerScore;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	//constructor - pass the serverConnect file retrieved from Server
	public PlayGame(ServerManager servMan)
	{
		this.serverConnect = servMan;
	}
	
	
	/**
	 * Main menu which runs in a loop until exit
	 * Provides options for the user to view and play Quizzes
	 * @throws IOException
	 */
	public void welcomeMenu() throws IOException
	{
		String quizInt = null;
		int quizId = 0;
		boolean setUpComplete = false;
		boolean complete = false;
		//loop until correctly establish if new player or returning player
		while (!setUpComplete)
		{
			if(playerSetup())
			{
				setUpComplete = true;
			}
		}
		
		//setup player complete - now load Quiz options for Player Client
		while(!complete)
		{
			System.out.println();
			System.out.println("************* Welcome to Mike's Quiz Server *************");
			System.out.println();
			System.out.println("What would you like to do? ");
			System.out.println("1: Play a quiz?");
			System.out.println("2: Show all available Quizzes");
			System.out.println("3: Show all your scores for a particular Quiz");
			System.out.println("4: Show all your scores for all Quizzes");
			System.out.println("5: Show all the scores for a particular Quiz");
			System.out.println("6: Show all the scores for a all Quizzes");
			System.out.println("7: Show all the Top Scores for a all Quizzes");
			System.out.println("8: Show Top Score for a particular Quiz");			
			System.out.println("0: Exit");
		
			//insert option from above - will loop until int identified
			int option = stringToIntCheck("Please enter the required option: ");
		
			switch(option) 
			{
				case 0: System.out.println("Exiting: Thank you and good bye");
						complete = true;
						System.exit(0);
						break;
						
				case 1: //System.out.println("*** DEBUG **** Play a quiz");
				
						if (quizListEmpty())
						{
							playQuiz();
						}
						break;
						
				case 2: //System.out.println("*** DEBUG **** Get All Available Quizzes");
						displayAllQuizzes();
						break;
					
						
				case 3: //System.out.println("*** DEBUG **** Show all your scores");
						
						if (quizListEmpty())
						{
							displayAllQuizzes();
							
							quizId = stringToIntCheck("Please enter the ID of the Quiz in which you wish to view your Scores: ");		
							if (serverConnect.returnAllQuizzes().containsKey(quizId))
							{	
								playerScoreForQuizID(quizId, player.getId());
							}
							else
							{
									quizNotPresent();
							}
						}									
						break;
						
				case 4: //System.out.println("*** DEBUG **** Show all your scores for All Quizzes");
						if (quizListEmpty())
						{
							allPlayerScoreForQuizID(player.getId());
						}				
						break;							
						
						
				case 5: //System.out.println("*** DEBUG **** Show all the scores for quiz ID");
						
						if (quizListEmpty())
						{
							displayAllQuizzes();
							quizId = stringToIntCheck("Please enter the ID of the Quiz in which you wish to view all the Scores: ");
							if (serverConnect.returnAllQuizzes().containsKey(quizId))
							{	
								allPlayerScoresForQuizID(quizId);
							}
							else
							{
									quizNotPresent();
							}						
						}				
						break;		
						
				case 6: //System.out.println("*** DEBUG **** Show all scores for All Quizzes");
						if (quizListEmpty())
						{
							allScoresForAllQuizzes();
						}					
						break;	
						
						
						
				case 7: //System.out.println("*** DEBUG **** Show all Top scores for All Quizzes");
						if (quizListEmpty())
						{
							getAllTopScoresForAllQuizzes();
						}				
						break;
				
				
				case 8: //System.out.println("*** DEBUG **** Show Top Scores for particular Quiz");
						if (quizListEmpty())
						{
							displayAllQuizzes();
							quizId = stringToIntCheck("Please enter the ID of the Quiz in which you wish to view the Top Scores: ");
							if (serverConnect.returnAllQuizzes().containsKey(quizId))
							{	
								getTopScoreForQuizId(quizId);
							}
							else
							{
									quizNotPresent();
							}		
						}							
						break;
				
				default: System.out.println("That is not an Option, try again");
						 break;
			}
		}		
	}
	
	
	/**
	 * Setup or retrieve player on connecting to the server.
	 * returning players enter an int to check against the server data
	 * new Players Simply enter their name
	 * @throws IOException
	 */
	public boolean playerSetup() throws IOException
	{
		boolean setUpComplete = false;
		System.out.println();
		String opt = readLineViaBuffer("Welcome, are you a returning player with an ID? y/n: ");
		//Returning player - get them from the server
		if (opt.toLowerCase().equals("y"))
		{
			int Id = stringToIntCheck("Please enter your Id: ");
			//serverConnect.returningPlayer(name, Id);
			player = serverConnect.getPlayerFromId(Id);
			if (player != null)
			{
				System.out.println();
				System.out.println("Welcome back " + player.getName());
				setUpComplete = true;
			}
			else
			{
				System.out.println("That player ID Does not exist, please try again");
			}
		}
		// New Player
		else if (opt.toLowerCase().equals("n"))
		{
			String name = readLineViaBuffer("Please enter your name: ");
			int Id = serverConnect.addNewPlayer(name);	
			System.out.println();
			System.out.println("**********************************************************************");
			System.out.println(name + ", Your Player ID is: " + Id + ". Please write this down and Keep it safe.");
			System.out.println("**********************************************************************");
			System.out.println();
			player = serverConnect.getPlayerFromId(Id);
			setUpComplete = true;			
		}
		//User did not correctly enter option
		else
		{
			System.out.println("You have not entered a recognised option - try again!");
		}
		return setUpComplete;		
	}
	
	
	
	
	/**
	 * Loads The Desired Quiz from Input, then allows user to input answers for each question
	 * @throws IOException
	 */
	public void playQuiz() throws IOException 
	{
		System.out.println(); 
		displayAllQuizzes();
		System.out.println();
		
		
		int option = stringToIntCheck("Please enter the ID of the Quiz in which you wish to play: ");
		if (serverConnect.returnAllQuizzes().containsKey(option))
				{					
					Quiz temp = serverConnect.getQuizFromID(option);					
					
					System.out.println();
					//confirms the quiz chosen
					System.out.println("Welcome to " + temp.getQuizName());
					List <Question> quests = temp.getQuestions();
					int questSize = quests.size();
					//create n amount of questions as indicated
					for (int i = 0; i < questSize; i++)
					{
						Question tempQuest = quests.get(i);
						System.out.println("Question: " + (i +1));
						System.out.println(tempQuest.getQuestion());
						String[] tempQuestArray = tempQuest.getAnswers();
						System.out.println((0 + 1) + ":"  + tempQuestArray[0]);
						System.out.println((1 + 1) + ":"  + tempQuestArray[1]);
						System.out.println((2 + 1) + ":"  + tempQuestArray[2]);
						int correctAnswer = tempQuest.getCorrectAnswer();
						
						int answer = stringToIntCheck("Please enter the number for the correct Answer: ");
								
						if (answer == correctAnswer)
						{		
							System.out.println();
							System.out.println("That answer is Correct");
							System.out.println();
							playerScore = playerScore + 1;
						}
						else if (answer != correctAnswer && answer < 4) 
						{
							System.out.println();
							System.out.println("That answer is wrong");
							System.out.println();
						}
						else
							{
								System.out.println("That Answer is not an option - Going to class that as a wrong Answer!");
							}
					}		
					PlayerScores pScore = new PlayerScores(option, player.getId(), player.getName(), playerScore);		
					//returns a string if top score added
					String returned = temp.addToPlayerScore(pScore);
					if (!returned.equals(""))
					{
						System.out.println(returned);
					}				
					//to show the score has been saved and returned from server
					//must add return function
					System.out.println("End of Quiz. Your Score was " + playerScore);
					//reset score for next go or set of questions.
					playerScore = 0;
				}
				else {
						quizNotPresent();
					 }
	}
	
	
	
	/**
	 * Displays player's score for each quiz
	 * @throws IOException
	 */
	public void allScoresForAllQuizzes() throws IOException
	{
		System.out.println();
		System.out.println("All Scores for the Available Quizzes");
	
		//iterates through two hashmaps to find the score for the Id provided or each quiz
		 HashMap<Integer, Quiz> tempQHashMap = getAllQuizzes();
			for(Map.Entry<Integer, Quiz> entry: tempQHashMap.entrySet())
			{			
					System.out.println();
					printRecalledQuizDets( entry.getValue());
					underline();
					List<PlayerScores> allScores = entry.getValue().getScores();
					int allScoresSize = allScores.size();
					for (int i = 0; i < allScoresSize; i++)
					{
						printRecalledPlayerDets(allScores.get(i));
					}
			  System.out.println();
			  underline();
			}			
	}
	
	

	
	/**
	 * Gets all the scores for a certain QuizID
	 * @param Id
	 * @throws RemoteException
	 */
	public void allPlayerScoresForQuizID(int quizId) throws IOException
	{
		System.out.println();
		System.out.println("All player Scores for the Quiz ID: " + quizId);
		System.out.println();
		//return from ID
		Quiz tempQ = serverConnect.getQuizFromID(quizId);
		printRecalledQuizDets(tempQ);
		underline();
		//list all players scores for that quiz
		List<PlayerScores> pScores = tempQ.getScores();
		int pScoresSize = pScores.size();
		for (int i = 0; i < pScoresSize; i++)
		{
				printRecalledPlayerDets(pScores.get(i));
		}
		System.out.println();
	}

	
	/**
	 * Gets a players score from their id for a certain quiz
	 * Also Prints out their average score
	 * @param Id
	 * @throws RemoteException
	 */
	public void playerScoreForQuizID(int quizId, int pId) throws IOException
	{
		System.out.println();
		System.out.println("All your Scores for the Quiz ID: " + quizId);
		System.out.println();
		Quiz tempQ = serverConnect.getQuizFromID(quizId);
		printRecalledQuizDets(tempQ);
		underline();
		List<PlayerScores> pScores = tempQ.getScores();
		int pScoresSize = pScores.size();
		int total = 0;
		int ave = 0;
		int count = 0;
		//iterate through the quiz scores to find the matching player ID
		for (int i = 0; i < pScoresSize; i++)
		{
				//get all the player's scores for said Quiz 
				if(pScores.get(i).getPlayerId() == pId)
					{
						int score = pScores.get(i).getScore();
						printRecalledPlayerDets(pScores.get(i));
						total = total + score;
						count = count + 1;						
					}				
		}
		//print out average score for the player
		if (count != 0)
		{
			ave = total/count;				
			System.out.println("Your Average Score over " + count + " attempt(s) is: " + ave);
		}
		else
		{
			System.out.println("You have not attempted this quiz");
		}
	}
	
	/**
	 * Prints the players Scores for all quizzes
	 * @param pId
	 * @throws IOException
	 */
	public void allPlayerScoreForQuizID(int pId) throws IOException
	{
		//get list of all quizzes then for each quiz print out the score for the player ID
		HashMap<Integer, Quiz> tempHlist = getAllQuizzes();
		if (tempHlist.isEmpty())
		{
			System.out.println("No Quizzes available");
		}
		else
		{
			for(Map.Entry<Integer, Quiz> entry: tempHlist.entrySet())
			{
				playerScoreForQuizID(entry.getKey(), pId);				
			}
		}		
	}
	

	/**
	 * Gets Top score from certain quiz
	 * @param Id
	 * @throws RemoteException
	 */
	public void getTopScoreForQuizId(int quizId) throws IOException
	{
		System.out.println("\nTop Score for the Quiz ID: " + quizId);
		System.out.println();
		Quiz tempQ = serverConnect.getQuizFromID(quizId);
		//print top score
		if (tempQ.getTopScore() != null)
		{
			printRecalledQuizDets(tempQ);
			underline();
			System.out.println("TOP SCORE: " + tempQ.getTopScore().getScore() + ", Obtained by: " + tempQ.getTopScore().getPlayerName() + "(ID: " + tempQ.getTopScore().getPlayerId() + ")");
		}
		//if no top score saved
		else
		{
			System.out.println("No Top Score Recorded for QuizId: " + tempQ.getQuizId() + " Quiz Name: "  + tempQ.getQuizName());
		}
	} 


	
	/**
	 * Displays Top score for each quiz
	 * @throws IOException
	 */
	public void getAllTopScoresForAllQuizzes() throws IOException
	{
		System.out.println("\nTop Scores for the Available Quizzes");
	
		//iterates through two hashmaps to find the score for the Id provided or each quiz
		 HashMap<Integer, Quiz> tempQHashMap = getAllQuizzes();
			for(Map.Entry<Integer, Quiz> entry: tempQHashMap.entrySet())
			{			
					if (entry.getValue().getTopScore() != null)
					{
						System.out.println();
						printRecalledQuizDets(entry.getValue());
						underline();
						System.out.println("TOP SCORE: " + entry.getValue().getTopScore().getScore() + ", Obtained by: " + entry.getValue().getTopScore().getPlayerName() + "(ID: " + entry.getValue().getTopScore().getPlayerId() + ")");
						System.out.println();
						underline();
					}
					else
					{
						System.out.println("No Top Score Recorded for QuizId: " + entry.getValue().getQuizId() + " Quiz Name: "  + entry.getValue().getQuizName());
					}
			}	
	}
	

	

	/**
	 * Displays all Quizzes available
	 * @throws IOException
	 */
	public void displayAllQuizzes() throws IOException 
	{		
		HashMap<Integer, Quiz> tempHList = getAllQuizzes();		
		if (tempHList != null)
		{
			System.out.println(); 
			System.out.println("List of Quizzes already on system");
			underline();
			for(Map.Entry<Integer, Quiz> entry: tempHList.entrySet())
			{
				System.out.println("QuizID: " + entry.getKey() + ". " + "Quiz Name: " + entry.getValue().getQuizName()); 
			}
			System.out.println(); 
		}
	}
	
	/**
	 * Returns all quizzes on server
	 * @return tempHlist (HashMap)
	 * @throws IOException
	 */
	public HashMap<Integer, Quiz> getAllQuizzes() throws IOException 
	{
		HashMap<Integer, Quiz> tempHlist = null;
		if (quizListEmpty())
		{
			tempHlist = serverConnect.returnAllQuizzes();
		}
		
		return tempHlist;
	}
	
	/**
	 * Returns an int from a string - through user input
	 * this will keep requesting a int to be inserted until one is detected
	 * @param instruction
	 * @return option
	 * @throws IOException
	 */
	public int stringToIntCheck(String instruction) throws IOException
	{		
		boolean complete = false;
		int option = 0;
		while(!complete)
		{
			String opt = readLineViaBuffer(instruction);			
			try{			
					option = Integer.parseInt(opt);	
					//System.out.println("You typed: " + option);
					complete = true;					
				}
	     	catch(NumberFormatException e)
				{
	     			System.out.println();
					System.out.println("That is not an Option - Please insert a number !!!! try again !!!!");
					System.out.println();
				}			
		}		
		return option;
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
	
	
	/**
	 * Checks if the server Quiz list is empty or not
	 * @return true/false
	 * @throws RemoteException
	 * @throws IOException
	 */
	public boolean quizListEmpty() throws RemoteException, IOException
	{
		if(!serverConnect.returnAllQuizzes().isEmpty())
		{	
			return true;
		} 
		else
		{
			System.out.println();
			System.out.println("No Quizzes on server at present - you need to load SetUpClient.java");
			return false;
		}	
	}
	
	/**
	 * Prints a Quiz's ID and Name
	 * @param q
	 * @throws RemoteException
	 */
	public void printRecalledQuizDets(Quiz q) throws RemoteException
	{
		System.out.println("Quiz ID: " + q.getQuizId() + ", Quiz Name: " + q.getQuizName());
	}
	
	/**
	 * Prints a PlayerScores details - ID, Name and Score
	 * @param p
	 * @throws RemoteException
	 */
	public void printRecalledPlayerDets(PlayerScores p) throws RemoteException
	{
		System.out.println("Player Id: " + p.getPlayerId() + ", Name: " + p.getPlayerName() + ", Score = " + p.getScore());	
	}
	
	
	/**
	 * Prints a simple dashed underLine
	 */
	public void underline()
	{
		System.out.println("---------------------------------------------------");
	}
	
	/**
	 * Prints on screen that option of quiz is not available
	 */
	public void quizNotPresent()
	{
		System.out.println();
		System.out.println("Quiz Does Not exist - Going back to the main Menu!!!");
		System.out.println();
	}
	
}
