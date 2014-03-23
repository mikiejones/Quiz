import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class TestQuizSetUp 
{
	
	ServerManager newManager; 
	
	@Before
	public void startUp() throws RemoteException
	{
		newManager = new ServerManagerImpl();
		
	}
	
	
	
	
	
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
	public void setUpMenu() throws IOException 
	{
		
		
	
	
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