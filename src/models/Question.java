package models;

/**********************************************************
**                     Question model                    **
**********************************************************/

//Holding the attributes of the question
public class Question
{
	
	private int Qid ,Qvotes, Answers;
	public void setAnswers(int answers) {
		Answers = answers;
	}

	private String QuestionText, OwnerNickname,CreatedHuman;
	private long Created;
	private double QRating ;
	private String OwnerPhoto;

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
		OwnerPhoto = null;
	}
	
	public String getOwnerPhoto() {
		return OwnerPhoto;
	}

	public void setOwnerPhoto(String ownerPhoto) {
		OwnerPhoto = ownerPhoto;
	}

	public Question(int qid, String questionText, String ownerNickname,
			double qrating,int qvotes ,String createdHuman,long created, int answers, String ownerPhoto) 
	{
		Qid = qid;
		QuestionText = questionText;
		OwnerNickname = ownerNickname;
		QRating = qrating;
		Qvotes = qvotes;
		CreatedHuman = createdHuman;
		Created = created;
		Answers = answers;
		OwnerPhoto = ownerPhoto;
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
