package models;

public class Answer
{
	
	private int Aid ,Qid ,AVotes;
	private String AnswerText, OwnerNickname, CreatedHuman;
	private long Created;
	public Answer(int aid, int qid, String answerText, String ownerNickname,int avotes ,String createdhuman, long created) 
	{
		Aid = aid;
		Qid = qid;
		AnswerText = answerText;
		OwnerNickname = ownerNickname;
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

	

	public void print() 
	{
		System.out.println("Aid: " + Aid + " Qid: " +Qid + " AnswerText: " + AnswerText + " OwnerNickname: " + OwnerNickname + " Created: " + Created +" AVotes: " + AVotes );
	}
	
	
	
}
