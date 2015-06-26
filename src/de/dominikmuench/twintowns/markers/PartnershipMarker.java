package de.dominikmuench.twintowns.markers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapPosition;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public class PartnershipMarker extends AbstractShapeMarker {

	private String municipality;

	private Location from;
	private List<Location> to = new ArrayList<Location>();
	private int radius;

	public PartnershipMarker(String municipality, Marker fromMarker, Marker toMarker) {
		addLocations(fromMarker.getLocation());
		addLocations(toMarker.getLocation());
		this.to.add(toMarker.getLocation());
		this.municipality = municipality;
		this.from = fromMarker.getLocation();
		this.color = new Color(200, 200, 0, 100).getRGB();
		this.highlightColor = new Color(0, 200, 200, 100).getRGB();
		this.radius = 5;
	}

	public PartnershipMarker(String municipality, Marker fromMarker, Marker[] toMarkers) {
		addLocations(fromMarker.getLocation());
		for (Marker toMarker : toMarkers) {
			addLocations(toMarker.getLocation());
			this.to.add(toMarker.getLocation());
		}
		this.municipality = municipality;
		this.from = fromMarker.getLocation();
		this.color = new Color(200, 200, 0, 100).getRGB();
		this.highlightColor = new Color(0, 200, 200, 100).getRGB();
		this.radius = 5;
	}

	public String getMunicipality() {
		return municipality;
	}

	public void setMunicipality(String municipality) {
		this.municipality = municipality;
	}

	public void addToMarker(Marker toMarker) {
		System.out.println("Adding Location to PartnershipMarker '" + this.municipality + ": " + toMarker.getLocation());
		addLocations(toMarker.getLocation());
		this.to.add(toMarker.getLocation());
	}

	@Override
	public void draw(PGraphics pg, List<MapPosition> mapPositions) {
		MapPosition from = mapPositions.get(0);
		List<MapPosition> toPositions = new ArrayList<>(mapPositions);
		if (!toPositions.isEmpty()) {
			toPositions.remove(0);
		}

		// municipality
		pg.pushStyle();
		pg.strokeWeight(strokeWeight);
		pg.noStroke();
		if (isSelected()) {
			pg.fill(highlightColor);
			pg.textSize(12);
			pg.textAlign(PFont.CENTER, PFont.BOTTOM);
			pg.text(municipality, from.x, from.y);
		} else {
			pg.fill(color);
		}
		pg.ellipse((int) from.x, (int) from.y, radius, radius); // TODO use radius in km and convert to px
		pg.popStyle();

		for (MapPosition toPosition : toPositions) {
			// connection
			if (isSelected()) {
				pg.pushStyle();
				pg.strokeWeight(2);
				pg.stroke(0, 200, 200, 100);
				pg.line(from.x, from.y, toPosition.x, toPosition.y);
				pg.popStyle();
			}

			// partner municipality
			pg.pushStyle();
			pg.strokeWeight(strokeWeight);
			pg.noStroke();
			if (isSelected()) {
				pg.fill(highlightColor);
			} else {
				pg.fill(color, 0);
			}
			pg.ellipse(toPosition.x, toPosition.y, radius, radius); // TODO use radius in km and convert to px
			pg.popStyle();
		}
	}

	@Override
	public boolean isInside(UnfoldingMap map, float checkX, float checkY) {
		ScreenPosition position = map.getScreenPosition(from);
		PVector pos = new PVector(position.x, position.y);
		return pos.dist(new PVector(checkX, checkY)) < radius;
	}


}
