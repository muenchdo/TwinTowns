package de.dominikmuench.twintowns.model;

import de.fhpotsdam.unfolding.geo.Location;

public class GermanMunicipality extends Municipality {
	
	private String state;
	
	public GermanMunicipality(String name, Location location, String state) {
		super(name, location, "Germany", "Europe");
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
