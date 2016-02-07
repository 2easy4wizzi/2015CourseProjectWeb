package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Random;

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
 * Servlet implementation class Questions
 */
public class Questions extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Questions() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Enumeration<String> params = request.getParameterNames(); 
		while(params.hasMoreElements())
		{
		 String paramName = (String)params.nextElement();
		 System.out.println("Attribute: "+paramName+", Value: "+request.getParameter(paramName));
		}
		try
		{
			String uri = request.getRequestURI();
			uri = uri.substring(uri.indexOf("Questions") + "Questions".length() + 1);
			System.out.println(uri);
			PrintWriter out = response.getWriter();
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context.lookup(DBConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			
			
			if(uri.equals("PostQuestion"))
			{
				try
				{
					PreparedStatement ps = conn.prepareStatement(DBConstants.INSERT_QUESTION_STMT);
					String fkQuery =  "SELECT * FROM TBL_USERS WHERE Nickname='" + request.getParameter("nickname") + "'";
					PreparedStatement ps2 = conn.prepareStatement(fkQuery);
					ResultSet rs = (ResultSet) ps2.executeQuery();
					while (rs.next()) {
				        for (int i = 1; i <= 6; i++) {
				            if (i > 1) System.out.print(" | ");
				            System.out.print(rs.getString(i));
				        }
				        System.out.println("");
				    }
					
					Random rand = new Random();
					int  n = rand.nextInt(1150) + 1;
					
					ps.setInt(1, n);
					ps.setString(2, request.getParameter("question"));
					ps.setString(3, request.getParameter("topics"));
					ps.setString(4, request.getParameter(fkQuery));
					ps.executeUpdate();
					
					conn.commit();
					ps.close();
				}
				catch (SQLException  e) 
				{
					getServletContext().log("Error while closing connection", e);
					response.sendError(500);// internal server error
				}
				finally{
					conn.close();
					out.close();
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
	
		
		
	}

}
