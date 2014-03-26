


public class PlayerScores implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int playerID;
	private int quizId;
	private int score; 
	
	public PlayerScores(int quizId, int playerID, int score)
	{
		this.playerID = playerID;
		this.quizId = quizId;
		this.score = score;
	}
	
	public int getQuizID()
	{
		return quizId;
	}
	
	public int getScore()  
	{
		return score;
	}
	
	
	public int getPlayerId()
	{
		return playerID;
	}
	
}
