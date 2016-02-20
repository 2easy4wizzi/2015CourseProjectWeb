package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import models.Question;
import models.Topic;
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
			uri = uri.substring(uri.indexOf("UserProfileServlet") + "UserProfileServlet".length() + 1);
//System.out.println(uri);
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
			Gson gson = new Gson();
			String userForShowing = null;
			String whichUser = request.getParameter("userToShow");
			if(whichUser.equals("logNow")){
				User userA = (User)(request.getSession().getAttribute("user"));
				userForShowing = userA.getNickname();
			}
			else{
				userForShowing = request.getParameter("userToShow");
				
			}
		
			if(uri.equals("getUserDetails"))
			{				
				Collection<User> userToShow = new ArrayList<User>();

				try
				{
					
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
					
					
					
					String userDisplaed = gson.toJson(userToShow, DBConstants.NEW_QUESTION_COLLECTION);

					out.println(userDisplaed);

					out.close();
					conn.commit();
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
				
			}
			else if(uri.equals("last5Questions")){
			
				try{
				Collection<Question> last5Questions = new ArrayList<Question>();


					
				PreparedStatement ps = conn.prepareStatement(DBConstants.SELECT_TOP_5_LAST_QUESTION_FOR_USER_STMT);
				ps.setString(1, userForShowing);
				ResultSet rs = (ResultSet) ps.executeQuery();
				while (rs.next()){
					java.sql.Timestamp ts = java.sql.Timestamp.valueOf(rs.getString(6));
					long tsTime = ts.getTime();
					DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
					java.sql.Date startDate = new java.sql.Date(ts.getTime());
					String createdHuman = df.format(startDate);
					double Qrating = rs.getDouble(4);
					DecimalFormat dfRating = new DecimalFormat("#.##");
					String dxRating=dfRating.format(Qrating);
					Qrating=Double.valueOf(dxRating);
					
					last5Questions.add(new Question(rs.getInt(1),rs.getString(2),rs.getString(3),Qrating,rs.getInt(5),createdHuman ,tsTime,rs.getInt(7)));
				}
				String last5QuestionsJson = gson.toJson(last5Questions, DBConstants.NEW_QUESTION_COLLECTION);

				out.println(last5QuestionsJson);
				out.close();

				rs.close();
				ps.close();
				
				
			}catch (SQLException  e) 
				{
				getServletContext().log("Error while closing connection", e);
				response.sendError(500);// internal server error
			}
			finally{
				conn.close();
			}
			
		}else if(uri.equals("getExpertise")){
			
			try{
				Collection<Topic> expertise = new ArrayList<Topic>();
		PreparedStatement ps = conn.prepareStatement(DBConstants.SELECT_TOP_5_TOPICS_BY_POPULARITY_STMT);

		ps.setString(1, userForShowing);
		ResultSet rs = (ResultSet) ps.executeQuery();
		
		while (rs.next()){
			expertise.add(new Topic(rs.getString("QTopics") , rs.getDouble("sigma")));
		}
		
			
		String expertiseArrayJson = gson.toJson(expertise, DBConstants.NEW_TOPIC_COLLECTION);

		out.println(expertiseArrayJson);
		out.close();

		rs.close();
		ps.close();
			}catch (SQLException  e) 
			{
			getServletContext().log("Error while closing connection", e);
			response.sendError(500);// internal server error
		}
		finally{
			conn.close();
		}
			
		}
		
		
		
		}
		catch (SQLException | NamingException e) 
		{
			e.printStackTrace();
		}		
	}
}




