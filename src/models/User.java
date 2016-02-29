package models;


import java.util.ArrayList;
import java.util.Collection;

public class User
{
	private String Username, Password, Nickname, Description, PhotoUrl;
	private double UserRating ;
	private Collection<String> Topics;
	public User(String username, String password, String nickname,String description, String photoUrl, double userRating, Collection<String> topics) 
	{
		Username = username;
		Password = password;
		Nickname = nickname;
		Description = description;
		PhotoUrl = photoUrl;
		UserRating = userRating;
		if ((topics!= null) && (!topics.isEmpty())){
			Topics=new ArrayList<String>(topics);
		}
		else{
			Topics=null;
		}
	}
	public String getUserName() 
	{
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getPassword() 
	{
		return Password;
	}

	public String getNickname() 
	{
		return Nickname;
	}
	
	public String getDescription() 
	{
		return Description;
	}
	
	public String getPhotoUrl() 
	{
		return PhotoUrl;
	}
	
	public double getUserRating() 
	{
		return UserRating;
	}
	
	public void print() 
	{
		System.out.println("User Details: " + Username + " " + Password + " " + Nickname + " " + Description + " " + PhotoUrl + " " + UserRating);
	}
	
	
	
}
