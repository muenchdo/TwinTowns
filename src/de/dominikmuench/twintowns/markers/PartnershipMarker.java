package de.dominikmuench.twintowns.markers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;
import controlP5.ControlP5;
import controlP5.Textfield;
import de.dominikmuench.twintowns.MapState;
import de.dominikmuench.twintowns.model.GermanMunicipality;
import de.dominikmuench.twintowns.model.Municipality;
import de.dominikmuench.twintowns.utility.Utilities;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.utils.MapPosition;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public class PartnershipMarker extends AbstractShapeMarker {

	private GermanMunicipality germanMunicipality;
	private List<Municipality> partnerMunicipalities;

	private int radius;
	private int highlightColorTransparent;

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
		this.highlightColor = new Color(0, 200, 200, 255).getRGB();
		this.highlightColorTransparent = new Color(0, 200, 200, 100).getRGB();
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
	protected void draw(PGraphics pg, List<MapPosition> mapPositions,
			HashMap<String, Object> properties, UnfoldingMap map) {

		// German municipality
		ScreenPosition municipalityScreenPos = map.getScreenPosition(germanMunicipality.getLocation());
		if (fulfillsFilter()) {
			pg.pushStyle();
			pg.noStroke();
			if (isSelected()) {
				pg.fill(highlightColor);
				pg.textSize(12);
				pg.textAlign(PFont.CENTER, PFont.BOTTOM);
				pg.text(germanMunicipality.getName(), municipalityScreenPos.x, municipalityScreenPos.y);
			} else {
				pg.fill(color);
			}
			pg.ellipse(municipalityScreenPos.x, municipalityScreenPos.y, radius, radius);
			pg.popStyle();

			// Partner municipalities
			for (Municipality partnerMunicipality : partnerMunicipalities) {
				ScreenPosition partnerScreenPos = map.getScreenPosition(partnerMunicipality.getLocation());

				// Connection
				if (isSelected()) {
					pg.pushStyle();
					pg.strokeWeight(2);
					pg.stroke(highlightColorTransparent);
					pg.line(municipalityScreenPos.x, municipalityScreenPos.y, partnerScreenPos.x, partnerScreenPos.y);
					pg.popStyle();
				}

				// Partner municipality
				pg.pushStyle();
				pg.strokeWeight(strokeWeight);
				pg.noStroke();
				if (isSelected()) {
					pg.fill(highlightColor);
					pg.ellipse(partnerScreenPos.x, partnerScreenPos.y, radius, radius);
					PVector intersection = Utilities.findMapEdgeIntersection(municipalityScreenPos, partnerScreenPos);
					pg.textAlign(PFont.CENTER, PFont.TOP);
					pg.text(partnerMunicipality.getName(), intersection.x, intersection.y);
				}
				pg.popStyle();
			}
		}
	}

	@Override
	public void draw(PGraphics pg, List<MapPosition> mapPositions) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isInside(UnfoldingMap map, float checkX, float checkY) {
		ScreenPosition position = map.getScreenPosition(germanMunicipality.getLocation());
		PVector pos = new PVector(position.x, position.y);
		return pos.dist(new PVector(checkX, checkY)) < radius;
	}


}
