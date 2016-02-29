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
import models.UserQuestionUnswer;


/**
 * Servlet implementation class UserProfileServlet
 * @author gilad eini
 * @author ilana veitzblit
 */
public class UserProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserProfileServlet() {
        super();
    }



	/**
	 * this function implements get request in the user profile domain
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * 
	 */
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try
		{
			String uri = request.getRequestURI();
			uri = uri.substring(uri.indexOf("UserProfileServlet") + "UserProfileServlet".length() + 1);
			System.out.println("get- " + uri);
			PrintWriter out = response.getWriter();
			User user = (User)(request.getSession().getAttribute("user"));
			request.getSession().setAttribute("user", user);
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
			/**
			 * this segment gets the information of a user that was clicked on
			 * @param userToShow the nickname of the user requested
			 * @return the user clicked details
			 */
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
						userToShow.add(new User(null,null,rs.getString(3),rs.getString(4),rs.getString(5),Urating,null));
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
			/**
			 * this segment gets the last 5 question that the user that was clicked posted
			 * @param userToShow the nickname of the user requested
			 * @return array of 5 question if there are 5
			 */
			else if(uri.equals("last5Questions")){
			
				try{
				Collection<Question> last5Questions = new ArrayList<Question>();
					
				PreparedStatement ps = conn.prepareStatement(DBConstants.SELECT_TOP_5_LAST_QUESTION_FOR_USER_STMT);
				ps.setString(1, userForShowing);
				ResultSet rs = (ResultSet) ps.executeQuery();
				while (rs.next()){
					java.sql.Timestamp ts = java.sql.Timestamp.valueOf(rs.getString(6));
					long tsTime = ts.getTime();
					DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");					java.sql.Date startDate = new java.sql.Date(ts.getTime());
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
		}
			/**
			 * this segment gets the last 5 question that the user that was clicked posted
			 * @param userToShow the nickname of the user requested
			 * @return array of 5 question if there are 5
			 */
			else if(uri.equals("getExpertise")){
			
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
			/**
			 * this segment gets the last 5 answer that the user that was clicked answered plus the question the answer belongs to.
			 * @param userToShow the nickname of the user requested
			 * @return array of 5 answers and their 5 questions if any exists
			 */
			else if(uri.equals("getQuestionForAnswer")){
				try{
					Collection<UserQuestionUnswer> last5AnsweredQuestions = new ArrayList<UserQuestionUnswer>();
				
					PreparedStatement psQuestion = null;
					PreparedStatement ps = conn.prepareStatement(DBConstants.SELECT_TOP_5_LAST_ANSWERS_FOR_USER_STMT);
					
					ps.setString(1, userForShowing);
					ResultSet rs = (ResultSet) ps.executeQuery();
					while (rs.next()){
						
						psQuestion = conn.prepareStatement(DBConstants.SELECT_QUESTION_BY_QID_STMT);
						psQuestion.setInt(1, rs.getInt("QId"));
						ResultSet rsQ = (ResultSet) psQuestion.executeQuery();
											
						while (rsQ.next()){
							java.sql.Timestamp ts = java.sql.Timestamp.valueOf(rsQ.getString("Created"));
							long tsTime = ts.getTime();
							DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");							java.sql.Date startDate = new java.sql.Date(ts.getTime());
							String createdHuman = df.format(startDate);
							double Qrating = rsQ.getDouble("QRating");
							DecimalFormat dfRating = new DecimalFormat("#.##");
							String dxRating=dfRating.format(Qrating);
							Qrating=Double.valueOf(dxRating);
							
							last5AnsweredQuestions.add(new UserQuestionUnswer(rs.getInt("QId"),rs.getString("AnswerText"),rsQ.getString("QuestionText"),rs.getInt("AVotes"),Qrating,createdHuman,tsTime));
						}
						rsQ.close();
						psQuestion.close();
					}
										
					String last5AnsweredQuestionsJson = gson.toJson(last5AnsweredQuestions, DBConstants.NEW_ANSWER_COLLECTION);
					out.println(last5AnsweredQuestionsJson);
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




