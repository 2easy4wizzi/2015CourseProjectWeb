package models;

/**********************************************************
**                     Topic model                       **
**********************************************************/

public class Topic {
	
	
	private String Topic;
	private double Popularity ;
	
	public Topic(String topic, double popularity){
		Topic = topic;
		Popularity = popularity;
	}
	
	public String getTopic() {
		return Topic;
	}
	public void setTopic(String topic) {
		Topic = topic;
	}
	public double getPopularity() {
		return Popularity;
	}
	public void setPopularity(double popularity) {
		Popularity = popularity;
	}
}
