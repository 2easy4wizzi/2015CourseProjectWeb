package models;

public class User
{
	private String Username, Password, Nickname, Description, PhotoUrl;
	private double UserRating ;
	public User(String username, String password, String nickname,String description, String photoUrl) 
	{
		Username = username;
		Password = password;
		Nickname = nickname;
		Description = description;
		PhotoUrl = photoUrl;
		UserRating = 0;
	}

	public String getUserName() 
	{
		return Username;
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
