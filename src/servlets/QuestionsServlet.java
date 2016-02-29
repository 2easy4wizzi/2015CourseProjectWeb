package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.ResultSet;
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
import com.google.gson.Gson;


import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import constants.DBConstants;
import models.Question;
import models.User;

/**
 * this servlet implement different requests related to questions. 
 * @author gilad eini
 * @author ilana veitzblit
 */
public class QuestionsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuestionsServlet() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    /**
     * doGet is used to get info from the servlet. depending on the js function that calls doGet, different info will be fetched.
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Connection conn = null;
		PrintWriter out = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			String uri = request.getRequestURI();
			uri = uri.substring(uri.indexOf("QuestionsServlet") + "QuestionsServlet".length() + 1);
			System.out.println("get- " + uri);
			out = response.getWriter();
			//User user = (User)(request.getSession().getAttribute("user"));
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context.lookup(DBConstants.DB_DATASOURCE);
			conn = ds.getConnection();
			/**
			 * this segment gets the top 20 new question from an offset. in addtion, it also see if the "next button" should apear in the html.
			 * @param from - offset to get 20 questions from it.
			 * @return array of top new 20 question plus info if the next button should be available
			 */
			if(uri.equals("GetNewTop20"))
			{
				
				Collection<Question> top20new = new ArrayList<Question>();
				int count = 0;
				int from = 0;
				Gson gson = new Gson();
				try
				{
					ps = conn.prepareStatement(DBConstants.SELECT_COUNT_NEW_QUESTIONS_STMT);
					rs = ps.executeQuery();
					while (rs.next()){
						  count = rs.getInt(1);
					}
					ps.close();
					rs.close();
					if(count == 0)
					{
						boolean dontShowNextButton = true;
						String boolJson = gson.toJson(dontShowNextButton, boolean.class);
						String noQstr = "noQuestionsFound";
						String strJson = gson.toJson(noQstr, String.class);
						String outRespone = "[" + boolJson + "," + strJson + "]";
						out.println(outRespone);
						out.close();
					}
					else{
						ps = conn.prepareStatement(DBConstants.SELECT_TOP_20_NEW_QUESTIONS_BY_TIMESTAMP_STMT);
						String strFrom = request.getParameter("top20from");
						from = Integer.parseInt(strFrom) * 20;
						ps.setInt(1, from);
						rs = ps.executeQuery();
						while (rs.next()){
							java.sql.Timestamp ts = java.sql.Timestamp.valueOf(rs.getString(6));
							long tsTime = ts.getTime();
							DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");							java.sql.Date startDate = new java.sql.Date(ts.getTime());
							String createdHuman = df.format(startDate);
							double Qrating = rs.getDouble(4);
							DecimalFormat dfRating = new DecimalFormat("#.##");
							String dxRating=dfRating.format(Qrating);
							Qrating=Double.valueOf(dxRating);
							
							PreparedStatement psPhoto = conn.prepareStatement(DBConstants.SELECT_PHOTO_BY_NICKNAME_STMT);
							psPhoto.setString(1, rs.getString("OwnerNickname"));
							ResultSet rsPhoto = psPhoto.executeQuery();
							String urlOwnerPhoto = null;
							while (rsPhoto.next()){
								urlOwnerPhoto = rsPhoto.getString("PhotoUrl");
							}
							
							top20new.add(new Question(rs.getInt(1),rs.getString(2),rs.getString(3),Qrating,rs.getInt(5),createdHuman ,tsTime,rs.getInt(7),urlOwnerPhoto));
						}
						boolean dontShowNextButton = false;
						if(count <= from+20 )
						{
							dontShowNextButton = true;
						}
						String top20newJson = gson.toJson(top20new, DBConstants.NEW_QUESTION_COLLECTION);
						String boolJson = gson.toJson(dontShowNextButton, boolean.class);
						String outRespone = "[" + boolJson + "," + top20newJson + "]";
						out.println(outRespone);
					}
				}
				catch (SQLException  e) 
				{
					getServletContext().log("Error while closing connection", e);
					response.sendError(500);// internal server error
				}
			}
			/**
			 * this segment gets the top 20  question from an offset. in addtion, it also see if the "next button" should apear in the html.
			 * @param from - offset to get 20 questions from it.
			 * @return array of top 20 question plus info if the next button should be available
			 */
			else if(uri.equals("GetTop20"))
			{
				Collection<Question> top20new = new ArrayList<Question>();
				int count = 0;
				int from = 0;
				Gson gson = new Gson();
				try
				{
					PreparedStatement psCount = conn.prepareStatement(DBConstants.SELECT_COUNT_QUESTIONS_STMT);
					ResultSet rsCount = (ResultSet) psCount.executeQuery();
					while (rsCount.next()){
						  count = rsCount.getInt(1);
					 }
					
					
					rsCount.close();
					psCount.close();
					
					if(count == 0)
					{
						boolean dontShowNextButton = true;
						String boolJson = gson.toJson(dontShowNextButton, boolean.class);
						String noQstr = "noQuestionsFound";
						String strJson = gson.toJson(noQstr, String.class);
						String outRespone = "[" + boolJson + "," + strJson + "]";
						out.println(outRespone);
						out.close();
						return;
					}
					
					 ps = conn.prepareStatement(DBConstants.SELECT_TOP_20_QUESTIONS_BY_TIMESTAMP_STMT);
					
					String strFrom = request.getParameter("top20from");
					from = Integer.parseInt(strFrom) * 20;
					ps.setInt(1, from);
					 rs = (ResultSet) ps.executeQuery();
					
					while (rs.next()){
						java.sql.Timestamp ts = java.sql.Timestamp.valueOf(rs.getString(6));
						long tsTime = ts.getTime();
						DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");						java.sql.Date startDate = new java.sql.Date(ts.getTime());
						String createdHuman = df.format(startDate);
						double Qrating = rs.getDouble(4);
						DecimalFormat dfRating = new DecimalFormat("#.##");
						String dxRating=dfRating.format(Qrating);
						Qrating=Double.valueOf(dxRating);
						
						
						PreparedStatement psPhoto = conn.prepareStatement(DBConstants.SELECT_PHOTO_BY_NICKNAME_STMT);
						psPhoto.setString(1, rs.getString("OwnerNickname"));
						ResultSet rsPhoto = psPhoto.executeQuery();
						String urlOwnerPhoto = null;
						while (rsPhoto.next()){
							urlOwnerPhoto = rsPhoto.getString("PhotoUrl");
						}
						
						top20new.add(new Question(rs.getInt(1),rs.getString(2),rs.getString(3),Qrating,rs.getInt(5),createdHuman ,tsTime,rs.getInt(7),urlOwnerPhoto));
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
				String top20newJson = gson.toJson(top20new, DBConstants.NEW_QUESTION_COLLECTION);
				
				boolean dontShowNextButton = false;
				if(count <= from+20 )
				{
					dontShowNextButton = true;
				}
				
				String boolJson = gson.toJson(dontShowNextButton, boolean.class);
				String outRespone = "[" + boolJson + "," + top20newJson + "]";
				out.println(outRespone);
				out.close();
			}
			/**
			 * this segment gets the top 20 new question from an offset.
			 * it is used to understand if the page presenting the new questions has changed.
			 * @param from - offset to get 20 questions from it.
			 * @return array of top new 20 question 
			 */
			else if(uri.equals("Update"))
			{
				Collection<Question> top20new = new ArrayList<Question>();
				
				int from = 0;
				Gson gson = new Gson();
				try
				{
					 ps = conn.prepareStatement(DBConstants.SELECT_TOP_20_NEW_QUESTIONS_BY_TIMESTAMP_STMT);	
					String strFrom = request.getParameter("from");
					from = Integer.parseInt(strFrom) * 20;
					ps.setInt(1, from);
					 rs = (ResultSet) ps.executeQuery();
					
					while (rs.next()){
						java.sql.Timestamp ts = java.sql.Timestamp.valueOf(rs.getString(6));
						long tsTime = ts.getTime();
						DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");						java.sql.Date startDate = new java.sql.Date(ts.getTime());
						String createdHuman = df.format(startDate);
						double Qrating = rs.getDouble(4);
						DecimalFormat dfRating = new DecimalFormat("#.##");
						String dxRating=dfRating.format(Qrating);
						Qrating=Double.valueOf(dxRating);
						top20new.add(new Question(rs.getInt(1),rs.getString(2),rs.getString(3),Qrating,rs.getInt(5),createdHuman ,tsTime,rs.getInt(7)));
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
				String top20newJson = gson.toJson(top20new, DBConstants.NEW_QUESTION_COLLECTION);
				//System.out.println("JSON: " +top20newJson);
				out.println(top20newJson);	
				out.close();
			}
			/**
			 * this segment gets the top 20 new question from an offset BY A TOPIC.
			 * @param from - offset to get 20 questions from it.
			 * @param topic - the 20 question will be brought only in this topic.
			 * @return array of top 20 question in this topic plus info if the next button should be available
			 */
			else if(uri.equals("getQuestionsByTopic"))
			{
				Collection<Question> top20byTopic = new ArrayList<Question>();
				int count = 0;
				int from = 0;
				Gson gson = new Gson();
				try
				{
					String topicName = request.getParameter("topic");
					String strFrom = request.getParameter("from");
					from = Integer.parseInt(strFrom) * 20;
					
					
					 ps = conn.prepareStatement(DBConstants.SELECT_COUNT_QUESTIONS_BY_TOPIC_STMT);
					ps.setString(1, topicName);
					 rs = ps.executeQuery();
					while (rs.next()){
						  count = rs.getInt("totalQuestionOfTopic");
					}
					ps.close();
					rs.close();
					//System.out.println("count was: " + count);
					if(count == 0){
						String boolJson = gson.toJson(true, boolean.class);
						String strJson = gson.toJson("noQuestionsOnTopicsFound", String.class);
						String outRespone = "[" + boolJson + "," + strJson + "]";
						out.println(outRespone);
						//System.out.println("final collection: " + outRespone);	
					}
					else{
						ps = conn.prepareStatement(DBConstants.SELECT_TOP_20_QUESTIONS_BY_TOPIC_STMT);
						ps.setString(1, topicName);
						ps.setInt(2, from);
						rs = ps.executeQuery();
						
						
						while (rs.next()){
							java.sql.Timestamp ts = java.sql.Timestamp.valueOf(rs.getString("Created"));
							long tsTime = ts.getTime();
							DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");							java.sql.Date startDate = new java.sql.Date(ts.getTime());
							String createdHuman = df.format(startDate);
							double Qrating = rs.getDouble("QRating");
							DecimalFormat dfRating = new DecimalFormat("#.##");
							String dxRating=dfRating.format(Qrating);
							Qrating=Double.valueOf(dxRating);
							
							PreparedStatement psPhoto = conn.prepareStatement(DBConstants.SELECT_PHOTO_BY_NICKNAME_STMT);
							psPhoto.setString(1, rs.getString("OwnerNickname"));
							ResultSet rsPhoto = psPhoto.executeQuery();
							String urlOwnerPhoto = null;
							while (rsPhoto.next()){
								urlOwnerPhoto = rsPhoto.getString("PhotoUrl");
							}
							
							top20byTopic.add(new Question(rs.getInt(1),rs.getString(2),rs.getString(3),Qrating,rs.getInt(5),createdHuman ,tsTime,rs.getInt(7),urlOwnerPhoto));
						}

						String boolJson;
						if(count <= from+20 ){
							boolJson = gson.toJson(true, boolean.class);	
						}
						else{
							boolJson = gson.toJson(false, boolean.class);	
						}		
						String top20byTopicJSON = gson.toJson(top20byTopic, DBConstants.NEW_TOPIC_COLLECTION);
						String outRespone = "[" + boolJson + "," + top20byTopicJSON + "]";
						
						out.println(outRespone);							
						
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
					out.close();
				}
			}
		}
		catch (SQLException | NamingException e) 
		{
			e.printStackTrace();
		}
		finally {
			if (rs != null) {
		        try {
		            rs.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		    if (ps != null) {
		        try {
		            ps.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		    if (conn != null) {
		        try {
		            conn.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		    if (out != null) {
	    		out.close();
		    }
		}		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		PrintWriter out = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		//Enumeration<String> params = request.getParameterNames(); while(params.hasMoreElements()){String paramName = (String)params.nextElement();System.out.println("Attribute: "+paramName+", Value: "+request.getParameter(paramName));}
		try
		{
			String uri = request.getRequestURI();
			uri = uri.substring(uri.indexOf("QuestionsServlet") + "QuestionsServlet".length() + 1);
			System.out.println("post- "+ uri);
			out = response.getWriter();
			User user = (User)(request.getSession().getAttribute("user"));
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context.lookup(DBConstants.DB_DATASOURCE);
			conn = ds.getConnection();
			
			/**
			 * this segment post a new question by the user that is logged in.
			 * it also post the topics in another table with the QID.
			 * whenever a user post a new question, his rating changes, so we also update his new rating
			 * @param topics - array of topics that the user entered
			 * @param questionText - the text of the question that the user entered
			 * @param user.getNickname() - the nickname of the user who entered the questions. we get the details of the user from the session.
			 */
			if(uri.equals("PostQuestion"))
			{
				try
				{
					ps = conn.prepareStatement(DBConstants.INSERT_QUESTION_STMT, new String[] { "QID"});			
					ps.setString(1, request.getParameter("questionText"));
					ps.setString(2, user.getNickname());
					ps.executeUpdate();
					rs = ps.getGeneratedKeys();
					 
					int qid = -1;
					if (rs.next()) {
						qid = rs.getInt(1);
					}
					
					conn.commit();
					
					
					/*********************************INSERT TOPICS *********************************************/
					String topics = request.getParameter("topics");
					topics = topics.substring(1, topics.length()-1);
					
					
					if(topics.length() != 0)
					{
						String delims = "[,]+";
						String[] tokens = topics.split(delims);
						for (int i = 0; i < tokens.length; i++) {
							tokens[i] = tokens[i].substring(1, tokens[i].length()-1);
							ps = conn.prepareStatement(DBConstants.INSERT_TOPIC_STMT);	
							ps.setInt(1, qid);
							ps.setString(2,tokens[i]);
							ps.executeUpdate();
							conn.commit();
						}
					}
					
					ps.close();
					rs.close();
					
					
					/*********************************UPDATE USER'S RATING****************************************/
					ps = conn.prepareStatement(DBConstants.GET_AVG_RATING_OF_QUESTIONS_BY_USER);					
					ps.setString(1, user.getNickname());
					ResultSet Owner = (ResultSet) ps.executeQuery();
					 double avgQuestions = 0;
					 while (Owner.next()){
					 if(Owner.getObject(1) != null)							
						 avgQuestions = Owner.getDouble(1);	
					 else{
						 avgQuestions = 0;
					 }
					}
					 
					ps.close();
					Owner.close();
					
				    //calculate AVG in answer table
					ps = conn.prepareStatement(DBConstants.GET_AVG_VOTES_OF_ANSWERS_BY_USER);		
					ps.setString(1, user.getNickname());
					Owner = (ResultSet) ps.executeQuery();
					double avgAnswer = 0;
					while (Owner.next()){
						if(Owner.getObject(1) != null)							
							avgAnswer = Owner.getDouble(1);	
						else{
							 avgAnswer = 0;
						}
					}
					
					ps.close();
					Owner.close();
					double userRating = 0.2 * avgQuestions + 0.8 * avgAnswer;
											
					
					ps = conn.prepareStatement(DBConstants.UPDATE_USER_RATING);
					ps.setDouble(1, userRating);
					ps.setString(2, user.getNickname());		
					ps.executeUpdate();
					conn.commit();

					ps.close();
				}
				catch (SQLException  e) 
				{
					getServletContext().log("Error while closing connection", e);
					response.sendError(500);// internal server error
				}		
			}
			
			
			
			
			
			
			
		}
		catch (SQLException | NamingException e) 
		{
			e.printStackTrace();
		}
		finally {
			if (rs != null) {
		        try {
		            rs.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		    if (ps != null) {
		        try {
		            ps.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		    if (conn != null) {
		        try {
		            conn.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		    if (out != null) {
	    		out.close();
		    }
		}	
	
		
		
	}

	
	/**
     * doPut is used to put new info in the DB. depending on the js function that calls doPut, different info will be put in the DB.
     */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Connection conn = null;
		PrintWriter out = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			String uri = request.getRequestURI();
			uri = uri.substring(uri.indexOf("QuestionsServlet") + "QuestionsServlet".length() + 1);
			System.out.println("put- " + uri);
			out = response.getWriter();
			User user = (User)(request.getSession().getAttribute("user"));
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context.lookup(DBConstants.DB_DATASOURCE);
			conn = ds.getConnection();
			/**
			 * this segment increment by 1 the number of answer for the given question.
			 * @param qid is given to update the question
			 */
			if(uri.equals("incQuestionAnswers"))
			{ 
				try
				{
					ps = conn.prepareStatement(DBConstants.UPDATE_QUESTION_ANSWERS_COLUMN_BY_QID_STMT);
					String strQid = request.getParameter("qid");
					ps.setInt(1, Integer.parseInt(strQid));
					ps.executeUpdate();
					conn.commit();
					ps.close();
				}
				catch (SQLException  e) 
				{
					getServletContext().log("Error while closing connection", e);
					response.sendError(500);// internal server error
				}
			}
			/**
			 * this segment add a vote to a question.
			 * it checks first that the user didn't vote already on this Question and that the user is not the owner of the question
			 * 2 side effects also occur. the question rating and the user rating changes.
			 * so we update both of them as well
			 * @param qid - the question that user vote on.
			 * @param voteValue - +1 or -1
			 * @return on voting violation, returns the right error message
			 */
			else if(uri.equals("addVote"))
			{ 
				try
				{
					Gson gson = new Gson();
					ps = conn.prepareStatement(DBConstants.SELECT_QUESTION_BY_QID_STMT);
					String strQid = request.getParameter("qid");
					int qid = Integer.parseInt(strQid);
					ps.setInt(1, qid);
					rs = (ResultSet) ps.executeQuery();
					
					User userA = (User)(request.getSession().getAttribute("user"));
					String questionOwner = null;
					double questoinRating=0;
					int questoinVotes = 0;
					while (rs.next()){
						questionOwner = rs.getString("OwnerNickname");
						questoinRating= rs.getDouble("QRating");
						questoinVotes = rs.getInt("QVotes");
					}
					ps.close();
					rs.close();
					if (userA.getNickname().equals(questionOwner)){
						String intJson = gson.toJson(1, int.class);
						out.println(intJson);
					}
					else 
					{
						ps = conn.prepareStatement(DBConstants.SELECT_QUESTION_VOTES_STMT);
						ps.setInt(1, qid);
						ps.setString(2, user.getNickname());
						rs = (ResultSet) ps.executeQuery();				
						if(!rs.next()){
							ps = conn.prepareStatement(DBConstants.INSERT_QUESTION_VOTE_STMT);
							ps.setInt(1, qid);
							ps.setString(2, user.getNickname());
							String strVoteVal = request.getParameter("voteValue");
							int voteVal = Integer.parseInt(strVoteVal);
							ps.setInt(3, voteVal);
							ps.executeUpdate();
							conn.commit();
							
							ps = conn.prepareStatement(DBConstants.GET_AVG_RATING_OF_QUESTION_ANSWERS);
							ps.setInt(1, qid);
							rs = (ResultSet) ps.executeQuery();	
							double answersAvgRating = 0;
							while (rs.next()){
								answersAvgRating = rs.getDouble(1);
							}
							
							
							ps = conn.prepareStatement(DBConstants.UPDATE_QUESTION_QVOTES_AND_QRATING_COLUMNS_BY_QID_STMT);	
							questoinVotes += voteVal;
							ps.setInt(1, questoinVotes);
							questoinRating = (double)questoinVotes * 0.2 + answersAvgRating*0.8;
							ps.setDouble(2, questoinRating);
							ps.setInt(3, qid);
							ps.executeUpdate();
								
							conn.commit();
							/*********************************UPDATE USER'S RATING****************************************/
							//find who ask the question					
							ps = conn.prepareStatement(DBConstants.SELECT_OWNER_BY_QID);
							ps.setInt(1, qid);
							ResultSet rsOwner = (ResultSet) ps.executeQuery();
							String ownerNickname = null;
							while (rsOwner.next()){
								ownerNickname = rsOwner.getString(1);
							}
							
							//calculate AVG in question table
							ps = conn.prepareStatement(DBConstants.GET_AVG_RATING_OF_QUESTIONS_BY_USER);
							ps.setString(1, ownerNickname);
							ResultSet Owner = (ResultSet) ps.executeQuery();
							 double avgQuestions = 0;
							 while (Owner.next()){
							 if(Owner.getObject(1) != null)							
								 avgQuestions = Owner.getDouble(1);	
							 else{
								 avgQuestions = 0;
							 }
							}
							    //calculate AVG in answer table
								ps = conn.prepareStatement(DBConstants.GET_AVG_VOTES_OF_ANSWERS_BY_USER);
								ps.setString(1, ownerNickname);
								Owner = (ResultSet) ps.executeQuery();
								 double avgAnswer = 0;
								 while (Owner.next()){
								 if(Owner.getObject(1) != null)							
									 avgAnswer = Owner.getDouble(1);	
								 else{
									 avgAnswer = 0;
								 }
								}
								 double userRating = 0.2 * avgQuestions + 0.8 * avgAnswer;
													
							//update who ask the question 
							ps = conn.prepareStatement(DBConstants.UPDATE_USER_RATING);
							ps.setDouble(1, userRating);
							ps.setString(2, ownerNickname);		
							ps.executeUpdate();
							conn.commit();
												
							/*********************************************************************************************/
							String intJson = gson.toJson(0, int.class);
							out.println(intJson);
														
						}
						else{
							String intJson = gson.toJson(2, int.class);
							out.println(intJson);
						}
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
			}

		}
		catch (SQLException | NamingException e) 
		{
			e.printStackTrace();
		}
		finally {
			if (rs != null) {
		        try {
		            rs.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		    if (ps != null) {
		        try {
		            ps.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		    if (conn != null) {
		        try {
		            conn.close();
		        } catch (SQLException e) { /* ignored */}
		    }
		    if (out != null) {
	    		out.close();
		    }
		}		
	}
}
