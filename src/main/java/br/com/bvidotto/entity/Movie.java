package br.com.bvidotto.entity;

public class Movie {

	private String name;
	private Integer inventory;
	private Double locationPrice;
	
	public Movie() {}
	
	public Movie(String name, Integer inventory, Double locationPrice) {
		this.name = name;
		this.inventory = inventory;
		this.locationPrice = locationPrice;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getInventory() {
		return inventory;
	}
	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}
	public Double getLocationPrice() {
		return locationPrice;
	}
	public void setLocationPrice(Double locationPrice) {
		this.locationPrice = locationPrice;
	}
}