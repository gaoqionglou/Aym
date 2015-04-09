package com.gao.aym.bean;

public class TopicInfo {
	private int id;
	private String title;
	private String imageUri;
	private String introduction;
	private int num;

	public TopicInfo(int id, String title, String imageUri,
			String introduction, int num) {
		this.id = id;
		this.title = title;
		this.imageUri = imageUri;   
		this.introduction = introduction;
		this.num = num;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageUri() {
		return imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	@Override
	public String toString() {
		return "TopicInfo [id=" + id + ", title=" + title + ", imageUri="
				+ imageUri + ", introduction=" + introduction + ", num=" + num
				+ "]";
	}
}
