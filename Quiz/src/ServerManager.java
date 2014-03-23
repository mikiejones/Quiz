import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * Interface to setup the Server Manager - that is the main port of contact
 * @author mikieJ
 *
 */


public interface ServerManager extends Remote 
{

	/**
	 * Create a new empty Quiz with name and number of questions
	 * returns the ID number to recall test
	 * @param name
	 * @param questionNum
	 */
	int addNewQuiz(String name, int questionNum) throws RemoteException;
	
	/**
	 * 
	 * @param quizTemp
	 * @return
	 * @throws RemoteException
	 */
	int createQuizId(Quiz quizTemp) throws RemoteException;
	
	
	/**
	 * 
	 * @param Id
	 * @return
	 * @throws RemoteException
	 */
	Quiz getQuizFromID(int Id) throws RemoteException;
	
	
	/**
	 * Add question to quiz object
	 * @param quiz
	 * @param q
	 * @throws RemoteException
	 */
	void addQuestionToQuiz(Quiz quiz, Question q)  throws RemoteException;
	
	/**
	 * Add questions to a quiz Via its Id
	 * @param ID
	 * @throws RemoteException
	 */
	void addQuestionToQuiz(int Id, Question q)  throws RemoteException;
	
	
}
