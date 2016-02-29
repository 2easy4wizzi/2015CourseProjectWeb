package models;

/**********************************************************
**                     Answer model                      **
**********************************************************/

public class Answer
{
	
	private int Aid ,Qid ,AVotes;
	private String AnswerText, OwnerNickname, CreatedHuman;
	private long Created;
	private String OwnerPhoto;
	
	public Answer(int aid, int qid, String answerText, String ownerNickname,int avotes ,String createdhuman, long created) 
	{
		Aid = aid;
		Qid = qid;
		AnswerText = answerText;
		OwnerNickname = ownerNickname;
		AVotes = avotes;
		Created = created;
		CreatedHuman = createdhuman;
		OwnerPhoto = null;
	}
	public String getOwnerPhoto() {
		return OwnerPhoto;
	}
	public void setOwnerPhoto(String ownerPhoto) {
		OwnerPhoto = ownerPhoto;
	}
	public Answer(int aid, int qid, String answerText, String ownerNickname,int avotes ,String createdhuman, long created, String ownerPhoto) 
	{
		Aid = aid;
		Qid = qid;
		AnswerText = answerText;
		OwnerNickname = ownerNickname;
		AVotes = avotes;
		Created = created;
		CreatedHuman = createdhuman;
		OwnerPhoto = ownerPhoto;
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
