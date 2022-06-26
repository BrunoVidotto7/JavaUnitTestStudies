package br.com.bvidotto.entity;

import java.util.Date;

public class Location {

	private User user;
	private Movie movie;
	private Date locationDate;
	private Date returnDate;
	private Double value;
	
	public User getUsuario() {
		return user;
	}
	public void setUsuario(User user) {
		this.user = user;
	}
	public Date getLocationDate() {
		return locationDate;
	}
	public void setLocationDate(Date locationDate) {
		this.locationDate = locationDate;
	}
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public Movie getFilme() {
		return movie;
	}
	public void setFilme(Movie movie) {
		this.movie = movie;
	}
}