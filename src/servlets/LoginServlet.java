package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.derby.iapi.sql.ResultSet;
//import org.apache.derby.tools.sysinfo;
import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import constants.DBConstants;


public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int isOnDB(String column, String var)
	{
		try {
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context.lookup(DBConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			//final String st = "SELECT * FROM TBL_USERS WHERE " + column+ "=?";
			PreparedStatement ps = conn.prepareStatement(DBConstants.SELECT_USER_BY_NAME_STMT);
			ps.setString(1, var);
			ResultSet rs = (ResultSet) ps.executeQuery();
			
	
			while(rs.next())
			{
				String name = rs.getString("Name");
				String pass = rs.getString("Password");
				System.out.println(" user name:" + name + " password:" + pass);
			}
			/*if (!rs.next() ) {
			    System.out.println("no data");
			}*/
			rs.close();
			ps.close();
			conn.close();
			return 0;
		}
		catch (SQLException | NamingException e) 
		{
			getServletContext().log("isOnDB : Error while closing connection", e);
			return -1;
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println(isOnDB("Name", "asd"));
		
		if(request.getParameter("action").equals("register")){
			System.out.println("called from register method");
		
		try {
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context.lookup(DBConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(DBConstants.INSERT_USER_STMT);

			pstmt.setString(1, request.getParameter("username"));
			pstmt.setString(2, request.getParameter("password"));
			pstmt.setString(3, request.getParameter("nickName"));
			pstmt.setString(4, request.getParameter("description"));
			pstmt.setString(5, request.getParameter("photo"));
			/*System.out.println(request.getParameter("username"));
			System.out.println(request.getParameter("password"));
			System.out.println(request.getParameter("nickName"));
			System.out.println(request.getParameter("description"));
			System.out.println(request.getParameter("photo"));*/
			
			pstmt.executeUpdate();
			
			conn.commit();
			pstmt.close();
			conn.close();
		} catch (SQLException | NamingException e) {
			getServletContext().log("Error while closing connection", e);
			response.sendError(500);// internal server error
		}
		response.sendRedirect("index.html");
		response.getWriter().append("Served at: ").append(request.getContextPath());

		}else{
			System.out.println("called from login function");
		}
	}
}
