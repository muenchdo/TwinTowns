package de.dominikmuench.twintowns.markers;

import java.util.List;

import processing.core.PGraphics;
import de.fhpotsdam.unfolding.examples.marker.connectionmarker.ConnectionMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapPosition;

public class PartnershipMarker extends ConnectionMarker {
	
	public PartnershipMarker(Marker fromMarker, Marker toMarker) {
		super(fromMarker, toMarker);
	}

	@Override
	public void draw(PGraphics pg, List<MapPosition> mapPositions) {
		MapPosition from = mapPositions.get(0);
		MapPosition to = mapPositions.get(1);
		pg.pushStyle();
		pg.strokeWeight(2);
		pg.stroke(200, 200, 0, 100);
		pg.line(from.x, from.y, to.x, to.y);
		pg.popStyle();
	}
	
	

}
