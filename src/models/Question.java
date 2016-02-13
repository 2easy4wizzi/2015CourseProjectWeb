package models;

public class Question
{
	
	private int Qid ,Qvotes, Answers;
	public void setAnswers(int answers) {
		Answers = answers;
	}




	private String QuestionText, QTopics, OwnerNickname,CreatedHuman;
	private long Created;
	private double QRating ;
	private String Title;







	public Question(int qid, String questionText, String qTopics,String ownerNickname,
			double qrating,int qvotes ,String createdHuman,long created, int answers,String title) 
	{
		Qid = qid;
		QuestionText = questionText;
		QTopics = qTopics;
		OwnerNickname = ownerNickname;
		QRating = qrating;
		Qvotes = qvotes;
		CreatedHuman = createdHuman;
		Created = created;
		Answers = answers;
		Title= title;
	}
	

	public String getTitle() {
		return Title;
	}
	
	public String getCreatedHuman() {
		return CreatedHuman;
	}
	
	public int getQid() {
		return Qid;
	}
	
	public int getAnswers() {
		return Answers;
	}
	




	public int getQvotes() {
		return Qvotes;
	}




	public String getQuestionText() {
		return QuestionText;
	}




	public String getQTopics() {
		return QTopics;
	}




	public String getOwnerNickname() {
		return OwnerNickname;
	}




	public long getCreated() {
		return Created;
	}




	public double getQRating() {
		return QRating;
	}




	public void print() 
	{
		System.out.println("Qid: " +Qid +  " Qvotes: " + Qvotes + " QuestionText: " + QuestionText + " QTopics: " + QTopics + " OwnerNickname: " + OwnerNickname + " Created: " + Created + " QRating: " + QRating +" Answers: " + Answers );
	}
	
	
	
}
