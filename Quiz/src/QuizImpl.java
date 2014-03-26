import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class QuizImpl extends UnicastRemoteObject implements Quiz,java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String quizName;
	private int questionTotal;
	private int quizId;
	private List<Question> quizQuestions = new ArrayList<Question>(); 
	private List<PlayerScores> scores  = new ArrayList<PlayerScores>();
	//adds players ID and Score to this Quiz
	private HashMap<Integer, Integer> playerScore = new HashMap<Integer, Integer>();

	
	public QuizImpl(String quizName, int questionTotal) throws RemoteException 
	{
		this.quizName = quizName;
		this.questionTotal = questionTotal;
	}


	@Override
	public void addQuestionToQuiz(Question quest)throws RemoteException 
	{
		quizQuestions.add(quest);
		
	}


	@Override
	public List<Question> getQuestions() throws RemoteException 
	{	
		return quizQuestions;
	}


	@Override
	public String getQuizName() throws RemoteException 
	{		
		return quizName;
	}


	@Override
	public int getQuizId() throws RemoteException
	{		
		return quizId;
	}


	@Override
	public void createQuizId(int id) throws RemoteException 
	{
		this.quizId = id;
		
	}


	
	public HashMap<Integer, Integer> getAllPlayerScores() throws RemoteException
	{		
		return playerScore;
	}


	@Override
	public void addToPlayerScore(int Id, int score) throws RemoteException
	{		
		//need to have an overwrting element
		playerScore.put(Id, score);
	}
	

	@Override
	public void addToPlayerScore(PlayerScores pScore) throws RemoteException
	{		
		//need to have an overwrting element
		scores.add(pScore);
	}
	
	
	@Override
	public int getPlayersScore(int Id) throws RemoteException
	{				
			int pScore = playerScore.get(Id);
			return pScore;		
	}
	
	
	@Override
	public List<PlayerScores> getScores() throws RemoteException
	{
		return scores;
	}
	
}
