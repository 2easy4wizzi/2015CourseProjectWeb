package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import com.google.gson.Gson;

import constants.DBConstants;
import models.User;

/**
 * Servlet implementation class UserProfileServlet
 */
public class UserProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserProfileServlet() {
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

		try
		{
			String uri = request.getRequestURI();
			uri = uri.substring(uri.indexOf("LeaderBoardServlet") + "LeaderBoardServlet".length() + 1);
System.out.println(uri);
			PrintWriter out = response.getWriter();
			User user = (User)(request.getSession().getAttribute("user"));
//user = new User("gilad","123","wizzi",null,null,0);
			request.getSession().setAttribute("user", user);
			if(user == null)
			{
				out.println("0");
				out.close();
				return;
			}
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context.lookup(DBConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
		
		
			if(uri.equals("getUserDetails"))
			{
				
				
				Collection<User> userToShow = new ArrayList<User>();
				//Gson gson = new Gson();
				try
				{
					String userForShowing = request.getParameter("userToShow");
					PreparedStatement ps = conn.prepareStatement(DBConstants.SELECT_USER_BY_NICKNAME_STMT);
					ps.setString(1, userForShowing);
					ResultSet rs = (ResultSet) ps.executeQuery();
					

					
					while (rs.next()){
						double Urating = rs.getDouble(6);
						DecimalFormat dfRating = new DecimalFormat("#.##");
						String dxRating=dfRating.format(Urating);
						Urating=Double.valueOf(dxRating);
						userToShow.add(new User(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),Urating));
					}
					
					//conn.commit();
					rs.close();
					ps.close();
				}
				
				
				
				
				catch (SQLException  e) 
				{
					getServletContext().log("Error while closing connection", e);
					response.sendError(500);// internal server error
				}
				finally{
					conn.close();
				}
				//String top20usersJson = gson.toJson(top20users, DBConstants.NEW_USER_COLLECTION);
System.out.println(userToShow);
				out.println(userToShow);
				out.close();
			}
		
		
		
		}
		catch (SQLException | NamingException e) 
		{
			e.printStackTrace();
		}		
	}
}




