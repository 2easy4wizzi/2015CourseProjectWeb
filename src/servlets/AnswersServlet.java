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
import models.Answer;

import models.User;



/**
 * Servlet implementation class AnswersServlet.
 * It deals with requests of answers
 * @author gilad eini
 * @author ilana veitzblit
 */
public class AnswersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AnswersServlet() {
        super();

    }

    /**
     * doGet is used to get info in the DB.
     * this doGet used only for getting answers for a given question
     * @param qid the qid of the question that we need to get the answers for
     * @return array in json of all the answers for the question.
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Enumeration<String> params = request.getParameterNames();while(params.hasMoreElements()){ String paramName = (String)params.nextElement(); System.out.println("Attribute: "+paramName+", Value: "+request.getParameter(paramName));}
		
		Connection conn = null;
		PrintWriter out = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
		{
			String uri = request.getRequestURI();
			uri = uri.substring(uri.indexOf("AnswersServlet") + "AnswersServlet".length() + 1);
			System.out.println("get- " + uri);
			out = response.getWriter();
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context.lookup(DBConstants.DB_DATASOURCE);
			 conn = ds.getConnection();
			/**
			 * GetAnswers get the answers for the given qid and return them
			 */
			if(uri.equals("GetAnswers"))
			{
				Collection<Answer> answers = new ArrayList<Answer>(); 
				try
				{
					 ps = conn.prepareStatement(DBConstants.SELECT_ANSWERS_ORDER_BY_VOTES_AND_TIMESTAMP_BY_QID_STMT);
					String temp = request.getParameter("qid");
					int qid = Integer.parseInt(temp);
					ps.setInt(1, qid);
					 rs = (ResultSet) ps.executeQuery();
					
					while (rs.next()){
						java.sql.Timestamp ts = java.sql.Timestamp.valueOf(rs.getString(6));
						long tsTime = ts.getTime();
						DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
						java.sql.Date startDate = new java.sql.Date(ts.getTime());
						String createdHuman = df.format(startDate);
						
						PreparedStatement psPhoto = conn.prepareStatement(DBConstants.SELECT_PHOTO_BY_NICKNAME_STMT);
						psPhoto.setString(1, rs.getString("OwnerNickname"));
						ResultSet rsPhoto = psPhoto.executeQuery();
						String urlOwnerPhoto = null;
						while (rsPhoto.next()){
							urlOwnerPhoto = rsPhoto.getString("PhotoUrl");
						}
						
						answers.add(new Answer(rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getString(4),rs.getInt(5),createdHuman,tsTime,urlOwnerPhoto));
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
				Gson gson = new Gson();
				String answersJson = gson.toJson(answers, DBConstants.NEW_ANSWER_COLLECTION);
				out.println(answersJson);
				out.close();
			}
		}
		catch (SQLException | NamingException e) 
		{
			e.printStackTrace();
		}
		finally
		{
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
     * this doPut used only for adding vote to an answer
     * @param qid the qid of the question that the answer belong to
     * @param aid the aid of the answer that was voted on
     * @param voteValue the voteValue of vote. +1 or -1
     * @return new Question rating
     */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Enumeration<String> params = request.getParameterNames();while(params.hasMoreElements()){ String paramName = (String)params.nextElement(); System.out.println("Attribute: "+paramName+", Value: "+request.getParameter(paramName));}
		
		Connection conn = null;
		PrintWriter out = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
		{
			String uri = request.getRequestURI();
			uri = uri.substring(uri.indexOf("AnswersServlet") + "AnswersServlet".length() + 1);
			System.out.println("put- " + uri);
			User user = (User)(request.getSession().getAttribute("user"));
			out = response.getWriter();
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context.lookup(DBConstants.DB_DATASOURCE);
			conn = ds.getConnection();
			/**
			 * in addVote case we first check that the user is not the owner of the answer and that this is his first vote on it
			 * than sav his vote value in answer_vote tbl. and increment the total answers for this question
			 * than recalculate question rating and user rating
			 */
			if(uri.equals("addVote"))
			{ 
				try
				{
					Gson gson = new Gson();
					 ps = conn.prepareStatement(DBConstants.SELECT_ANSWER_BY_AID_STMT);
					String strAid = request.getParameter("aid");
					int aid = Integer.parseInt(strAid);
					ps.setInt(1, aid);
					 rs = (ResultSet) ps.executeQuery();
					
					User userA = (User)(request.getSession().getAttribute("user"));
					String answerOwner = null;
					
					while (rs.next()){
						answerOwner = rs.getString("OwnerNickname");
										
					}
					ps.close();
					rs.close();
					
					if (userA.getNickname().equals(answerOwner)){
						
						String strJson = gson.toJson("cant vote to your own answer", String.class);
						out.println(strJson);
						//out.println("1");
					}
					else 
					{
						ps = conn.prepareStatement(DBConstants.SELECT_ANSWER_VOTES_STMT);
						ps.setInt(1, aid);
						ps.setString(2, user.getNickname());
						rs = (ResultSet) ps.executeQuery();				
						if(!rs.next()){
							ps = conn.prepareStatement(DBConstants.INSERT_ANSWER_VOTE_STMT);
							ps.setInt(1, aid);
							String temp = request.getParameter("qid");
							int qid = Integer.parseInt(temp);
							ps.setInt(2, qid);
							ps.setString(3, user.getNickname());
							String strVoteVal = request.getParameter("voteValue");
							int voteVal = Integer.parseInt(strVoteVal);
							ps.setInt(4, voteVal);
							ps.executeUpdate();
							conn.commit();
							
							ps = conn.prepareStatement(DBConstants.UPDATE_ANSWER_QVOTES_BY_AID_STMT);			
							ps.setInt(1, voteVal);
							ps.setInt(2, aid);
							ps.executeUpdate();
							
							
							
							ps = conn.prepareStatement(DBConstants.GET_AVG_RATING_OF_QUESTION_ANSWERS);
							ps.setInt(1, qid);
							rs = (ResultSet) ps.executeQuery();	
							double answersAvgRating = 0;
							while (rs.next()){
								answersAvgRating = rs.getDouble(1);
							}		
							
							ps = conn.prepareStatement(DBConstants.SELECT_QVOTES_BY_QID_STMT);	
							ps.setInt(1, qid);
							rs = ps.executeQuery();
							int qvotes = 0;
							while (rs.next()){
								qvotes = rs.getInt(1);
							}
							double qRating = (((double)qvotes * 0.2) + (answersAvgRating * 0.8));
							
							ps = conn.prepareStatement(DBConstants.UPDATE_QRATING_BY_FORMULA_STMT);	
							
							ps.setDouble(1, qRating);
							ps.setInt(2, qid);
							ps.executeUpdate();
							System.out.println(qRating+ " xxxxxxxx");
				
							/*********************************UPDATE USER'S RATING****************************************/
							//Update the owner of the answer
							ps = conn.prepareStatement(DBConstants.GET_AVG_RATING_OF_QUESTIONS_BY_USER);	
							ps.setString(1, answerOwner);
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
							ps.setString(1, answerOwner);
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
													
							
							ps = conn.prepareStatement(DBConstants.UPDATE_USER_RATING);
							ps.setDouble(1, userRating);
							ps.setString(2, answerOwner);		
							ps.executeUpdate();
							conn.commit();
							
							//UPDATE THW OWNER OF THE QUESTION							
							//find who ask the question	that you answered				
							ps = conn.prepareStatement(DBConstants.SELECT_OWNER_BY_QID);
							ps.setInt(1, qid);
							ResultSet rsOwner = (ResultSet) ps.executeQuery();
							String ownerNickname = null;
							while (rsOwner.next()){
								ownerNickname = rsOwner.getString(1);
							}
							
							//update the owner of the Question that voted on its answer
							ps = conn.prepareStatement(DBConstants.GET_AVG_RATING_OF_QUESTIONS_BY_USER);
							ps.setString(1, ownerNickname);
							Owner = (ResultSet) ps.executeQuery();
							 avgQuestions = 0;
							 while (Owner.next()){
							 if(Owner.getObject(1) != null)							
								 avgQuestions = Owner.getDouble(1);	
							 else{
								 avgQuestions = 0;
							 }
							}
						    //calculate AVG in answer table
							ps = conn.prepareStatement(DBConstants.GET_AVG_RATING_OF_ANSWERS_BY_USER);
							ps.setString(1, ownerNickname);
							Owner = (ResultSet) ps.executeQuery();
							 avgAnswer = 0;
							 while (Owner.next()){
							 if(Owner.getObject(1) != null)							
								 avgAnswer = Owner.getDouble(1);	
							 else{
								 avgAnswer = 0;
							 }
							}
						    userRating = 0.2 * avgQuestions + 0.8 * avgAnswer;
													
							
							ps = conn.prepareStatement(DBConstants.UPDATE_USER_RATING);
							ps.setDouble(1, userRating);
							ps.setString(2, ownerNickname);		
							ps.executeUpdate();
							conn.commit();
																			
							/*********************************************************************************************/
														
							ps = conn.prepareStatement(DBConstants.SELECT_QUESTION_BY_QID_STMT);	
							ps.setInt(1, qid);
							rs = (ResultSet) ps.executeQuery();	
							double newQRating = 0;
							while (rs.next()){
								newQRating = rs.getDouble(5);
							}
							
							DecimalFormat df = new DecimalFormat("#.##");
							String dx=df.format(newQRating);
							newQRating=Double.valueOf(dx);
							
							out.println(newQRating);
							conn.commit();
						}
						else{
							String strJson = gson.toJson("already voted to this answer", String.class);
							out.println(strJson);
							//out.println("2");
						}
					}
					//recalc rating
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

				out.close();
			}
			
		}
		catch (SQLException | NamingException e) 
		{
			e.printStackTrace();
		}
		finally
		{
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
	
	/**
	 * doPost is used for posting a new answer and updating the question and the user's rating
	 *  @param answerText the text of the answer
	 *  @param qid the id of the question that the answer belongs to
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Enumeration<String> params = request.getParameterNames();while(params.hasMoreElements()){ String paramName = (String)params.nextElement(); System.out.println("Attribute: "+paramName+", Value: "+request.getParameter(paramName));}
		
		Connection conn = null;
		PrintWriter out = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
		{
			String uri = request.getRequestURI();
			uri = uri.substring(uri.indexOf("AnswersServlet") + "AnswersServlet".length() + 1);
			System.out.println("post- " + uri);
			out = response.getWriter();
			User user = (User)(request.getSession().getAttribute("user"));
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context.lookup(DBConstants.DB_DATASOURCE);
			 conn = ds.getConnection();
			
			
			if(uri.equals("PostAnswer"))
			{
				try
				{
					 ps = conn.prepareStatement(DBConstants.INSERT_ANSWER_STMT);			
					String questionId = request.getParameter("qid");
					int qid = Integer.parseInt(questionId);
					ps.setInt(1, qid);
					ps.setString(2, request.getParameter("answerText"));
					ps.setString(3, user.getNickname());
					ps.executeUpdate();
					ps.close();
					
					
					
/************************************UPDATE question RATING*********************************************************/
					
					ps = conn.prepareStatement(DBConstants.GET_AVG_RATING_OF_QUESTION_ANSWERS);
					ps.setInt(1, qid);
					 rs = (ResultSet) ps.executeQuery();	
					double answersAvgRating = 0;
					while (rs.next()){
						answersAvgRating = rs.getDouble(1);
					}
					
					
					rs.close();
					ps = conn.prepareStatement(DBConstants.SELECT_QVOTES_BY_QID_STMT);	
					ps.setInt(1, qid);
					rs = ps.executeQuery();
					int qvotes = 0;
					while (rs.next()){
						qvotes = rs.getInt(1);
					}
					double qRating = (((double)qvotes * 0.2) + (answersAvgRating * 0.8));
					
					
					ps = conn.prepareStatement(DBConstants.UPDATE_QRATING_BY_FORMULA_STMT);	
					
					ps.setDouble(1, qRating);
					ps.setInt(2, qid);
					ps.executeUpdate();
					conn.commit();
					
					
					/*********************************UPDATE USER'S RATING****************************************/
					//Update who post answer
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
						 double userRating = 0.2 * avgQuestions + 0.8 * avgAnswer;
											
					
					ps = conn.prepareStatement(DBConstants.UPDATE_USER_RATING);
					ps.setDouble(1, userRating);
					ps.setString(2, user.getNickname());		
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
					
					ps = conn.prepareStatement(DBConstants.GET_AVG_RATING_OF_QUESTIONS_BY_USER);
					ps.setString(1, ownerNickname);
					Owner = (ResultSet) ps.executeQuery();
					 avgQuestions = 0;
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
					 avgAnswer = 0;
					 while (Owner.next()){
					 if(Owner.getObject(1) != null)							
						 avgAnswer = Owner.getDouble(1);	
					 else{
						 avgAnswer = 0;
					 }
					}
						 userRating = 0.2 * avgQuestions + 0.8 * avgAnswer;
											
					
					ps = conn.prepareStatement(DBConstants.UPDATE_USER_RATING);
					ps.setDouble(1, userRating);
					ps.setString(2, ownerNickname);		
					ps.executeUpdate();
					conn.commit();								
					/*********************************************************************************************/
					
					ps.close();
					rs.close();
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
