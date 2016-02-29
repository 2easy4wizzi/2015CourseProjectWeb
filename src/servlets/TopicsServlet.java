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
import models.Topic;

/**
 * Servlet implementation class CalcPopularTopics. It deals with requests if topics
 * @author Gilad Eini
 * @author Ilana Veitzblit
 */
public class TopicsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TopicsServlet() {
        super();
        
    }
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    /**
     * doGet is used to get info from the servlet. depending on the js function that calls doGet, different info will be fetched.
     * @param from - offset to get 20 questions from it.
     * @param qid - the question id 
	 * @return array of 20 most popular questions from an offset + boolean that notify if the next button will stay on / array of all topic belong to the question
     * @throws SQLException
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try
		{
			String uri = request.getRequestURI();
			uri = uri.substring(uri.indexOf(DBConstants.TOPICS_SERVLET_NAME) + DBConstants.TOPICS_SERVLET_NAME.length() + 1);
			PrintWriter out = response.getWriter();
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context.lookup(DBConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			/*
			 * this segment gets the top 20 MOST popular topics from an offset. in addtion, it also see if the "next button" should apear in the html.
			 */
			if(uri.equals("calcPopularTopics"))
			{
				Collection<Topic>top20mostPopularFrom = new ArrayList<Topic>();
				
				int count = 0;
				int from = 0;
				Gson gson = new Gson();
				try
				{	//get number of topics				
					PreparedStatement ps = conn.prepareStatement(DBConstants.SELECT_COUNT_TOPICS_STMT);
					ResultSet rs = ps.executeQuery();
					while (rs.next()){
						  count = rs.getInt(1);
					}
					if(count == 0){
						String boolJson = gson.toJson(true, boolean.class);
						String strJson = gson.toJson("noTopicsFound", String.class);
						String outRespone = "[" + boolJson + "," + strJson + "]";
						out.println(outRespone);
					}
					else{
						//get 20 most popular topics
						ps = conn.prepareStatement(DBConstants.SELECT_20_MOST_POPULAR_TOPICS_STMT);
						String strFrom = request.getParameter("from");
						from = Integer.parseInt(strFrom) * 20;
						ps.setInt(1, from);
						
						rs = ps.executeQuery();
						while (rs.next()){
							//formating
							double popularity = rs.getDouble("Sumup");
							DecimalFormat dfRating = new DecimalFormat("#.##");
							String dxRating=dfRating.format(popularity);
							popularity = Double.valueOf(dxRating);
							
							top20mostPopularFrom.add(new Topic(rs.getString("QTopics") , popularity));
						}
						String boolJson;
						if(count <= from+20 ){
							boolJson = gson.toJson(true, boolean.class);	
						}
						else{
							boolJson = gson.toJson(false, boolean.class);	
						}		
						String top20mostPopularFromJSON = gson.toJson(top20mostPopularFrom, DBConstants.NEW_TOPIC_COLLECTION);
						String outRespone = "[" + boolJson + "," + top20mostPopularFromJSON + "]";					
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
			/*
			 * this segment fetch all the topics of a question to be presented in the question view.
			 */
			else if(uri.equals("GetTopics"))
			{
				Collection<String> topics = new ArrayList<String>();
				Gson gson = new Gson();
				try
				{
					
					PreparedStatement ps = conn.prepareStatement(DBConstants.SELECT_TOPICS_BY_QID_STMT);
					String qidStr = request.getParameter("qid");
					int qid = Integer.parseInt(qidStr);
					ps.setInt(1, qid);
					ResultSet rs =  ps.executeQuery();
					
					while (rs.next()){
						topics.add(new String(rs.getString(2)));
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
				String topicsJson = gson.toJson(topics, DBConstants.NEW_TOPICS_COLLECTION);
				out.println(topicsJson);
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
