package com.project.bluepandora.blooddonation.data;

public class DistrictItem implements Item {

	private int distId;
	private String distName;

	public DistrictItem() {
		// TODO Auto-generated constructor stub
	}

	public DistrictItem(int distId, String distName) {
		this.distId = distId;
		this.distName = distName;
	}

	public int getDistId() {
		return distId;
	}

	public void setDistId(int distId) {
		this.distId = distId;
	}

	public String getDistName() {
		return distName;
	}

	public void setDistName(String distName) {
		this.distName = distName;
	}

	public String toString() {
		return distName;
	}
}
