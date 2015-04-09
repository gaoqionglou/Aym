package com.gao.aym.bean;

public class LetterContact implements Comparable<LetterContact>{
	private String sortletter;
	private String name;
    private int isFirstShow;
	
    /**  
     *  isFirstShow =0
     * @param sortletter
     * @param name
     */
    public LetterContact(String sortletter, String name) {
		this.sortletter = sortletter;
		this.name = name;
		this.isFirstShow = 0;
	}

	public String getSortletter() {
		return sortletter;
	}

	public void setSortletter(String sortletter) {
		this.sortletter = sortletter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
   
	


	public int getIsFirstShow() {
		return isFirstShow;
	}

	public void setIsFirstShow(int isFirstShow) {
		this.isFirstShow = isFirstShow;
	}

	@Override
	public String toString() {
		return "LetterContact [sortletter=" + sortletter + ", name=" + name
				+ ", isFirstShow=" + isFirstShow + "]";
	}

	public int compareTo(LetterContact another) {
		// TODO Auto-generated method stub
		
		return getSortletter().compareTo(another.getSortletter());
	}

	

	

}
