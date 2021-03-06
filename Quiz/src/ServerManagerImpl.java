
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class ServerManagerImpl extends UnicastRemoteObject implements ServerManager, Serializable, Runnable 
{
	private HashMap<Integer, Quiz> quizMap = new HashMap<Integer, Quiz>(); 
	private HashMap<Integer, Player> players = new HashMap<Integer, Player>(); 
	
	private static int playerId = 0;
	private static int quizId = 0;			
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected ServerManagerImpl() throws RemoteException 
	{
		super();
		
		//shutdown hook - to flush all data upon completion
		//ShutDown hook - obtained and Modified from following tutorial site...
		//http://hellotojavaworld.blogspot.co.uk/2010/11/runtimeaddshutdownhook.html
		  Runtime.getRuntime().addShutdownHook(new Thread() 
	        {
	        	
	            public void run() 
	            {      
	            	System.out.println("Server saving Data.......");
	            	try {
	            			//flush all data to two files
							masterFlush();
							System.out.println("Data Saving Complete   :}");
						} catch (RemoteException e)
						{
							e.printStackTrace();
						} catch (IOException e)
						{
							e.printStackTrace();
						}
	            }
	        });
		
		//call quizzes from file
		recallQuizzesFromFile();
		readPlayersAndScoresFromFile();
	}

	@Override
	//Synched to ensure the QuizID is thread Safe
    public synchronized int addNewQuiz(String name, int questionNum) throws RemoteException 
	{
		if(name == null || questionNum == 0)
		{
			throw new NullPointerException("One of the Parameters is Null");
		} 
		else
		{
			QuizImpl quizTemp = new QuizImpl(name, questionNum);
			//creates quiz ID after object created
			int id = createQuizId(quizTemp);
			//add quiz to HashMap - quizMap
			quizMap.put(id, quizTemp);
			//now increase quizID count by 1
			increaseQuizId();		
			return id;
		}
	}
	
	@Override
	public void addQuizFromFile(int Id, String name, int questionNum) throws RemoteException 
	{
		if(name == null || questionNum == 0)
		{
			throw new NullPointerException("One of the Parameters is Null");
		} 
		else
		{
			QuizImpl quizTemp = new QuizImpl(name, questionNum);
			quizMap.put(Id, quizTemp);
			quizTemp.createQuizId(Id);			
			//adjust quizID for newly created quizzes - every time quiz is recalled ensure quizId grows with it.
			increaseQuizId();
		}
	}	
	

	/**
	 * increases the QuizID ready for newly created quizzes
	 */
	public void increaseQuizId()
	{
		quizId = quizId + 1;	
	}
	

	@Override
	public int createQuizId(Quiz quizTemp) throws RemoteException 
	{
		if (quizTemp == null) 
		{
			throw new IllegalArgumentException("Quiz does not exist");
		}
		//updates quiz with ID
		quizTemp.createQuizId(quizId);		
		return quizId;
	}

	
	@Override
	public Quiz getQuizFromID(int Id) throws RemoteException 
	{	
		Quiz quizFound = null;
		if (quizMap.containsKey(Id))
		{	
			quizFound = quizMap.get(Id);		
			System.out.println("****DEBUG**** Quiz: " + quizFound.getQuizName() + " found");
		}	
		else
		{	
				throw new IllegalArgumentException ("Quiz " + Id + " Does not exist");
		}			
		return quizFound;
	}	
	
	
	@Override
	public void addQuestionToQuiz(Quiz quiz, Question q) throws RemoteException 
	{
		if (quiz == null || q == null) 
		{
			throw new IllegalArgumentException("Quiz/Question does not exist");
		}
		quiz.addQuestionToQuiz(q);
		
	}

	@Override
	public void addQuestionToQuiz(int Id, Question q) throws RemoteException 
	{
		if (q == null) 
		{
			throw new IllegalArgumentException("Question does not exist");
		}
		Quiz temp = this.getQuizFromID(Id);
		temp.addQuestionToQuiz(q);
		
	}

	@Override
	public HashMap<Integer, Quiz> returnAllQuizzes() throws RemoteException 
	{		
		return quizMap;
	}

	@Override
	//Synched to ensure the PlayerID is thread Safe
	public synchronized int addNewPlayer(String name) throws RemoteException 
	{
		if(name == null)
		{
			throw new NullPointerException("Name Parameters is Null");
		} 
		else
		{
			//create ID 
			int Id = playerId;
			Player newPlayer = new Player(Id, name);
			playerId = playerId + 1;
			players.put(Id, newPlayer);
			return Id;
		}
	}

	@Override
	public void addPlayersFromFile(String name, int Id) throws RemoteException 
	{
		if(name == null)
		{
			throw new NullPointerException("Name Parameters is Null");
		} 
		else
		{
			Player returningPlayer = new Player(Id, name);
			players.put(Id, returningPlayer);
			playerId = playerId + 1;
			//no need to return ID here - this is from file	
		}
	}
	

	@Override
	public HashMap<Integer, Player> returnAllPlayers() throws RemoteException 
	{		
		return players;
	}
		
	

	@Override
	public Player getPlayerFromId(int Id) throws RemoteException 
	{
		Player tempPlayer = null;
		if(!players.isEmpty())
		{
			for(Map.Entry<Integer, Player> entry: players.entrySet())
			{
				if (entry.getKey() == Id)
				{
					tempPlayer = entry.getValue();
					System.out.println("***DEBUG*** Player ID: " + entry.getKey() + ". " + "Player Name: " + entry.getValue().getName());
				}
			}
		}
		return tempPlayer;	
	}
	

	@Override
	public void masterFlush() throws RemoteException, IOException
	{
		flush();			
		flushPlayers();
	}
	
	
	/**
	 * Saves Quiz data to file - this is automatically done when server shuts down
	 * @throws IOException
	 * @throws RemoteException
	 */
	//removed from user sight
	public void flush() throws IOException, RemoteException
	{
		if (checkFileExists("Quiz.txt"))
		{			
			FileWriter fileWite = new FileWriter("Quiz.txt");
			BufferedWriter bufferWrite = new BufferedWriter(fileWite);				
			FileReader file = new FileReader( "Quiz.txt");
			BufferedReader buffer = new BufferedReader(file);
			String line = null;
			while ( (line = buffer.readLine()) != null)		
			{
				bufferWrite.newLine();
			}
		
			HashMap<Integer, Quiz> tempQuizMap = returnAllQuizzes();
			int hMSize = tempQuizMap.size(); 
			//System.out.println("***DEBUG*** HasMap size is: " + hMSize); 
			bufferWrite.write(hMSize + "," + " Quizzes"); 
			bufferWrite.newLine();
			for(Map.Entry<Integer, Quiz> entry: tempQuizMap.entrySet())
			{	
					//System.out.println("***DEBUG*** Writing Quiz: " + entry.getValue().getQuizName() + " to file");
					bufferWrite.write(entry.getValue().getQuizId() + "," + entry.getValue().getQuizName() + "," + entry.getValue().getQuestionTotal()); 
					bufferWrite.newLine();
					//gets list of questions associated to this quiz
					List <Question> quests = entry.getValue().getQuestions();
					int questSize = quests.size();
					//add each question, answer1,2,3 and correctAnswer to file
					for (int i = 0; i < questSize; i++)
					{
						Question tempQuest = quests.get(i);	
						String[] answers = tempQuest.getAnswers();
						bufferWrite.write(tempQuest.getQuestion() + "," + answers[0] + "," + answers[1] + "," + answers[2] + "," + tempQuest.getCorrectAnswer()); 
						bufferWrite.newLine();
					}					
			}	
				
				bufferWrite.close();
				buffer.close();
		}	
		else
		{
			System.out.println("File does not exist");
		}
	}
	
	/**
	 * Saves Player data and playerScores data to file - this is automatically done when server shuts down
	 * @throws RemoteException
	 * @throws IOException
	 */
	public void flushPlayers() throws RemoteException, IOException
	{
		if (checkFileExists("PlayerStats.txt"))
		{			
			FileWriter fileWite = new FileWriter("PlayerStats.txt");
			BufferedWriter bufferWrite = new BufferedWriter(fileWite);				
			FileReader file = new FileReader( "PlayerStats.txt");
			BufferedReader buffer = new BufferedReader(file);
			String line = null;
			while ( (line = buffer.readLine()) != null)		
			{
				bufferWrite.newLine();
			}
		
			HashMap<Integer, Player> tempPlayerMap = returnAllPlayers();
			int hMSize = tempPlayerMap.size(); 

			bufferWrite.write(hMSize + "," + " Players"); 
			bufferWrite.newLine();
			//get all players and their ID and write to file first
			for(Map.Entry<Integer, Player> entry: tempPlayerMap.entrySet())
			{	
					bufferWrite.write(entry.getValue().getId() + "," + entry.getValue().getName()); 
					bufferWrite.newLine();
			}		
		
			//get all quizzes and their scores and add write them to file
			 HashMap<Integer, Quiz> tempQHashMap =  returnAllQuizzes();
			 int numOfQuizzes = tempQHashMap.size();
			 //first add the number of quizzes to iterate through on recalling
			 bufferWrite.write(numOfQuizzes + "," + "Number of Quizzes");
			 bufferWrite.newLine();
				for(Map.Entry<Integer, Quiz> entry: tempQHashMap.entrySet())
				{		
					//get list of all scores for each quiz
					List<PlayerScores> allScores = entry.getValue().getScores();
					int allScoresSize = allScores.size();
					bufferWrite.write("QuizID" + "," + entry.getValue().getQuizId() + "," + "Quiz Attempts" + "," + allScoresSize + ", Quiz Name: " + entry.getValue().getQuizName());
					bufferWrite.newLine();
					for (int i = 0; i < allScoresSize; i++)
					{
							bufferWrite.write(allScores.get(i).getPlayerId() + "," + allScores.get(i).getPlayerName() + "," + allScores.get(i).getScore()); 
							bufferWrite.newLine();
					}
				}			
				bufferWrite.close();
				buffer.close();
		}	
		else
		{
			System.out.println("File does not exist");
		}
	}
	
	
	/**
	 * Reads Quiz data and player/playerScore data from file. 
	 * This is done automatically upon server creation.	 *
	 * @throws RemoteException
	 */
	public void readPlayersAndScoresFromFile() throws RemoteException
	{
		System.out.println("***DEBUG*** Reading players and scores from file");
		System.out.println();
		   String fileName = "PlayerStats.txt";
		   try {	
			       //possibly dont need this as us createNewfile() which checks for you
				   if (!checkFileExists(fileName))
			        {
			        	File file = new File(fileName);
			        	file.createNewFile();
			        } 

		   		Scanner s = null;
	            s = new Scanner(new BufferedReader(new FileReader(fileName)));
	            String line = null; 	
	            //check to see file has contents - then iterate through contacts
	            if(s.hasNext())
	            {	   
	            	line = s.nextLine();
	            	//Read and save to server all players
	            	int playersFound = itemsInFileFound(line);
					for (int i = 0; i < playersFound; i++)
					{
						 line = s.nextLine();	
						 getPlayerFromLine(line);								
					}	
					
					//now read Quiz ID and add that to scores
					line = s.nextLine();
					int quizzesFound = itemsInFileFound(line);
					for (int i = 0; i < quizzesFound; i++)
					{
						line = s.nextLine();
						int [] result = getQuizIdAndAttemptsFromFile(line);
						//get quiz id from line above
						Quiz tempQuiz = getQuizFromID(result[0]);	
						System.out.println("***DEBUG*** Quiz: " + tempQuiz.getQuizId() + " found");
						//Iterate through the scores adding them to quiz
						for (int j = 0; j < result[1]; j++)
						{
							line = s.nextLine();
							//create playerscore from file
							PlayerScores tempPscores = returnPlayerScoresFromFile(result[1], line);
							//add the above playerscore to the designated quiz
							String holder = tempQuiz.addToPlayerScore(tempPscores);							
						}						
					}              
	            }
	            else
	            {
	            	System.out.println("The file: " + fileName + " is empty");
	            	  s.close();
	            }
	   		}
	        catch(IOException e)
			{
					System.out.println("An error has occurred, Check file is not in use");				
			}
	}
	
	
	/**
	 * Reads the QuizId and Attempts from File
	 * @param line
	 * @return result array {quizID, quizAttempts}.
	 */
	public int[] getQuizIdAndAttemptsFromFile(String line)
	{
		String[] stringArray = line.split(",");
		int quizId = Integer.parseInt(stringArray[1].trim());
		int quizAttempts = Integer.parseInt(stringArray[3].trim());	
		int[] result = {quizId, quizAttempts};
		return result;
	}
	
	/**
	 * Returns PlayerScores from File
	 * @param quizId
	 * @param line
	 * @return p (PlayerScores)
	 */
	public PlayerScores returnPlayerScoresFromFile(int quizId, String line)
	{
		String[] stringArray = line.split(",");
		int playerID = Integer.parseInt(stringArray[0].trim());
		String pName = stringArray[1].trim();
		int score = Integer.parseInt(stringArray[2].trim());
		PlayerScores p = new PlayerScores(quizId, playerID, pName, score);
		return p;
	}
	
	/**
	 * Identifies Player name and ID from the given string line
	 * Then calls the addPlayersFromFile method, adding that player to the server 
	 * @param line
	 * @throws RemoteException
	 */
	public void getPlayerFromLine(String line) throws RemoteException
	{
		String[] stringArray = line.split(",");
		int Id = Integer.parseInt(stringArray[0].trim());
		String pName = stringArray[1].trim();
		addPlayersFromFile(pName, Id);		
	}
	
	
	/**
	 * Recalls and adds quizzes from file to the server
	 * @throws RemoteException
	 */
	public void recallQuizzesFromFile() throws RemoteException
	{
		   String fileName = "Quiz.txt";
		   try {	//possible dont need this as us createNewfile() which checks for you
				   if (!checkFileExists(fileName))
			        {
			        	File file = new File(fileName);
			        	file.createNewFile();
			        } 

		   		Scanner s = null;
	            s = new Scanner(new BufferedReader(new FileReader(fileName)));
	            String line = null; 	
	            //check to see file has contents - then iterate through contacts
	            if(s.hasNext())
	            {	   
	            	line = s.nextLine();
	            	//System.out.println("line = " + line + " Start of Meetings Method");
	            	int QuizzesFound = itemsInFileFound(line);
					for (int i = 0; i < QuizzesFound; i++)
					{
						 line = s.nextLine();	
						 //System.out.println("***DEBUG*** " + line);	
						 //call create contact method below - to use details from each line found
						int[] quest = this.createQuizFromFile(line);
						for (int j = 0; j < quest[1]; j++)
						{
							line = s.nextLine();
							addQuestionsFromFileToQuiz(quest[0],line);
						}												
					}	             
	            }
	            else
	            {
	            	System.out.println("The file: " + fileName + " is empty");
	            	  s.close();
	            }
	   		}
	        catch(IOException e)
			{
					System.out.println("An error has occurred, Check file is not in use");				
			}		   
	}

	/**
	 *  Checks a line in the CSV/txt file for the first int.
	 *  This is how many records of that type to iterate through 
	 * @param line
	 * @return itemsFound (Int)
	 */
	public int itemsInFileFound(String line)
	{	
		if(line == null)
		{
			throw new NullPointerException("line read from File appears to be Null");
		} 
		else
		{
			line = line.trim();					
			String[] stringArray = line.split(",");
			int itemsFound = Integer.parseInt(stringArray[0].trim());
			System.out.println("***DEBUG*** Items Found: " + itemsFound);	
			return itemsFound;
		}
	}
	
	/**
	 * Creates a new quiz from a saved quiz on file.
	 * @param lineRead
	 * @return int[] QuizID and Number of questions in that quiz
	 * @throws RemoteException
	 * @throws FileNotFoundException
	 */
	public int[] createQuizFromFile(String lineRead) throws RemoteException, FileNotFoundException
	{
		if(lineRead == null)
		{
			throw new NullPointerException("line read from File appears to be Null");
		} 
		else
		{
			//need to read the first line to establish how many questions per quiz.
			String[] stringArray = lineRead.split(",");
			int id = Integer.parseInt(stringArray[0].trim());
			String quizName = stringArray[1].trim();
			int numberOfQuests = Integer.parseInt(stringArray[2].trim());
			addQuizFromFile(id, quizName, numberOfQuests);
	        int[] quizInit = {id, numberOfQuests};  
			return quizInit;
		}
				 
	}
	
	/**
	 * Adds questions to a quiz that was returned from File
	 * @param quizId
	 * @param lineRead
	 * @throws RemoteException
	 */
	public void addQuestionsFromFileToQuiz(int quizId, String lineRead) throws RemoteException
	{
		if(lineRead == null)
		{
			throw new NullPointerException("line read from File appears to be Null");
		} 
		else
		{
			String[] stringArray = lineRead.split(",");
			String question = stringArray[0].trim();
			String ansOne = stringArray[1].trim();
			String ansTwo = stringArray[2].trim();
			String ansThree = stringArray[3].trim();
			String corAnswer = stringArray[4].trim();
			int answer = Integer.parseInt(corAnswer);
			
			Question q = new Question(question, ansOne, ansTwo, ansThree, answer);
			addQuestionToQuiz(quizId, q);
		}
	}
	
	/**
	 * Checks the provided File (String) actually exists, if not it creates one
	 * @param fileName (String)
	 */
	public boolean checkFileExists(String fileName) throws RemoteException
	{	
		//No option to check the directory as do not know what it is -
		//if user interface then this would be an option
		File file = new File(fileName);
		if(file.exists())
		{
			return true;
		}		
		return false;	
	}

	@Override
	public void run() 
	{				
	};

}
