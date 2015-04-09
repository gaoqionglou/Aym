package com.gao.aym.bean;

public class ActivityInfo {
	private int id;
	private String title;
	private String imageUri;
	private String startTime;
	private String endTime;
	private int state; // 1-进行中， 0-已下线

	public ActivityInfo(){
		
	}
	public ActivityInfo(int id, String title, String imageUri,String startTime, String endTime, int state) {
		super();
		this.id = id;
		this.title = title;
		this.imageUri = imageUri;
		this.startTime = startTime;
		this.endTime = endTime;
		this.state = state;
	}

	public String getImageUri() {
		return imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	@Override
	public String toString() {
		return "ActivityInfo [id=" + id + ", title=" + title + ", imageUri="
				+ imageUri + ", startTime=" + startTime + ", endTime="
				+ endTime + ", state=" + state + "]";
	}

}
