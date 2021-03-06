import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;


public class TestQuizSetUp 
{
	
	ServerManager newManager; 
	
	@Before
	public void startUp() throws RemoteException
	{
		//newManager = new ServerManagerImpl();		
	}
	
	
	
	
	/*
	@Test
	public void test() throws IOException 
	{
		//ServerManager newManager = new ServerManagerImpl();
		
		String quest = null; 
	    String ansOne = null; 
	    String ansTwo = null; 
		String ansThree = null; 
		String correctAns = null;
	    int answer = 0;
		
	    String qName = readLineViaBuffer("Please Enter a Name for your Quiz: ");	    
		String qAmount = readLineViaBuffer("Please Enter quantity of Questions: ");
	    int quantOfQuestions = Integer.parseInt(qAmount);	    
	    
		int returnedID = newManager .addNewQuiz(qName, quantOfQuestions);
	    
	    Quiz tempQuiz = newManager.getQuizFromID(returnedID);
	    
	    //QuizImpl quiz1 = new QuizImpl(qName, quantOfQuestions);
	    
	    
		
		for (int i = 0; i < quantOfQuestions; i++)
		{
			System.out.println("Question: " + (i + 1));
			quest = readLineViaBuffer("Please Enter a Question: ");
		 	ansOne = readLineViaBuffer("Please Enter 1st Answer: ");
		   	ansTwo = readLineViaBuffer("Please Enter 2nd Answer: ");
		  	ansThree = readLineViaBuffer("Please Enter 3rd Answer: ");
			correctAns = readLineViaBuffer("Please Enter which is the correct Answer: 1,2, or 3: ");
		    answer = Integer.parseInt(correctAns);
		    Question questTemp = new Question(quest, ansOne, ansTwo, ansThree, answer);
		    newManager.addQuestionToQuiz(tempQuiz, questTemp);
		}
		
		
		List<Question> temp = tempQuiz.getQuestions();
		
		
		for (int j= 0; j< temp.size(); j++)
		{
			System.out.println("Question " + (j+1) + ": ");
			System.out.println(temp.get(j).getQuestion());
		}
		
		int num = temp.get(0).getCorrectAnswer();
		String[] answers = temp.get(0).getAnswers();
		System.out.println("Corrrect Answer is: " + num);
		System.out.println("Answer is: " + answers[num-1]);
		assertEquals(num, 1);
			
	}

	
	
	
	@Test
	public void setUpQuestions() throws IOException 
	{
		
				System.out.println();
				System.out.println("************* Quiz Setup *************");
				System.out.println();
				String qName = readLineViaBuffer("Please Enter a Name for your Quiz: ");	    
				String qAmount = readLineViaBuffer("Please Enter quantity of Questions: ");
				int quantOfQuestions = Integer.parseInt(qAmount);	
				
				int returnedID = newManager.addNewQuiz(qName, quantOfQuestions);
				Quiz tempQuiz = newManager.getQuizFromID(returnedID);
				adQuestions(quantOfQuestions, tempQuiz);
				List<Question> temp = tempQuiz.getQuestions();
				assertEquals(temp.size(), quantOfQuestions);
	}
	
	*/
	
	/*
	@Test
	public void savePlayerScores() throws IOException
	{
		int Id0 = newManager.addNewPlayer("John");
		int Id1 =  newManager.addNewPlayer("Paul");
		int Id2 =  newManager.addNewPlayer("Mike");
		int Id3 =  newManager.addNewPlayer("Tom");
		
		
		
		PlayerScores pScore0 = new PlayerScores(0,Id0, newManager.getPlayerFromId(Id0).getName(), 3);
		PlayerScores pScore1 = new PlayerScores(0,Id1,newManager.getPlayerFromId(Id1).getName(), 6);
		PlayerScores pScore2 = new PlayerScores(0,Id2,newManager.getPlayerFromId(Id2).getName(), 5);
		PlayerScores pScore3 = new PlayerScores(0,Id3,newManager.getPlayerFromId(Id3).getName(), 13);
		
		System.out.println();
		System.out.println("************* Quiz Setup *************");
		System.out.println();
		Quiz tempQuiz = null;
		for (int i = 0; i< 2; i++)
		{
			String qName = readLineViaBuffer("Please Enter a Name for your Quiz: ");	    
			String qAmount = readLineViaBuffer("Please Enter quantity of Questions: ");
			int quantOfQuestions = Integer.parseInt(qAmount);	
			
			int returnedID = newManager.addNewQuiz(qName, quantOfQuestions);
			tempQuiz = newManager.getQuizFromID(returnedID);
			adQuestions(quantOfQuestions, tempQuiz);
			//List<Question> temp = tempQuiz.getQuestions();
		};
		flush();
		//assertEquals(temp.size(), quantOfQuestions);	
		

		tempQuiz.addToPlayerScore(pScore0);
		tempQuiz.addToPlayerScore(pScore1);
		tempQuiz.addToPlayerScore(pScore2);
		tempQuiz.addToPlayerScore(pScore3);
		
		flushPlayers();
		
	}
	*/
	
	/*
	@Test
	public void readPlayersAndScoresFromFile() throws RemoteException
	{
		   String fileName = "PlayerStats.txt";
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
	            	//Read and save to server all players
	            	int playersFound = itemsInFileFound(line);
					for (int i = 0; i < playersFound; i++)
					{
						 line = s.nextLine();	
						 getPlayerFromLine(line);								
					}	
					
					//now read Quiz ID and add that to scores
					
					int quizzesFound = itemsInFileFound(line);
					for (int i = 0; i < quizzesFound; i++)
					{
						line = s.nextLine();
						int [] result = getQuizIdAndAttemptsFromFile(line);
						Quiz tempQuiz = newManager.getQuizFromID(result[0]);	
						for (int j = 0; j < result[1]; j++)
						{
							line = s.nextLine();
							//create playerscore from file
							PlayerScores tempPscores = returnPlayerScoresFromFile(result[1], line);
							//add the above playerscore to the designated quiz
							tempQuiz.addToPlayerScore(tempPscores);
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
		   
		    HashMap<Integer, Quiz> tempQuizMap = newManager.returnAllQuizzes();
			//int hMSize = tempQuizMap.size();
			String[] quizNames = new String[10];
			for(Map.Entry<Integer, Quiz> entry: tempQuizMap.entrySet())
			{	
				quizNames[entry.getKey()] = entry.getValue().getQuizName();
				System.out.println("QuizId: " + entry.getValue().getQuizId() + ", QuizName: " +  entry.getValue().getQuizName());
				List<Question> tempQ =  entry.getValue().getQuestions();
				int tempQsize = tempQ.size();
				for (int i = 0; i < tempQsize; i++)
				{
					System.out.println("Question: " + tempQ.get(i).getQuestion());
				}
			}
			
			
		  assertEquals(quizNames[0], "mikes quiz");
	}
	*/
	
	/*
	public int[] getQuizIdAndAttemptsFromFile(String line)
	{
		String[] stringArray = line.split(",");
		int quizId = Integer.parseInt(stringArray[1].trim());
		int quizAttempts = Integer.parseInt(stringArray[3].trim());	
		int[] result = {quizId, quizAttempts};
		return result;
	}
	*/
	
	
	/*
	public PlayerScores returnPlayerScoresFromFile(int quizId, String line)
	{
		String[] stringArray = line.split(",");
		int playerID = Integer.parseInt(stringArray[0].trim());
		String pName = stringArray[1].trim();
		int score = Integer.parseInt(stringArray[2].trim());
		PlayerScores p = new PlayerScores(quizId, playerID, pName, score);
		return p;
	}
	*/
	
	/*
	public void getPlayerFromLine(String line) throws RemoteException
	{
		String[] stringArray = line.split(",");
		int Id = Integer.parseInt(stringArray[0].trim());
		String pName = stringArray[1].trim();
		newManager.addPlayersFromFile(pName, Id);
		
	}
	*/
	
	
	/*
	@Test
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
		   
		    HashMap<Integer, Quiz> tempQuizMap = newManager.returnAllQuizzes();
			//int hMSize = tempQuizMap.size();
			String[] quizNames = new String[10];
			for(Map.Entry<Integer, Quiz> entry: tempQuizMap.entrySet())
			{	
				quizNames[entry.getKey()] = entry.getValue().getQuizName();
				System.out.println("QuizId: " + entry.getValue().getQuizId() + ", QuizName: " +  entry.getValue().getQuizName());
				List<Question> tempQ =  entry.getValue().getQuestions();
				int tempQsize = tempQ.size();
				for (int i = 0; i < tempQsize; i++)
				{
					System.out.println("Question: " + tempQ.get(i).getQuestion());
				}
			}
			
			
		  assertEquals(quizNames[0], "mikes quiz");
	}
*/

	/**
	 *  Checks a line in the CSV/txt file for the first int.
	 *  This is how many records of that type to iterate through 
	 * @param line
	 * @return itemsFound (Int)
	 */
	/*
	public int itemsInFileFound(String line)
	{		
		line = line.trim();					
		String[] stringArray = line.split(",");
		int itemsFound = Integer.parseInt(stringArray[0].trim());
		//System.out.println("***DEBUG*** Items Found: " + itemsFound);	
		return itemsFound;
	}
	*/
	
	/*public int[] createQuizFromFile(String lineRead) throws RemoteException, FileNotFoundException
	{
		//need to read the first line to establish how many questions per quiz.
		String[] stringArray = lineRead.split(",");
		int id = Integer.parseInt(stringArray[0].trim());
		String quizName = stringArray[1].trim();
		int numberOfQuests = Integer.parseInt(stringArray[2].trim());
		newManager.addQuizFromFile(id, quizName, numberOfQuests);
        int[] quizInit = {id, numberOfQuests};  
		return quizInit;
				 
	}
	*/
	/*
	public void addQuestionsFromFileToQuiz(int quizId, String lineRead) throws RemoteException
	{
		String[] stringArray = lineRead.split(",");
		String question = stringArray[0].trim();
		String ansOne = stringArray[1].trim();
		String ansTwo = stringArray[2].trim();
		String ansThree = stringArray[3].trim();
		String corAnswer = stringArray[4].trim();
		int answer = Integer.parseInt(corAnswer);
		
		Question q = new Question(question, ansOne, ansTwo, ansThree, answer);
		newManager.addQuestionToQuiz(quizId, q);
	}
	*/
	
	/*
	
	@Test
	public void saveQuizzesToFile() throws IOException
	{
		System.out.println();
		System.out.println("************* Quiz Setup *************");
		System.out.println();
		String qName = readLineViaBuffer("Please Enter a Name for your Quiz: ");	    
		String qAmount = readLineViaBuffer("Please Enter quantity of Questions: ");
		int quantOfQuestions = Integer.parseInt(qAmount);	
		
		int returnedID = newManager.addNewQuiz(qName, quantOfQuestions);
		Quiz tempQuiz = newManager.getQuizFromID(returnedID);
		adQuestions(quantOfQuestions, tempQuiz);
		List<Question> temp = tempQuiz.getQuestions();
		
		flush();
		assertEquals(temp.size(), quantOfQuestions);	
		
	}
	*/
	
	/*
	public void flush() throws RemoteException, IOException
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
		
			HashMap<Integer, Quiz> tempQuizMap = newManager.returnAllQuizzes();
			int hMSize = tempQuizMap.size(); 
			//System.out.println("***DEBUG*** HasMap size is: " + hMSize); 
			bufferWrite.write(hMSize + "," + " Quizzes"); 
			bufferWrite.newLine();
			for(Map.Entry<Integer, Quiz> entry: tempQuizMap.entrySet())
			{	
					//System.out.println("***DEBUG*** Writing Contact: " + entry.getValue().getName() + " to file");
					bufferWrite.write(entry.getValue().getQuizId() + "," + entry.getValue().getQuizName() + "," + entry.getValue().getQuestionTotal()); 
					bufferWrite.newLine();
					//gets list of questions associated to this quiz
					List <Question> quests = entry.getValue().getQuestions();
					int questSize = quests.size();
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
	*/
	
	
	
	/*
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
		
			HashMap<Integer, Player> tempPlayerMap = newManager.returnAllPlayers();
			int hMSize = tempPlayerMap.size(); 
			
			
			//System.out.println("***DEBUG*** HasMap size is: " + hMSize); 
			bufferWrite.write(hMSize + "," + " Players"); 
			bufferWrite.newLine();
			for(Map.Entry<Integer, Player> entry: tempPlayerMap.entrySet())
			{	
					//System.out.println("***DEBUG*** Writing Contact: " + entry.getValue().getName() + " to file");
					bufferWrite.write(entry.getValue().getId() + "," + entry.getValue().getName()); 
					bufferWrite.newLine();
			}		
		
		
			//get all quizzes and their scores and add write them to file
			 HashMap<Integer, Quiz> tempQHashMap =  newManager.returnAllQuizzes();
			 int numOfQuizzes = tempQHashMap.size();
			 //first add the number of quizzes to iterate through on recalling
			 bufferWrite.write(numOfQuizzes + "," + "Number of Quizzes");
			 bufferWrite.newLine();
				for(Map.Entry<Integer, Quiz> entry: tempQHashMap.entrySet())
				{		
					//get list of all scores for each quiz
					List<PlayerScores> allScores = entry.getValue().getScores();
					int allScoresSize = allScores.size();
					//System.out.println("QuizID: " + "," + entry.getValue().getQuizId() + "," + allScoresSize + ", Quiz Name: " + entry.getValue().getQuizName());
					//System.out.println("---------------------------------------------------");
					bufferWrite.write("QuizID" + "," + entry.getValue().getQuizId() + "," + "Quiz Attempts" + "," + allScoresSize + ", Quiz Name: " + entry.getValue().getQuizName());
					bufferWrite.newLine();
					for (int i = 0; i < allScoresSize; i++)
					{
							System.out.println("PlayerId: " + allScores.get(i).getPlayerId() + ", Score = " + allScores.get(i).getScore());
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
	*/
	
	
	
		/**
		 * Checks the provided File (String) actually exists, if not it creates one
		 * @param fileName (String)
		 */
	/*
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
		};
	
	
	*/
	
	
	
	/**
	 * Creates n number of questions in designated quiz
	 * @param num
	 * @param tempQuiz
	 * @throws IOException
	 */
		/*
	public void adQuestions(int num, Quiz tempQuiz) throws IOException
	{
		for (int i = 0; i < num; i++)
		{
			System.out.println("Question: " + (i + 1));
			String quest = readLineViaBuffer("Please Enter a Question: ");
			String ansOne = readLineViaBuffer("Please Enter 1st Answer: ");
			String ansTwo = readLineViaBuffer("Please Enter 2nd Answer: ");
			String ansThree = readLineViaBuffer("Please Enter 3rd Answer: ");
			String correctAns = readLineViaBuffer("Please Enter which is the correct Answer: 1,2, or 3: ");
			int answer = Integer.parseInt(correctAns);
		    Question questTemp = new Question(quest, ansOne, ansTwo, ansThree, answer);
		    newManager.addQuestionToQuiz(tempQuiz, questTemp);		
		}
		
	}
	
	*/
	
	
	
	
	@Test
	public void inputErrorCheck() throws IOException
	{
		int option = stringToIntCheck("Please enter the required option: ");
				
		System.out.println("option Chosen: " + option);
	}
	

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
					System.out.println("Wront format - try again");
				}			
		}		
		return option;
	}
	
	
	//Saves repeating the request for input - requires the instructions of what your are asked to type
	//require the throws IOException to allow buffer reader to work 
	public String readLineViaBuffer(String instructions) throws IOException  
	{
		BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
	    System.out.print(instructions);
	    String stringRead = br1.readLine();				
		return stringRead;
	}
	
	
	
	
	
	
	
	
	
	
}
