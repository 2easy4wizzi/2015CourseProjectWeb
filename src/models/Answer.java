package models;

public class Answer
{
	
	private int Aid ,Qid ,AVotes;
	private String AnswerText, OwnerNickname, CreatedHuman;
	private long Created;
	private double ARating ;
	public Answer(int aid, int qid, String answerText, String ownerNickname,double arating,int avotes ,String createdhuman, long created) 
	{
		Aid = aid;
		Qid = qid;
		AnswerText = answerText;
		OwnerNickname = ownerNickname;
		ARating = arating;
		AVotes = avotes;
		Created = created;
		CreatedHuman = createdhuman;
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

	public long getCreated() {
		return Created;
	}
	
	public String getCreatedHuman() {
		return CreatedHuman;
	}

	public double getARating() {
		return ARating;
	}

	public void print() 
	{
		System.out.println("Aid: " + Aid + " Qid: " +Qid + " AnswerText: " + AnswerText + " OwnerNickname: " + OwnerNickname + " Created: " + Created + " ARating: " + ARating +" AVotes: " + AVotes );
	}
	
	
	
}
