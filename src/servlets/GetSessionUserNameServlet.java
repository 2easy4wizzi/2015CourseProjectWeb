package servlets;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.User;

/**
 * Servlet implementation class GetSessionUserNameServlet
 */
public class GetSessionUserNameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetSessionUserNameServlet() {
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

		String uri = request.getRequestURI();
		uri = uri.substring(uri.indexOf("GetSessionUserNameServlet") + "GetSessionUserNameServlet".length() + 1);
		//System.out.println("uri is :" + uri);
		
		User user = (User)(request.getSession().getAttribute("user"));
		if(uri.equals("GetUsername"))
		{
			if(user != null){
				response.getWriter().write(user.getNickname());
			}
		}
		else if(uri.equals("RemoveAtt"))
		{	
			
			if(user != null) {
				System.out.println("in remove att:" + user.getUserName() + " " + user.getNickname());
				 request.getSession().removeAttribute("user");
				 response.getWriter().write(user.getNickname());				 
			   }
		}

		
	}

}
