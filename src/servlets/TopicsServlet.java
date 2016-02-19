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
import java.util.Enumeration;
import java.util.Iterator;

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
 * Servlet implementation class CalcPopularTopics
 */
public class TopicsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TopicsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
			
				
				
	}

	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Enumeration<String> params = request.getParameterNames(); while(params.hasMoreElements()){String paramName = (String)params.nextElement();System.out.println("Attribute: "+paramName+", Value: "+request.getParameter(paramName));}
		try
		{
			String uri = request.getRequestURI();
			uri = uri.substring(uri.indexOf(DBConstants.TOPICS_SERVLET_NAME) + DBConstants.TOPICS_SERVLET_NAME.length() + 1);
			System.out.println(uri);
			PrintWriter out = response.getWriter();
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context.lookup(DBConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			if(uri.equals("calcPopularTopics"))
			{
				Collection<Topic>top20mostPopularFrom = new ArrayList<Topic>();
				
				int count = 0;
				int from = 0;
				Gson gson = new Gson();
				try
				{
					
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
/*	System.out.println("final collection: " + outRespone);*/	
					}
					else{
						ps = conn.prepareStatement(DBConstants.SELECT_20_MOST_POPULAR_TOPICS_STMT);
						String strFrom = request.getParameter("from");
						from = Integer.parseInt(strFrom) * 20;
						ps.setInt(1, from);
						
						rs = ps.executeQuery();
						while (rs.next()){
	//System.out.println(rs.getString("QTopics") +"-" +rs.getDouble("Sumup"));
							top20mostPopularFrom.add(new Topic(rs.getString("QTopics") , rs.getDouble("Sumup")));
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
						
	/*System.out.println("final collection: " + outRespone);		*/
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
				//System.out.println("JSON: " +topicsJson);
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

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
