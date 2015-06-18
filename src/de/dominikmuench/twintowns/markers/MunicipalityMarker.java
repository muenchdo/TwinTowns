package de.dominikmuench.twintowns.markers;

import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;

public class MunicipalityMarker extends SimplePointMarker {
	
	public MunicipalityMarker(Location location) {
		super(location);
	}

	@Override
	public void draw(PGraphics pg, float x, float y) {
		pg.pushStyle();
	    pg.noStroke();
	    pg.fill(200, 200, 0, 100);
	    pg.ellipse(x, y, 5, 5);
	    pg.popStyle();
	}

}
