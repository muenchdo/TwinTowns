package de.dominikmuench.twintowns.markers;

import java.awt.Color;

import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;

public class MunicipalityMarker extends SimplePointMarker {

	public MunicipalityMarker(Location location) {
		super(location);
		this.color = new Color(200, 200, 0, 100).getRGB();
		this.highlightColor = new Color(0, 200, 200, 100).getRGB();
		this.radius = 5;
	}

	@Override
	public void draw(PGraphics pg, float x, float y) {
		if (isHidden())
			return;

		pg.pushStyle();
		pg.strokeWeight(strokeWeight);
		pg.noStroke();
		if (isSelected()) {
			pg.fill(highlightColor);
		} else {
			pg.fill(color);
		}
		pg.ellipse((int) x, (int) y, radius, radius); // TODO use radius in km and convert to px
		pg.popStyle();
	}

}
