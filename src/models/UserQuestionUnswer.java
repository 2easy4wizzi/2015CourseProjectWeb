package models;

/**********************************************************
**               User Question Answer model              **
**********************************************************/
//Holding user- question, answer attributes
public class UserQuestionUnswer
{
	
	private int Qid ,AVotes;
	private String AnswerText, QuestionText, CreatedHuman;
	private long Created;
	private double QRating ;
	public UserQuestionUnswer(int qid, String answerText,String questionText, int avotes, double qRating ,String createdhuman, long created) 
	{
		Qid = qid;
		AnswerText = answerText;
		QuestionText = questionText;
		AVotes = avotes;
		QRating = qRating;
		Created = created;
		CreatedHuman = createdhuman;
	}



	public int getQid() {
		return Qid;
	}

	public String getAnswerText() {
		return AnswerText;
	}
	public String getQuestionText() {
		return QuestionText;
	}

	public int getAVotes() {
		return AVotes;
	}
	public double getQRating() {
		return QRating;
	}
	
	public long getCreated() {
		return Created;
	}
	
	public String getCreatedHuman() {
		return CreatedHuman;
	}

	

	public void print() 
	{
		System.out.println(" Qid: " +Qid + " AnswerText: " + AnswerText + " QuestionText: " + QuestionText + " Created: " + Created +" AVotes: " + AVotes );
	}
	
	
	
}
