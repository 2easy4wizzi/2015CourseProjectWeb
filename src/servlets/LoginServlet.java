package servlets;

import java.io.IOException;
import java.io.PrintWriter;
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
	
	public int isOnDBReg(String column, String var, Connection conn) throws SQLException , NamingException
	{
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
			return retValue;
		
	}
	
	public int isOnDBlogin(String username, String password, Connection conn) throws SQLException , NamingException
	{
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
				retValue = -1;
			}
			rs.close();
			ps.close();
			return retValue;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException 
	{
		Enumeration params = request.getParameterNames(); 
		while(params.hasMoreElements())
		{
		 String paramName = (String)params.nextElement();
		 System.out.println("Attribute: "+paramName+", Value: "+request.getParameter(paramName));
		}
		try
		{
			PrintWriter out = response.getWriter();
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context.lookup(DBConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			String uri = request.getRequestURI();
			uri = uri.substring(uri.indexOf("LoginServlet") + "LoginServlet".length() + 1);
			System.out.println(uri);
			
			if(uri.equals("Register"))
			{
				try
				{
					//out.print("Success");
					if(isOnDBReg("Name", request.getParameter("username"), conn)== 0)
					{
						if(isOnDBReg("Nickname", request.getParameter("nickName"), conn)== 0){
						out.println("3");
						}
						else 
						{
							out.println("0");
						}
					}
					else if(isOnDBReg("Nickname", request.getParameter("nickName"), conn)== 0) 
					{
						out.println("2");
			
					}
					else
					{
						
						PreparedStatement ps = conn.prepareStatement(DBConstants.INSERT_USER_STMT);
						
						ps.setString(1, request.getParameter("username"));
						ps.setString(2, request.getParameter("password"));
						ps.setString(3, request.getParameter("nickName"));
						ps.setString(4, request.getParameter("description"));
						ps.setString(5, request.getParameter("photo"));
						ps.executeUpdate();
						
						conn.commit();
						ps.close();
						
					}
				}
				catch (SQLException | NamingException e) 
				{
					getServletContext().log("Error while closing connection", e);
					response.sendError(500);// internal server error
				}
				finally{
					System.out.println("reg :: inside inner finally");
					conn.close();
					out.close();
					
				}
			}
			else if(uri.equals("Login"))
			{
				try
				{
					if(isOnDBlogin(request.getParameter("username"),request.getParameter("password"), conn) != 1)
					{
						System.out.println("user name or password are not correct");
						out.print("user name or password are not correct");
					}
					else
					{
						
						System.out.println("welcome back " + request.getParameter("username"));
					}
				}
				catch (SQLException | NamingException e) 
				{
					getServletContext().log("Error while closing connection", e);
					response.sendError(500);// internal server error
				}
				finally{
					System.out.println("login :: inside inner finally");
					out.close();
					conn.close();
				}
			}	
		}
		catch (SQLException | NamingException e) 
		{
			e.printStackTrace();
		}
		finally
		{
		}
			//response.sendRedirect("index.html");
			//response.getWriter().append("Served at: ").append(request.getContextPath());	
	}
}

