package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import constants.DBConstants;
import models.Question;
import models.Answer;
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
		Enumeration<String> params = request.getParameterNames(); 
		while(params.hasMoreElements())
		{
		 String paramName = (String)params.nextElement();
		 System.out.println("Attribute: "+paramName+", Value: "+request.getParameter(paramName));
		}
		try
		{
			String uri = request.getRequestURI();
			uri = uri.substring(uri.indexOf("QuestionsServlet") + "QuestionsServlet".length() + 1);
			System.out.println(uri);
			PrintWriter out = response.getWriter();
			User user = (User)(request.getSession().getAttribute("user"));
//User user = new User("gilad","123","wizzi",null,null);

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
			else if(uri.equals("GetNewTop20"))
			{
				Collection<Question> top20new = new ArrayList<Question>(); 
				try
				{
					PreparedStatement ps = conn.prepareStatement(DBConstants.SELECT_TOP_20_QUESTION_BY_TIMESTAMP_STMT);			
					ResultSet rs = (ResultSet) ps.executeQuery();
							
					while (rs.next()){
						top20new.add(new Question(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getDouble(5),rs.getInt(6),rs.getString(7),rs.getInt(8)));
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
				String top20newJson = gson.toJson(top20new, DBConstants.NEW_QUESTION_COLLECTION);
				System.out.println("JSON: " +top20newJson);
				out.println(top20newJson);
	        	out.close();
			}
			else if(uri.equals("GetAnswers"))
			{
				Collection<Answer> answers = new ArrayList<Answer>(); 
				try
				{
					PreparedStatement ps = conn.prepareStatement(DBConstants.SELECT_ANSWERS_BY_QID_STMT);
					String temp = request.getParameter("qid");
					int qid = Integer.parseInt(temp);
					ps.setInt(1, qid);
					ResultSet rs = (ResultSet) ps.executeQuery();
					
					while (rs.next()) {
					    for (int i = 1; i <= 6; i++) {
					        if (i > 1) System.out.print(" | ");
					        System.out.print(rs.getString(i));
					    }
					    System.out.println("");
					}
					
					while (rs.next()){
						answers.add(new Answer(rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getString(4),rs.getDouble(5),rs.getInt(6),rs.getString(7)));
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
				System.out.println("JSON: " +answersJson);
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
		}
	
		
		
	}

}
