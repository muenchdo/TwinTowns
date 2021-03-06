package de.dominikmuench.twintowns.model;

import de.fhpotsdam.unfolding.geo.Location;

public class Municipality {
	
	protected String name;
	protected Location location;
	protected String country;
	protected String continent;
	protected String form;
	protected Integer year;
	
	public Municipality(String name, Location location, String country, String continent, String form, Integer year) {
		this.name = name;
		this.location = location;
		this.country = country;
		this.continent = continent;
		this.form = form;
		this.year = year;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getContinent() {
		return continent;
	}
	
	public void setContinent(String continent) {
		this.continent = continent;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}
	
}
