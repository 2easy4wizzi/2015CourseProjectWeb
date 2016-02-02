package constants;

public interface DBConstants {

	public final String USERS = "users";
	//public final Type CUSTOMER_COLLECTION = new TypeToken<Collection<Customer>>() {}.getType();
	//derby constants
	//test for github
	public final String DB_NAME = "webGiladDB";
	public final String DB_DATASOURCE = "java:comp/env/jdbc/webGiladDatasource";
	public final String PROTOCOL = "jdbc:derby:"; 
	//sql statements
	public final String CREATE_USERS_TABLE = "CREATE TABLE CUSTOMER(Name varchar(10) NOT NULL,"
																		+ "Password varchar(8) NOT NULL,"
																		+ "Nickname varchar(20) NOT NULL,"
																		+ "Description varchar(50),"
																		+ "Photo varchar(200))";
	public final String INSERT_USER_STMT = "INSERT INTO CUSTOMER VALUES(?,?,?,?,?)";
	public final String SELECT_ALL_USERS_STMT = "SELECT * FROM USER";
	public final String SELECT_USER_BY_NAME_STMT = "SELECT * FROM USER " + "WHERE Name=?";
}
