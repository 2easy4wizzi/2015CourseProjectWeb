package constants;

public interface DBConstants {

	public final String USERS = "users";
	//public final Type CUSTOMER_COLLECTION = new TypeToken<Collection<Customer>>() {}.getType();
	//derby constants
	//test for github
	public final String DB_NAME = "ExampleDB";
	public final String DB_DATASOURCE = "java:comp/env/jdbc/ExampleDatasource";
	public final String PROTOCOL = "jdbc:derby:"; 
	//sql statements
	public final String CREATE_USERS_TABLE = "CREATE TABLE CUSTOMER(Name varchar(100) NOT NULL,"
			+ "Password varchar(100) NOT NULL)";
	public final String INSERT_USER_STMT = "INSERT INTO CUSTOMER VALUES(?,?)";
	public final String SELECT_ALL_USERS_STMT = "SELECT * FROM USER";
	public final String SELECT_USER_BY_NAME_STMT = "SELECT * FROM USER "
			+ "WHERE Name=?";
}
