import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

public interface Quiz extends Remote
{	
	/**
	 * Adds a question to the current quiz
	 * @param quest
	 * @throws RemoteException
	 */
	void addQuestionToQuiz(Question quest) throws RemoteException;
	
	/**
	 * Returns the name of the current Quiz
	 * @return
	 * @throws RemoteException
	 */
	String getQuizName() throws RemoteException;
	
	/**
	 * Returns a list of questions for current Quiz
	 * @return
	 * @throws RemoteException
	 */
	List<Question> getQuestions() throws RemoteException;
	
	/**
	 * Returns ID of currently selected Quiz
	 * @return
	 * @throws RemoteException
	 */
	int getQuizId() throws RemoteException;
	
	/**
	 * Creates an ID for the newly created quiz
	 * @param id
	 * @throws RemoteException
	 */
	void createQuizId(int id) throws RemoteException;
	
	
	void addToPlayerScore(int Id, int score) throws RemoteException;
	
	void addToPlayerScore(PlayerScores pScore) throws RemoteException;
	
	HashMap<Integer, Integer> getAllPlayerScores() throws RemoteException;
	
	int getPlayersScore(int Id) throws RemoteException;
	
	List<PlayerScores> getScores() throws RemoteException;
	
	int getQuestionTotal() throws RemoteException;

	void setQuestionTotal(int questionTotal) throws RemoteException;

	
}
