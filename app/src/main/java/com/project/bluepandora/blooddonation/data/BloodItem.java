package com.project.bluepandora.blooddonation.data;

public class BloodItem implements Item {

	private int bloodId;
	private String bloodName;
	public int getBloodId() {
		return bloodId;
	}
	public void setBloodId(int bloodId) {
		this.bloodId = bloodId;
	}
	public String getBloodName() {
		return bloodName;
	}
	public void setBloodName(String bloodName) {
		this.bloodName = bloodName;
	}
}
