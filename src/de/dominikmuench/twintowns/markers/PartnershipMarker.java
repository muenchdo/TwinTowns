package de.dominikmuench.twintowns.markers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;
import controlP5.ControlP5;
import controlP5.Textfield;
import de.dominikmuench.twintowns.MapState;
import de.dominikmuench.twintowns.model.GermanMunicipality;
import de.dominikmuench.twintowns.model.Municipality;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.utils.MapPosition;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public class PartnershipMarker extends AbstractShapeMarker {

	private GermanMunicipality germanMunicipality;
	private List<Municipality> partnerMunicipalities;

	private int radius;
	
	public PartnershipMarker(GermanMunicipality germanMunicipality, Municipality partnerMunicipality) {
		this(germanMunicipality, new Municipality[]{partnerMunicipality});
	}

	public PartnershipMarker(GermanMunicipality germanMunicipality, Municipality[] partnerMunicipalities) {
		this.germanMunicipality = germanMunicipality;
		this.partnerMunicipalities = new ArrayList<Municipality>();
		
		addLocations(germanMunicipality.getLocation());
		for (Municipality partnerMunicipality : partnerMunicipalities) {
			addLocations(partnerMunicipality.getLocation());
			this.partnerMunicipalities.add(partnerMunicipality);
		}
		
		this.color = new Color(200, 200, 0, 100).getRGB();
		this.highlightColor = new Color(0, 200, 200, 100).getRGB();
		this.radius = 5;
	}

	public GermanMunicipality getGermanMunicipality() {
		return germanMunicipality;
	}

	public void addPartnerMunicipality(Municipality partnerMunicipality) {
		addLocations(partnerMunicipality.getLocation());
		this.partnerMunicipalities.add(partnerMunicipality);
	}
	
	public boolean fulfillsFilter() {
		ControlP5 cp5 = MapState.getInstance().getCp5();
		boolean filteredByMunicipality = germanMunicipality.getName().contains(cp5.get(Textfield.class, "municipalityFilter").getText());
		boolean filteredByState = germanMunicipality.getState().contains(cp5.get(Textfield.class, "stateFilter").getText());
		return filteredByMunicipality && filteredByState;
	}

	@Override
	public void draw(PGraphics pg, List<MapPosition> mapPositions) {
		MapPosition from = mapPositions.get(0);
		List<MapPosition> toPositions = new ArrayList<>(mapPositions);
		if (!toPositions.isEmpty()) {
			toPositions.remove(0);
		}

		// municipality
		if (fulfillsFilter()) {
			pg.pushStyle();
			pg.strokeWeight(strokeWeight);
			pg.noStroke();
			if (isSelected()) {
				pg.fill(highlightColor);
				pg.textSize(12);
				pg.textAlign(PFont.CENTER, PFont.BOTTOM);
				pg.text(germanMunicipality.getName(), from.x, from.y);
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
	}

	@Override
	public boolean isInside(UnfoldingMap map, float checkX, float checkY) {
		ScreenPosition position = map.getScreenPosition(germanMunicipality.getLocation());
		PVector pos = new PVector(position.x, position.y);
		return pos.dist(new PVector(checkX, checkY)) < radius;
	}


}
