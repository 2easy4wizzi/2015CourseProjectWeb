package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
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

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import com.google.gson.Gson;

import constants.DBConstants;
import models.Answer;
import models.Question;
import models.User;

/**
 * Servlet implementation class AnswersServlet
 */
public class AnswersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AnswersServlet() {
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
			uri = uri.substring(uri.indexOf("AnswersServlet") + "AnswersServlet".length() + 1);
			System.out.println(uri);
			PrintWriter out = response.getWriter();
User user = (User)(request.getSession().getAttribute("user"));
            user = new User("gilad","123","wizzi",null,null);

			if(user == null)
			{
				out.println("0");
				out.close();
				return;
			}
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context.lookup(DBConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			
			if(uri.equals("PostAnswer"))
			{
				try
				{
					PreparedStatement ps = conn.prepareStatement(DBConstants.INSERT_ANSWER_STMT);			
					String temp = request.getParameter("qid");
					int qid = Integer.parseInt(temp);
					ps.setInt(1, qid);
					ps.setString(2, request.getParameter("answerText"));
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
			else if(uri.equals("GetAnswers"))
			{
				Collection<Answer> answers = new ArrayList<Answer>(); 
				try
				{
					PreparedStatement ps = conn.prepareStatement(DBConstants.SELECT_ANSWERS_BY_QID_STMT);
					System.out.println("qid in get answers "+request.getParameter("qid"));
					String temp = request.getParameter("qid");
					int qid = Integer.parseInt(temp);
					ps.setInt(1, qid);
					ResultSet rs = (ResultSet) ps.executeQuery();
					
					while (rs.next()){
						java.sql.Timestamp ts = java.sql.Timestamp.valueOf(rs.getString(7));
						long tsTime = ts.getTime();
						DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
						java.sql.Date startDate = new java.sql.Date(ts.getTime());
						String createdHuman = df.format(startDate);
						answers.add(new Answer(rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getString(4),rs.getDouble(5),rs.getInt(6),createdHuman,tsTime));
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
				String answersJson = gson.toJson(answers, DBConstants.NEW_QUESTION_COLLECTION);
				System.out.println("answers: " +answersJson);
				out.println(answersJson);
				out.close();
			}
			else if(uri.equals("addVote"))
			{ 
				try
				{
					PreparedStatement ps = conn.prepareStatement(DBConstants.SELECT_ANSWER_BY_AID_STMT);
					String strAid = request.getParameter("aid");
					int aid = Integer.parseInt(strAid);
					ps.setInt(1, aid);
					ResultSet rs = (ResultSet) ps.executeQuery();
					
					User userA = (User)(request.getSession().getAttribute("user"));
					String answerOwner = null;
					int auestoinVotes = 0;
					while (rs.next()){
						answerOwner = rs.getString("OwnerNickname");
						auestoinVotes= rs.getInt("AVotes");				
					}
answerOwner = "bla";
					if (userA.getNickname().equals(answerOwner)){
						out.println("cant vote to your own answer");
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
								
							conn.commit();
						}
						else{
							out.println("already voted to this answer");
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
		}
	
		
		
	}
	

}
