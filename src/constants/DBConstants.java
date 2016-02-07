package constants;

public interface DBConstants 
{
	public final String DB_NAME = "webGiladDB";
	public final String DB_DATASOURCE = "java:comp/env/jdbc/webGiladDatasource";
	public final String PROTOCOL = "jdbc:derby:"; 
	//sql statements
	public final String CREATE_USERS_TABLE =  "CREATE TABLE TBL_USERS("
										    + "Name varchar(10) PRIMARY KEY,"
											+ "Password varchar(8) NOT NULL,"
											+ "Nickname varchar(20) UNIQUE NOT NULL,"
											+ "Description varchar(50),"
											+ "Photo varchar(300),"
											+ "Rating real DEFAULT 0"
											+ ")";
	public final String INSERT_USER_STMT = "INSERT INTO TBL_USERS VALUES(?,?,?,?,?,DEFAULT)";
	public final String SELECT_ALL_USERS_STMT = "SELECT * FROM TBL_USERS";
	public final String SELECT_USER_BY_NAME_STMT = "SELECT * FROM TBL_USERS WHERE Name=?";
	public final String SELECT_USER_BY_NICKNAME_STMT = "SELECT * FROM TBL_USERS WHERE Nickname=?";
	public final String SELECT_USER_BY_NAME_AND_PASSWORD_STMT = "SELECT * FROM TBL_USERS WHERE Name=? AND Password=?";

	public final String CREATE_QUESTIONS_TABLE =  "CREATE TABLE TBL_QUESTIONS("
											+ "Id Integer PRIMARY KEY,"
											+ "Question varchar(300) NOT NULL,"
											+ "Topics varchar(1000),"
											+ "Nickname varchar(20),"
											+ "Rating real DEFAULT 0,"
											+ "Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
											+ "FOREIGN KEY (Nickname) REFERENCES TBL_USERS(Nickname)"
											+ ")";
	public final String INSERT_QUESTION_STMT = "INSERT INTO TBL_QUESTIONS VALUES(?,?,?,?,DEFAULT,DEFAULT)";

}
