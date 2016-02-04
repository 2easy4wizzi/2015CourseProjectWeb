package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Statement;
import java.util.Enumeration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.derby.tools.sysinfo;
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
	
	public int isOnDBReg(String column, String var)
	{
		try {
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context.lookup(DBConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			PreparedStatement ps;
			int retValue = 0;
			if(column.equals("Name"))
			{
				ps = conn.prepareStatement(DBConstants.SELECT_USER_BY_NAME_STMT);
			}
			else// if (column.equals("Nickname"))
			{
				ps = conn.prepareStatement(DBConstants.SELECT_USER_BY_NICKNAME_STMT);
			}
			ps.setString(1, var);
			ResultSet rs = (ResultSet) ps.executeQuery();
			if (!rs.next() ) {
			    System.out.println(var+  "is available");
			    retValue = 1;
			}
			rs.close();
			ps.close();
			conn.close();
			return retValue;
		}
		catch (SQLException | NamingException e) 
		{
			getServletContext().log("isOnDBReg : Error while closing connection", e);
			return -1;
		}
	}
	
	public int isOnDBlogin(String username, String password)
	{
		try 
		{
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context.lookup(DBConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			PreparedStatement ps;
			ps = conn.prepareStatement(DBConstants.SELECT_USER_BY_NAME_STMT);
			ps.setString(1, username);
			ResultSet rs = (ResultSet) ps.executeQuery();
			int retValue = 1;
			if (!rs.next()) 
			{
			    System.out.println(username +" is not in the system");
			    retValue = 0;
			}
			else if(!rs.getString("Password").equals(password))
			{
				System.out.println("password is not correct");
				retValue = 0;
			}
			rs.close();
			ps.close();
			conn.close();
			return retValue;
		}
		catch (SQLException | NamingException e) 
		{
			getServletContext().log("isOnDBlogin : Error while closing connection", e);
			return -1;
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException 
	{
		response.getWriter().write("0");
		Enumeration params = request.getParameterNames(); 
		while(params.hasMoreElements()){
		 String paramName = (String)params.nextElement();
		 System.out.println("Attribute Name - "+paramName+", Value - "+request.getParameter(paramName));
		}
		
		String uri = request.getRequestURI();
		uri = uri.substring(uri.indexOf("LoginServlet") + "LoginServlet".length() + 1);
		System.out.println(uri);
		
		if(request.getParameter("action").equals("register")) //register button pressed
		{
			if(isOnDBReg("Name", request.getParameter("username"))== 0)
			{
				System.out.println("user name taken");
			}
			else if(isOnDBReg("Nickname", request.getParameter("nickName"))== 0) 
			{
				System.out.println("nickname taken");
			}
			else
			{
				
				System.out.println("called from register method");
				try 
				{
					Context context = new InitialContext();
					BasicDataSource ds = (BasicDataSource) context.lookup(DBConstants.DB_DATASOURCE);
					Connection conn = ds.getConnection();
					PreparedStatement pstmt = conn.prepareStatement(DBConstants.INSERT_USER_STMT);
		
					pstmt.setString(1, request.getParameter("username"));
					pstmt.setString(2, request.getParameter("password"));
					pstmt.setString(3, request.getParameter("nickName"));
					pstmt.setString(4, request.getParameter("description"));
					pstmt.setString(5, request.getParameter("photo"));
					pstmt.executeUpdate();
					
					conn.commit();
					pstmt.close();
					conn.close();
					response.getWriter().write("1");
				} 
				catch (SQLException | NamingException e) 
				{
					getServletContext().log("Error while closing connection", e);
					response.sendError(500);// internal server error
				}
			}
			response.sendRedirect("index.html");
			//response.getWriter().append("Served at: ").append(request.getContextPath());	
		}
		else if(request.getParameter("action").equals("login"))//login button pressed
		{
			System.out.println("called from login function");

			 
			
			
			
			if(isOnDBlogin(request.getParameter("username"),request.getParameter("password")) == 0)
			{
				System.out.println("cant log in");
				
			}
			else
			{
				response.getWriter().write("1");
				System.out.println("welcome back " + request.getParameter("username"));
			}
			
		}
	}
	}

