package com.project.bluepandora.blooddonation.data;

public class SlideItem implements Item {
	private String slideItem;
	private int icons;
	private int background;
	public SlideItem() {
		// TODO Auto-generated constructor stub
	}

	public SlideItem(String slideItem) {
		// TODO Auto-generated constructor stub
		this.slideItem = slideItem;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public String getSlideItem() {
		return slideItem;
	}

	public void setSlideItem(String slideItem) {
		this.slideItem = slideItem;
	}

	public int getIcons() {
		return icons;
	}

	public void setIcons(int icons) {
		this.icons = icons;
	}

	public int getBackground() {
		return background;
	}

	public void setBackground(int background) {
		this.background = background;
	}

}
