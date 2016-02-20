package models;

public class Question
{
	
	private int Qid ,Qvotes, Answers;
	public void setAnswers(int answers) {
		Answers = answers;
	}




	private String QuestionText, OwnerNickname,CreatedHuman;
	private long Created;
	private double QRating ;







	public Question(int qid, String questionText, String ownerNickname,
			double qrating,int qvotes ,String createdHuman,long created, int answers) 
	{
		Qid = qid;
		QuestionText = questionText;
		OwnerNickname = ownerNickname;
		QRating = qrating;
		Qvotes = qvotes;
		CreatedHuman = createdHuman;
		Created = created;
		Answers = answers;
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
		System.out.println("Qid: " +Qid +  " Qvotes: " + Qvotes + " QuestionText: " + QuestionText + " OwnerNickname: " + OwnerNickname + " Created: " + Created + " QRating: " + QRating +" Answers: " + Answers );
	}
	
	
	
}
