package models;

public class Answer
{
	
	private int Aid ,Qid ,AVotes;
	private String AnswerText, OwnerNickname, Created;
	private double ARating ;
	public Answer(int aid, int qid, String answerText, String ownerNickname,double arating,int avotes ,String created) 
	{
		Aid = aid;
		Qid = qid;
		AnswerText = answerText;
		OwnerNickname = ownerNickname;
		ARating = arating;
		AVotes = avotes;
		Created = created;
	}

	public int getAid() {
		return Aid;
	}

	public int getQid() {
		return Qid;
	}

	public int getAVotes() {
		return AVotes;
	}

	public String getAnswerText() {
		return AnswerText;
	}

	public String getOwnerNickname() {
		return OwnerNickname;
	}

	public String getCreated() {
		return Created;
	}

	public double getARating() {
		return ARating;
	}

	public void print() 
	{
		System.out.println("Aid: " + Aid + " Qid: " +Qid + " AnswerText: " + AnswerText + " OwnerNickname: " + OwnerNickname + " Created: " + Created + " ARating: " + ARating +" AVotes: " + AVotes );
	}
	
	
	
}
