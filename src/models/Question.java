package models;

public class Question
{
	
	private int Qid ,Qvotes, Answers;
	private String QuestionText, QTopics, OwnerNickname, Created;
	private double QRating ;
	public Question(int qid, String questionText, String qTopics,String ownerNickname,double qrating,int qvotes ,String created, int answers) 
	{
		Qid = qid;
		QuestionText = questionText;
		QTopics = qTopics;
		OwnerNickname = ownerNickname;
		QRating = qrating;
		Qvotes = qvotes;
		Created = created;
		Answers = answers;
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




	public String getCreated() {
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
