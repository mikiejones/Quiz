import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
	
	
	public void welcomeMenu() throws IOException
	{
		
		playerSetup();		
		
		
		boolean complete = false;
		while(!complete)
		{
			System.out.println("************* Welcome to Mike's Quiz Server *************");
			System.out.println();
			System.out.println("What would you like to do? ");
			System.out.println("1: Play a quiz?");
			System.out.println("2: Show all available Quizzes");
			System.out.println("3: Show all your scores for all Quizzes");
			System.out.println("0: Exit");
		
			String opt = readLineViaBuffer("Please enter the required option: ");
			//must deal with non int returned here
			int option = Integer.parseInt(opt);	
			
			switch(option) 
			{
				case 0: System.out.println("Exiting: Thank you and good bye");
						complete = true;
						System.exit(0);
						break;
						
				case 1: System.out.println("*** DEBUG **** Play a quiz");
						playQuiz();
						break;
						
				case 2: System.out.println("*** DEBUG **** Get All Available Quizzes");
						displayAllQuizzes();
						break;
					
						
				case 3: System.out.println("*** DEBUG **** Show all your scores");
						playerScoreForEachQuiz();
						break;
				
				default: System.out.println("*** DEBUG **** Not an Option, try again");
						break;
			}
		}		
	}
	
	
	
	public void playerSetup() throws IOException
	{
		String opt = readLineViaBuffer("Welcome, are you a returning player with an ID? y/n: ");
		if (opt.toLowerCase().equals("y"))
		{
			String name = readLineViaBuffer("Please enter your name.");
			String ident = readLineViaBuffer("Please enter your Id.");
			int Id = Integer.parseInt(ident);	
			//serverConnect.returningPlayer(name, Id);
			player = serverConnect.getPlayerFromId(Id);
		}
		else
		{
			String name = readLineViaBuffer("Please enter your name.");
			int Id = serverConnect.addNewPlayer(name);	
			System.out.println();
			System.out.println("Your Player ID is: " + Id + ". Please write this down and Keep it safe.");
			System.out.println();
			player = serverConnect.getPlayerFromId(Id);
			//addNewPlayer
		}		
	}
	
	/**
	 * Displays player's score for each quiz
	 * @throws IOException
	 */
	public void playerScoreForEachQuiz() throws IOException
	{
		System.out.println("\nAll Scores for the Available Quizzes");
	
		
		 HashMap<Integer, Quiz> tempQHashMap = getAllQuizzes();
		 int playerId = player.getId();
			for(Map.Entry<Integer, Quiz> entry: tempQHashMap.entrySet())
			{				
				HashMap<Integer, Integer> tempPlayScore = entry.getValue().getAllPlayerScores();
				for(Map.Entry<Integer, Integer> innerEntry: tempPlayScore.entrySet())
				{
					if(innerEntry.getKey() == playerId)
					{
						System.out.println("Quiz ID: " + entry.getValue().getQuizId() + ", Name: " + entry.getValue().getQuizName() + " - Your Score: " + innerEntry.getValue());
					}
					else
					{
						System.out.println("Quiz ID: " + entry.getValue().getQuizId() + ", Name: " + entry.getValue().getQuizName() + "Not attempted");
					}
					
				}
				
			}	
		System.out.println();
	}
	
	
	/**
	 * 
	 * @throws IOException
	 */
	public void playQuiz() throws IOException 
	{
		System.out.println(); 
		displayAllQuizzes();
		System.out.println();
		String opt = readLineViaBuffer("Please enter the ID of the Quiz in which you wish to play: ");
		//must deal with non int returned here
		int option = Integer.parseInt(opt);	
		
		
		Quiz temp = serverConnect.getQuizFromID(option);
		
		System.out.println("Welcome to " + temp.getQuizName());
		List <Question> quests = temp.getQuestions();
		int questSize = quests.size();
		
		for (int i = 0; i < questSize; i++)
		{
			Question tempQuest = quests.get(i);
			System.out.println("Question: " + i);
			System.out.println(tempQuest.getQuestion());
			String[] tempQuestArray = tempQuest.getAnswers();
			System.out.println((0 + 1) + ":"  + tempQuestArray[0]);
			System.out.println((1 + 1) + ":"  + tempQuestArray[1]);
			System.out.println((2 + 1) + ":"  + tempQuestArray[2]);
			int correctAnswer = tempQuest.getCorrectAnswer();
			String ans = readLineViaBuffer("Please enter Correct Answer: ");
			//must deal with non int returned here
			int answer = Integer.parseInt(ans);	
			
			if (answer == correctAnswer)
			{
				System.out.println("That answer is Correct");
				playerScore = playerScore + 1;
			}
			else
			{
				System.out.println("That answer is wrong");
			}
			
		}
		
		//HashMap<Integer, Integer> tempScoreHashMap = temp.getAllPlayerScores();
		temp.addToPlayerScore(player.getId(), playerScore);
		System.out.println("End of Quiz. Your Score was " + playerScore);
		//reset score for next go or set of questions.
		playerScore = 0;
	}
	

	/**
	 * Displays all Quizzes available
	 * @throws IOException
	 */
	public void displayAllQuizzes() throws IOException 
	{
		HashMap<Integer, Quiz> tempHlist = getAllQuizzes();
		System.out.println(); 
		System.out.println("List of Quizzes already on system");
		System.out.println("---------------------------------");
		for(Map.Entry<Integer, Quiz> entry: tempHlist.entrySet())
		{
			System.out.println("QuizID: " + entry.getKey() + ". " + "Quiz Name: " + entry.getValue().getQuizName()); 
		}
		System.out.println(); 
	}
	
	
	/**
	 * returns all quizzes on server
	 * @return
	 * @throws IOException
	 */
	public HashMap<Integer, Quiz> getAllQuizzes() throws IOException 
	{
		HashMap<Integer, Quiz> tempHlist = serverConnect.returnAllQuizzes();
		return tempHlist;
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