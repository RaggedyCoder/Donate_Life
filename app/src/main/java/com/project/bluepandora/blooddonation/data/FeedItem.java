package com.project.bluepandora.blooddonation.data;

public class FeedItem implements Item {
	private int id, bloodAmount;
	// private String name, status, image, profilePic, timeStamp, url;
	private String name, timeStamp, emergency, bloodGroup, hospital, area,
			contact;

	public FeedItem() {
	}

	public FeedItem(int id, int bloodAmount, String name, String timeStamp,
			String emergency, String bloodGroup, String hospital, String area,
			String contact) {
		super();
		this.id = id;
		this.bloodAmount = bloodAmount;
		this.name = name;
		this.timeStamp = timeStamp;
		this.emergency = emergency;
		this.bloodGroup = bloodGroup;
		this.hospital = hospital;
		this.area = area;
		this.contact = contact;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getEmergency() {
		return emergency;
	}

	public void setEmergency(String emergency) {
		this.emergency = emergency;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public int getBloodAmount() {
		return bloodAmount;
	}

	public void setBloodAmount(int bloodAmount) {
		this.bloodAmount = bloodAmount;
	}

}
