package models;

public class Question
{
	
	private int Qid ,Qvotes ;
	private String QuestionText, QTopics, OwnerNickname, Created;
	private double QRating ;
	public Question(int qid,int qvotes, String questionText, String qTopics,String ownerNickname, String created) 
	{
		Qid = qid;
		Qvotes = qvotes;
		QuestionText = questionText;
		QTopics = qTopics;
		OwnerNickname = ownerNickname;
		Created = created;
		QRating = 0;
	}
	

	
	
	public int getQid() {
		return Qid;
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
		System.out.println("Qid: " +Qid +  " Qvotes: " + Qvotes + " QuestionText: " + QuestionText + " QTopics: " + QTopics + " OwnerNickname: " + OwnerNickname + " Created: " + Created + " QRating: " + QRating);
	}
	
	
	
}
