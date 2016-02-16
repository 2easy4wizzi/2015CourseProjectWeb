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
import java.util.Enumeration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

import org.apache.derby.tools.sysinfo;
import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import constants.DBConstants;
import models.Question;

import models.User;

/**
 * Servlet implementation class QuestionsServlet
 */
public class QuestionsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuestionsServlet() {
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
//Enumeration<String> params = request.getParameterNames(); while(params.hasMoreElements()){String paramName = (String)params.nextElement();System.out.println("Attribute: "+paramName+", Value: "+request.getParameter(paramName));}
		try
		{
			String uri = request.getRequestURI();
			uri = uri.substring(uri.indexOf("QuestionsServlet") + "QuestionsServlet".length() + 1);
//System.out.println(uri);
			PrintWriter out = response.getWriter();
			User user = (User)(request.getSession().getAttribute("user"));
user = new User("gilad","123","wizzi",null,null);
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
			
			
			if(uri.equals("PostQuestion"))
			{
				try
				{
					PreparedStatement ps = conn.prepareStatement(DBConstants.INSERT_QUESTION_STMT);			
					ps.setString(1, request.getParameter("questionText"));
					ps.setString(2, request.getParameter("topics"));
					ps.setString(3, user.getNickname());
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
			else if(uri.equals("incQuestionAnswers"))
			{ 
				try
				{
					PreparedStatement ps = conn.prepareStatement(DBConstants.UPDATE_QUESTION_ANSWERS_COLUMN_BY_QID_STMT);
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
				finally{
					conn.close();
				}
	        	out.close();
			}
			else if(uri.equals("addVote"))
			{ 
				try
				{
					PreparedStatement ps = conn.prepareStatement(DBConstants.SELECT_QUESTION_BY_QID_STMT);
					String strQid = request.getParameter("qid");
					int qid = Integer.parseInt(strQid);
					ps.setInt(1, qid);
					ResultSet rs = (ResultSet) ps.executeQuery();
					
					User userA = (User)(request.getSession().getAttribute("user"));
					String questionOwner = null;
					double questoinRating=0;
					int questoinVotes = 0;
					while (rs.next()){
						questionOwner = rs.getString("OwnerNickname");
						questoinRating= rs.getDouble("QRating");
						questoinVotes = rs.getInt("QVotes");
					}
questionOwner = "bla";
					if (userA.getNickname().equals(questionOwner)){
						out.println("cant vote to your own question");
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
							DecimalFormat df = new DecimalFormat("#.##");
							String dx=df.format(questoinRating);
							questoinRating=Double.valueOf(dx);
							ps.setDouble(2, questoinRating);
							ps.setInt(3, qid);
							ps.executeUpdate();
								
							conn.commit();
						}
						else{
							out.println("already voted to this question");
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
			else if(uri.equals("GetNewTop20"))
			{
				Collection<Question> top20new = new ArrayList<Question>();
				int count = 0;
				int from = 0;
				Gson gson = new Gson();
				try
				{
					PreparedStatement psCount = conn.prepareStatement(DBConstants.SELECT_COUNT_NEW_QUESTIONS_STMT);
					ResultSet rsCount = (ResultSet) psCount.executeQuery();
					while (rsCount.next()){
						  count = rsCount.getInt(1);

					}
					//System.out.println("count is:"+count);
					
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
					
					PreparedStatement ps = conn.prepareStatement(DBConstants.SELECT_TOP_20_NEW_QUESTIONS_BY_TIMESTAMP_STMT);
					
					String strFrom = request.getParameter("top20from");
					from = Integer.parseInt(strFrom) * 20;
					ps.setInt(1, from);
					ResultSet rs = (ResultSet) ps.executeQuery();
					
					while (rs.next()){
						java.sql.Timestamp ts = java.sql.Timestamp.valueOf(rs.getString(7));
						long tsTime = ts.getTime();
						DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
						java.sql.Date startDate = new java.sql.Date(ts.getTime());
						String createdHuman = df.format(startDate);
						top20new.add(new Question(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getDouble(5),rs.getInt(6),createdHuman ,tsTime,rs.getInt(8)));
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
				//out.println(top20newJson);
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
					
					PreparedStatement ps = conn.prepareStatement(DBConstants.SELECT_TOP_20_QUESTIONS_BY_TIMESTAMP_STMT);
					
					String strFrom = request.getParameter("top20from");
					from = Integer.parseInt(strFrom) * 20;
					ps.setInt(1, from);
					ResultSet rs = (ResultSet) ps.executeQuery();
					
					while (rs.next()){
						java.sql.Timestamp ts = java.sql.Timestamp.valueOf(rs.getString(7));
						long tsTime = ts.getTime();
						DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
						java.sql.Date startDate = new java.sql.Date(ts.getTime());
						String createdHuman = df.format(startDate);
						top20new.add(new Question(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getDouble(5),rs.getInt(6),createdHuman ,tsTime,rs.getInt(8)));
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
