package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import constants.DBConstants;

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(DBConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			Statement stmt =  conn.createStatement();
			ResultSet rs = (ResultSet) stmt.executeQuery(DBConstants.SELECT_ALL_USERS_STMT);
			int i = 0;
			while(rs.next()){
				
				String name = rs.getString("Name");
				String pass = rs.getString("Password");
				System.out.println(++i + " user name:" + name + " password:" + pass);
			}
			
			
			PreparedStatement ps = conn.prepareStatement(DBConstants.SELECT_USER_BY_NAME_STMT);
			ps.setString(1,"klober");
			rs = ps.executeQuery();
			
	
			//psFindIfExists.setString(1,request.getParameter("usernameA"));
			System.out.println("findinding ::::");
			i = 0;
			while(rs.next()){
				
				String name = rs.getString("Name");
				String pass = rs.getString("Password");
				System.out.println(++i + " user name:" + name + " password:" + pass);
			}
			/*if (!resultSet.next() ) {
			    System.out.println("no data");
			}*/
			rs.close();
			stmt.close();
			conn.close();
	} catch (SQLException | NamingException e) {
		getServletContext().log("Error while closing connection", e);
		response.sendError(500);//internal server error
	}
	response.sendRedirect("index.html");
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
		//doGet(request, response);
	}

}
