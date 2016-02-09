package constants;

import java.lang.reflect.Type;
import java.util.Collection;
import com.google.gson.reflect.TypeToken;
import models.Question;

public interface DBConstants 
{
	public final String DB_NAME = "webGiladDB";
	public final String DB_DATASOURCE = "java:comp/env/jdbc/webGiladDatasource";
	public final String PROTOCOL = "jdbc:derby:"; 
	public final Type NEW_QUESTION_COLLECTION = new TypeToken<Collection<Question>>() {}.getType();
	//sql statements
	public final String CREATE_USERS_TABLE =  "CREATE TABLE TBL_USERS("
										    + "Username varchar(10) PRIMARY KEY,"
											+ "Password varchar(8) NOT NULL,"
											+ "Nickname varchar(20) UNIQUE NOT NULL,"
											+ "Description varchar(50),"
											+ "PhotoUrl varchar(300),"
											+ "UserRating real DEFAULT 0"
											+ ")";
	public final String INSERT_USER_STMT = "INSERT INTO TBL_USERS VALUES(?,?,?,?,?,DEFAULT)";
	public final String SELECT_ALL_USERS_STMT = "SELECT * FROM TBL_USERS";
	public final String SELECT_USER_BY_NAME_STMT = "SELECT * FROM TBL_USERS WHERE Username=?";
	public final String SELECT_USER_BY_NICKNAME_STMT = "SELECT * FROM TBL_USERS WHERE Nickname=?";
	public final String SELECT_USER_BY_NAME_AND_PASSWORD_STMT = "SELECT * FROM TBL_USERS WHERE Username=? AND Password=?";

	public final String CREATE_QUESTIONS_TABLE =  "CREATE TABLE TBL_QUESTIONS("
											+ "QId INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,"
											+ "QuestionText varchar(300) NOT NULL,"
											+ "QTopics varchar(1000),"
											+ "OwnerNickname varchar(20),"
											+ "QRating real DEFAULT 0,"
											+ "QVotes INT DEFAULT 0,"
											+ "Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
											+ "FOREIGN KEY (OwnerNickname) REFERENCES TBL_USERS(Nickname)"
											+ ")";
	public final String INSERT_QUESTION_STMT = "INSERT INTO TBL_QUESTIONS (QuestionText, QTopics, OwnerNickname, QRating, QVotes, Created) VALUES(?,?,?,DEFAULT,DEFAULT,DEFAULT)";
	public final String SELECT_QUESTION_BY_NICKNAME_STMT = "SELECT * FROM TBL_QUESTIONS WHERE OwnerNickname=?";
	public final String SELECT_TOP_20_QUESTION_BY_TIMESTAMP_STMT = "SELECT * FROM TBL_QUESTIONS FETCH FIRST 20 ROWS ONLY";




}
