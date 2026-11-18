package model;

import java.util.Date;

public class CoffeeLot {
	private int id;
	private String name;
	private String variety;
	private double area;
	private Date plantingDate;
	private boolean irrigated;
	private User user;
	
	public CoffeeLot() {
		this(0);
	}
	
	public CoffeeLot(int id) {
		this.id = id;
		setName("");
		setVariety("");
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVariety() {
		return variety;
	}

	public void setVariety(String variety) {
		this.variety = variety;
	}

	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
	}

	public Date getPlantingDate() {
		return plantingDate;
	}

	public void setPlantingDate(Date plantingDate) {
		this.plantingDate = plantingDate;
	}

	public boolean isIrrigated() {
		return irrigated;
	}

	public void setIrrigated(boolean irrigated) {
		this.irrigated = irrigated;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
