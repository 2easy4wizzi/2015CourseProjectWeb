package constants;

import java.lang.reflect.Type;
import java.util.Collection;
import com.google.gson.reflect.TypeToken;

import models.Answer;
import models.Question;
import models.Topic;
import models.User;
import models.UserQuestionUnswer;

public interface DBConstants 
{
	public final String DB_NAME = "webGiladDB";
	public final String DB_DATASOURCE = "java:comp/env/jdbc/webGiladDatasource";
	public final String PROTOCOL = "jdbc:derby:"; 
	public final Type NEW_QUESTION_COLLECTION = new TypeToken<Collection<Question>>() {}.getType();
	public final Type NEW_ANSWER_COLLECTION   = new TypeToken<Collection<Answer>>() {}.getType();	
    public final Type NEW_TOPICS_COLLECTION   = new TypeToken<Collection<String>>() {}.getType();
	public final Type NEW_USER_COLLECTION = new TypeToken<Collection<User>>() {}.getType();
	public final Type NEW_TOPIC_COLLECTION = new TypeToken<Collection<Topic>>() {}.getType();
    public final Type NEW_USER_QUESTION_UNSWER_COLLECTION   = new TypeToken<Collection<UserQuestionUnswer>>() {}.getType();
	
	public final String TOPICS_SERVLET_NAME = "TopicsServlet";
	
	//sql statements
	public final String CREATE_USERS_TABLE =  "CREATE TABLE TBL_USERS("
										    + "Username varchar(10) PRIMARY KEY,"
											+ "Password varchar(8) NOT NULL,"
											+ "Nickname varchar(20) UNIQUE NOT NULL,"
											+ "Description varchar(50),"
											+ "PhotoUrl varchar(300),"
											+ "UserRating double DEFAULT 0"
											+ ")";
	public final String INSERT_USER_STMT = "INSERT INTO TBL_USERS VALUES(?,?,?,?,?,DEFAULT)";
	public final String SELECT_ALL_USERS_STMT = "SELECT * FROM TBL_USERS";
	public final String SELECT_USER_BY_NAME_STMT = "SELECT * FROM TBL_USERS WHERE Username=?";
	public final String SELECT_USER_BY_NICKNAME_STMT = "SELECT * FROM TBL_USERS WHERE Nickname=?";
	public final String SELECT_USER_BY_NAME_AND_PASSWORD_STMT = "SELECT * FROM TBL_USERS WHERE Username=? AND Password=?";

	public final String CREATE_QUESTIONS_TABLE =  "CREATE TABLE TBL_QUESTIONS("
											+ "QId INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,"
											+ "QuestionText varchar(300) NOT NULL,"
											+ "OwnerNickname varchar(20),"
											+ "QRating double DEFAULT 0,"
											+ "QVotes INT DEFAULT 0,"
											+ "Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
											+ "Answers INT DEFAULT 0,"
											+ "FOREIGN KEY (OwnerNickname) REFERENCES TBL_USERS(Nickname)"
											+ ")";
	public final String INSERT_QUESTION_STMT = "INSERT INTO TBL_QUESTIONS (QuestionText, OwnerNickname, QRating, QVotes, Created) VALUES(?,?,DEFAULT,DEFAULT,DEFAULT)";
	public final String SELECT_TOP_20_NEW_QUESTIONS_BY_TIMESTAMP_STMT = "SELECT * FROM TBL_QUESTIONS WHERE Answers=0 ORDER BY Created DESC OFFSET ? ROWS FETCH NEXT 20 ROWS ONLY";
	public final String SELECT_TOP_20_QUESTIONS_BY_TIMESTAMP_STMT = "SELECT * FROM TBL_QUESTIONS ORDER BY QRating DESC OFFSET ? ROWS FETCH NEXT 20 ROWS ONLY";
	public final String SELECT_TOP_20_QUESTIONS_BY_TOPIC_STMT = "SELECT TBL_QUESTIONS.* FROM TBL_TOPICS INNER JOIN TBL_QUESTIONS ON TBL_TOPICS.QId=TBL_QUESTIONS.QId where TBL_TOPICS.QTopics=? OFFSET ? ROWS FETCH NEXT 20 ROWS ONLY";
	public final String SELECT_COUNT_NEW_QUESTIONS_STMT = "SELECT COUNT (*) FROM TBL_QUESTIONS WHERE Answers=0";
	public final String SELECT_COUNT_QUESTIONS_STMT = "SELECT COUNT (*) FROM TBL_QUESTIONS";
	public final String SELECT_COUNT_QUESTIONS_BY_TOPIC_STMT = "SELECT COUNT(TBL_QUESTIONS.QID) as totalQuestionOfTopic FROM TBL_TOPICS INNER JOIN TBL_QUESTIONS ON TBL_TOPICS.QId=TBL_QUESTIONS.QId where TBL_TOPICS.QTopics=?";
	public final String UPDATE_QUESTION_ANSWERS_COLUMN_BY_QID_STMT = "UPDATE TBL_QUESTIONS SET Answers = Answers + 1 WHERE QId=?";
	public final String UPDATE_QUESTION_QVOTES_AND_QRATING_COLUMNS_BY_QID_STMT = "UPDATE TBL_QUESTIONS SET QVotes = ?, QRating = ? WHERE QId=?";
	public final String SELECT_QUESTION_BY_QID_STMT = "SELECT * FROM TBL_QUESTIONS WHERE Qid=?";
	public final String UPDATE_QRATING_BY_FORMULA_STMT = "UPDATE TBL_QUESTIONS SET QRating = ? WHERE QId=?";
	//public final String SELECT_SUM_QRATING_BY_QIDS_STMT = "SELECT SUM (QRating) FROM TBL_QUESTIONS WHERE QId=?";
	public final String SELECT_QVOTES_BY_QID_STMT = "SELECT QVotes FROM TBL_QUESTIONS WHERE QID = ?";


	public final String CREATE_ANSWERS_TABLE =  "CREATE TABLE TBL_ANSWERS("
											+ "AId INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,"
											+ "QId INT,"
											+ "AnswerText varchar(300) NOT NULL,"
											+ "OwnerNickname varchar(20),"
											+ "AVotes INT DEFAULT 0,"
											+ "Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
											+ "FOREIGN KEY (QId) REFERENCES TBL_QUESTIONS(QId),"
											+ "FOREIGN KEY (OwnerNickname) REFERENCES TBL_USERS(Nickname)"
											+ ")";
	public final String INSERT_ANSWER_STMT = "INSERT INTO TBL_ANSWERS (QId, AnswerText, OwnerNickname, AVotes, Created) VALUES(?,?,?,DEFAULT,DEFAULT)";
	public final String SELECT_ANSWERS_BY_QID_STMT = "SELECT * FROM TBL_ANSWERS WHERE Qid=?";
	public final String SELECT_ANSWER_BY_AID_STMT = "SELECT * FROM TBL_ANSWERS WHERE Aid=?";
	public final String UPDATE_ANSWER_QVOTES_BY_AID_STMT = "UPDATE TBL_ANSWERS SET AVotes = AVotes + ? WHERE AId=?";
	public final String SELECT_ANSWERS_ORDER_BY_VOTES_AND_TIMESTAMP_BY_QID_STMT = "SELECT * FROM TBL_ANSWERS WHERE Qid=? ORDER BY AVotes DESC, Created ASC";
	public final String GET_AVG_RATING_OF_QUESTION_ANSWERS = "SELECT AVG(AVotes) FROM TBL_ANSWERS WHERE Qid=?";
	
	
	public final String CREATE_QUESTION_VOTES_TABLE =  "CREATE TABLE TBL_QUESTION_VOTES("
			+ "QId INT,"
			+ "OwnerNickname varchar(20),"
			+ "VoteValue INT NOT NULL,"
			+ "FOREIGN KEY (QId) REFERENCES TBL_QUESTIONS(QId),"
			+ "FOREIGN KEY (OwnerNickname) REFERENCES TBL_USERS(Nickname),"
			+ "PRIMARY KEY (QId, OwnerNickname)"
			+ ")";
	public final String INSERT_QUESTION_VOTE_STMT = "INSERT INTO TBL_QUESTION_VOTES VALUES(?,?,?)";
	public final String SELECT_QUESTION_VOTES_STMT = "SELECT * FROM TBL_QUESTION_VOTES WHERE Qid=? AND OwnerNickname=?";
	
	
	public final String CREATE_ANSWER_VOTES_TABLE =  "CREATE TABLE TBL_ANSWER_VOTES("
			+ "AId INT,"
			+ "QId INT,"
			+ "OwnerNickname varchar(20),"
			+ "VoteValue INT NOT NULL,"
			+ "FOREIGN KEY (QId) REFERENCES TBL_QUESTIONS(QId),"
			+ "FOREIGN KEY (AId) REFERENCES TBL_ANSWERS(AId),"
			+ "FOREIGN KEY (OwnerNickname) REFERENCES TBL_USERS(Nickname),"
			+ "PRIMARY KEY (AId, OwnerNickname)"
			+ ")";
	public final String INSERT_ANSWER_VOTE_STMT = "INSERT INTO TBL_ANSWER_VOTES VALUES(?,?,?,?)";
	public final String SELECT_ANSWER_VOTES_STMT = "SELECT * FROM TBL_ANSWER_VOTES WHERE Aid=? AND OwnerNickname=?";
	

	public final String GET_AVG_VOTES_OF_ANSWERS_BY_USER = "SELECT AVG(CAST (AVotes AS DOUBLE PRECISION)) FROM TBL_ANSWERS WHERE OwnerNickname=?";
	public final String UPDATE_USER_RATING = "UPDATE TBL_USERS SET UserRating=? WHERE Nickname=?";
	public final String SELECT_OWNER_BY_QID = "SELECT OwnerNickname FROM TBL_QUESTIONS WHERE Qid=?";
	public final String SELECT_COUNT_USERS_STMT = "SELECT COUNT (*) FROM TBL_USERS";
	public final String SELECT_TOP_20_USERS_BY_USER_RATING_STMT = "SELECT * FROM TBL_USERS ORDER BY UserRating DESC FETCH NEXT 20 ROWS ONLY";
	public final String GET_AVG_RATING_OF_QUESTIONS_BY_USER = "SELECT AVG (Qrating) FROM TBL_QUESTIONS WHERE OwnerNickname=?";
	
	
	
	
	public final String CREATE_TOPICS_TABLE =  "CREATE TABLE TBL_TOPICS("
			+ "QId INT,"
			+ "QTopics varchar(50),"
			+ "FOREIGN KEY (QId) REFERENCES TBL_QUESTIONS(QId),"
			+ "PRIMARY KEY (QId, QTopics)"
			+ ")";
	public final String INSERT_TOPIC_STMT = "INSERT INTO TBL_TOPICS VALUES(?,?)";
	public final String SELECT_TOPICS_BY_QID_STMT = "SELECT * FROM TBL_TOPICS WHERE Qid=?";
	//public final String SELECT_DISTINCT_TOPICS_STMT = "SELECT DISTINCT QTopics FROM TBL_TOPICS";
	//public final String SELECT_QID_LIST_BY_TOPICS_STMT = "SELECT QId FROM TBL_TOPICS WHERE QTopics=?";
	public final String SELECT_20_MOST_POPULAR_TOPICS_STMT = "SELECT TBL_TOPICS.QTopics, sum(TBL_QUESTIONS.QRating) as Sumup FROM TBL_TOPICS INNER JOIN TBL_QUESTIONS ON TBL_TOPICS.QId=TBL_QUESTIONS.QId GROUP BY TBL_TOPICS.QTopics ORDER BY sumup desc OFFSET ? ROWS FETCH NEXT 20 ROWS ONLY";
	public final String SELECT_COUNT_TOPICS_STMT = "SELECT COUNT (DISTINCT QTopics) FROM TBL_TOPICS";
	public final String SELECT_TOP_5_TOPICS_BY_POPULARITY_STMT = "SELECT TBL_TOPICS.qtopics, sum(TBL_QUESTIONS.qvotes) as sigma FROM TBL_ANSWERS INNER JOIN TBL_QUESTIONS ON TBL_ANSWERS.QId=TBL_QUESTIONS.QId inner join TBL_TOPICS ON TBL_TOPICS.QId=TBL_QUESTIONS.QId where TBL_ANSWERS.OwnerNickname = ? group by TBL_TOPICS.qtopics order by sigma desc FETCH NEXT 5 ROWS ONLY";
	public final String SELECT_TOP_5_LAST_QUESTION_FOR_USER_STMT = "SELECT * FROM TBL_QUESTIONS WHERE OwnerNickname = ? ORDER BY Created DESC FETCH NEXT 5 ROWS ONLY";
	public final String SELECT_5_LAST_UNSWERED_QUESTIONS_BY_USER_STMT = "SELECT TBL_QUESTIONS.* FROM TBL_ANSWERS INNER JOIN TBL_QUESTIONS ON TBL_ANSWERS.QId=TBL_QUESTIONS.QId where TBL_ANSWERS.OwnerNickname= ? ORDER BY TBL_ANSWERS.Created FETCH NEXT 5 ROWS ONLY";
	public final String SELECT_ANSWER_BY_QID_AND_USER_STMT = "SELECT * FROM TBL_ANSWERS WHERE Qid = ? AND OwnerNickname = ?";
}
