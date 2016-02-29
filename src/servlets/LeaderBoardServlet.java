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
 * Servlet implementation class LeaderBoard
 * @author Gilad Eini
 * @author Ilana Veitzblit
 */
public class LeaderBoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LeaderBoardServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * this function is used to get 20 top user by rating from an offset given
	 * @return users that registered in the system
	 * @throws SQLException
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
		try
		{
			
			String uri = request.getRequestURI();
			uri = uri.substring(uri.indexOf("LeaderBoardServlet") + "LeaderBoardServlet".length() + 1);
			PrintWriter out = response.getWriter();
			User user = (User)(request.getSession().getAttribute("user"));
			request.getSession().setAttribute("user", user);
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context.lookup(DBConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();

			//Get users that registered in our web site
			if(uri.equals("getUsers"))
				{
			    	Collection<String> expertise = null;
					Collection<User> top20users = new ArrayList<User>();
					Gson gson = new Gson();
					try
					{
						//get 20 users ordered by user rating
						PreparedStatement ps = conn.prepareStatement(DBConstants.SELECT_TOP_20_USERS_BY_USER_RATING_STMT);
						ResultSet rs = (ResultSet) ps.executeQuery();
								
						while (rs.next()){
							//get expertise for each user
							PreparedStatement psE = conn.prepareStatement(DBConstants.SELECT_TOP_5_TOPICS_BY_POPULARITY_STMT);
							
							psE.setString(1, rs.getString("Nickname"));
							ResultSet rsE = (ResultSet) psE.executeQuery();
							expertise = new ArrayList<String>();			
							if(rsE != null){	
							while (rsE.next()){
								expertise.add(new String(rsE.getString("QTopics")));	
							}
							rsE.close();
							psE.close();
							}
							double Urating = rs.getDouble(6);
							//formating
							DecimalFormat dfRating = new DecimalFormat("#.##");
							String dxRating=dfRating.format(Urating);
							Urating=Double.valueOf(dxRating);
							top20users.add(new User(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),Urating,expertise));
						}
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
					//return user collection
					String top20usersJson = gson.toJson(top20users, DBConstants.NEW_USER_COLLECTION);
					out.println(top20usersJson);
					out.close();
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


