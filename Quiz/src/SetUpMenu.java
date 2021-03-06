import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class SetUpMenu implements java.io.Serializable
{
	
	private ServerManager serverConnect;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	//constructor - pass the serverConnect file retrieved from Server
	public SetUpMenu(ServerManager servMan)
	{
		this.serverConnect = servMan;
	}
	

	/**
	 * Welcome method. Initial screen seen by client - gives ability to view and create quizzes
	 * @throws IOException
	 */
	public void welcomeMenu() throws IOException
	{
		boolean complete = false;
		while(!complete)
		{
			System.out.println();
			System.out.println("************* Welcome to Mike's Quiz Setup *************");
			System.out.println();
			System.out.println("What would you like to do? ");
			System.out.println("1: Make a new Quiz?");
			System.out.println("2: Show all available Quizzes");
			System.out.println("0: Exit");		
			
			int option = stringToIntCheck("Please enter the required option: ");
			
			switch(option) 
			{
				case 0: System.out.println("Exiting: Thank you and good bye");
						complete = true;
						System.exit(0);
						break;
						
				case 1: System.out.println("*** DEBUG **** Making new quiz");
						makeNewQuiz();
						break;
						
				case 2: System.out.println("*** DEBUG **** Get All Available Quizzes");
						getAllQuizzes();
						break;
					
				default: System.out.println("*** DEBUG **** Not an Option, try again");
						break;
			}
		}		
	}
	
	/**
	 * Make a new Quiz from Scratch
	 * @throws IOException
	 */
	public void makeNewQuiz() throws IOException
	{		
			System.out.println();
			System.out.println("************* Quiz Setup *************");
			System.out.println();
			String qName = readLineViaBuffer("Please Enter a Name for your Quiz: ");	
			
			int quantOfQuestions = stringToIntCheck("Please Enter quantity of Questions: ");

			//create new quiz and return its ID
			int returnedID = serverConnect.addNewQuiz(qName, quantOfQuestions);
			Quiz tempQuiz = serverConnect.getQuizFromID(returnedID);
			addQuestions(quantOfQuestions, tempQuiz);
			System.out.println();
			System.out.println("Congratulations, Quiz Setup Complete.");
			System.out.println("****** Your Quiz ID: " + returnedID + ". Please Make a note of this. ******");
	}
	
	/**
	 * Creates a number of questions in designated quiz
	 * @param num
	 * @param tempQuiz
	 * @throws IOException
	 */
	public void addQuestions(int num, Quiz tempQuiz) throws IOException
	{
		for (int i = 0; i < num; i++)
		{
			System.out.println("Question: " + (i + 1));
			String quest = readLineViaBuffer("Please Enter a Question: ");
			String ansOne = readLineViaBuffer("Please Enter 1st Answer: ");
			String ansTwo = readLineViaBuffer("Please Enter 2nd Answer: ");
			String ansThree = readLineViaBuffer("Please Enter 3rd Answer: ");
			
			boolean complete = false;
			while (!complete)
			{
				int answer = stringToIntCheck("Please Enter which is the correct Answer: 1,2, or 3: ");
				if (answer < 4 && answer > 0)
				{
					complete = true; 
					Question questTemp = new Question(quest, ansOne, ansTwo, ansThree, answer);
					serverConnect.addQuestionToQuiz(tempQuiz, questTemp);	
				}
				else
				{
					System.out.println("That is not one of the options, try again");
				}
			} 	
		}		
	}	
	
	
	/**
	 * Returns from server HashMap of all Quizzes available
	 * @throws IOException
	 */
	public void getAllQuizzes() throws IOException 
	{	
		System.out.println(); 
		if(!serverConnect.returnAllQuizzes().isEmpty())			
		{	
			HashMap<Integer, Quiz> tempHlist = serverConnect.returnAllQuizzes();
			System.out.println("List of Quizzes already on system");
			System.out.println("---------------------------------");		
			for(Map.Entry<Integer, Quiz> entry: tempHlist.entrySet())
			{
				System.out.println("QuizID: " + entry.getKey() + ". " + "Quiz Name: " + entry.getValue().getQuizName()); 
			}
			System.out.println(); 
		} else
		{
			System.out.println("No Quizzes to display at present - you need to create a Quiz, choose option 1 from menu");
		}	
	}	
	
	
	/**
	 * Returns an int from a string - through user input
	 * this will keep requesting a int to be inserted until one is detected
	 * @param instruction
	 * @return option (int)
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
					System.out.println("Wrong format - Please insert a number !!!! try again !!!!");
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
	
}

