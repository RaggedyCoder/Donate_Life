package com.project.bluepandora.blooddonation.data;

public class UserInfoItem implements Item {

	private String keyWord;
	private int distId;
	private int groupId;
	private String mobileNumber;
	private String firstName;
	private String lastName;

	public UserInfoItem() {
		// TODO Auto-generated constructor stub
	}

	public UserInfoItem(String firstName, String lastName, String keyWord,
			String mobileNumber, int distId, int groupId) {
		// TODO Auto-generated constructor stub
		this.firstName = firstName;
		this.lastName = lastName;
		this.keyWord = keyWord;
		this.mobileNumber = mobileNumber;
		this.distId = distId;
		this.groupId = groupId;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public int getDistId() {
		return distId;
	}

	public void setDistId(int distId) {
		this.distId = distId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
