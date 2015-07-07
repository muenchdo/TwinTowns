package de.dominikmuench.twintowns.model;

import de.fhpotsdam.unfolding.geo.Location;

public class Municipality {
	
	protected String name;
	protected Location location;
	protected String country;
	protected String continent;
	
	public Municipality(String name, Location location, String country, String continent) {
		this.name = name;
		this.location = location;
		this.country = country;
		this.continent = continent;
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
	
}
