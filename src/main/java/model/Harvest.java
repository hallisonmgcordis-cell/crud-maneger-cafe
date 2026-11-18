package model;

import java.util.Date;

public class Harvest {
	private int id;
	private CoffeeLot coffeeLot;
	private Date harvestDate;
	private int bags;
	private String quality;
	private String notes;
	private User user;
	
	public Harvest() {
		this(0);
	}
	
	public Harvest(int id) {
		this.id = id;
		setQuality("");
		setNotes("");
	}

	public int getId() {
		return id;
	}

	public CoffeeLot getCoffeeLot() {
		return coffeeLot;
	}

	public void setCoffeeLot(CoffeeLot coffeeLot) {
		this.coffeeLot = coffeeLot;
	}

	public Date getHarvestDate() {
		return harvestDate;
	}

	public void setHarvestDate(Date harvestDate) {
		this.harvestDate = harvestDate;
	}

	public int getBags() {
		return bags;
	}

	public void setBags(int bags) {
		this.bags = bags;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
