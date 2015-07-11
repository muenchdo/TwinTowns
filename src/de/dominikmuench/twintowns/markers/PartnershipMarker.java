package de.dominikmuench.twintowns.markers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private static final int textSpacing = 5;
	private static final String rightArrow = "→";
	private static final String leftArrow = "←";
	private static final String upArrow = "↑";
	private static final String downArrow = "↓";

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
		
		if (MapState.getInstance().getSelectedMarker() != null && MapState.getInstance().getSelectedMarker().equals(this)) {
			this.highlightColor = new Color(0, 200, 200, 255).getRGB();
			this.highlightColorTransparent = new Color(0, 200, 200, 100).getRGB();
		} else {
			this.highlightColor = new Color(100, 100, 100, 255).getRGB();
			this.highlightColorTransparent = new Color(100, 100, 100, 100).getRGB();
		}

		// German municipality
		ScreenPosition municipalityScreenPos = map.getScreenPosition(germanMunicipality.getLocation());
		if (fulfillsFilter()) {
			pg.pushStyle();
			pg.noStroke();
			if (isSelected()) {
				pg.fill(highlightColor);
				pg.textSize(12);
				pg.textAlign(PFont.CENTER, PFont.BOTTOM);
				pg.text(germanMunicipality.getName(), municipalityScreenPos.x, municipalityScreenPos.y - textSpacing);
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
					Map<String, Object> intersectionInfo = Utilities.findMapEdgeIntersection(municipalityScreenPos, partnerScreenPos);
					PVector intersection = (PVector) intersectionInfo.get(Utilities.KEY_INTERSECTION);
					String edge = (String) intersectionInfo.get(Utilities.KEY_EDGE);
					String arrowedName = partnerMunicipality.getName();
					switch (edge) {
					case Utilities.TOP_EDGE:
						pg.textAlign(PFont.CENTER, PFont.TOP);
						arrowedName = upArrow + " " + arrowedName;
						intersection.y += textSpacing;
						break;
					case Utilities.RIGHT_EDGE:
						pg.textAlign(PFont.RIGHT, PFont.CENTER);
						arrowedName = arrowedName + " " + rightArrow;
						intersection.x -= textSpacing;
						break;
					case Utilities.BOTTOM_EDGE:
						pg.textAlign(PFont.CENTER, PFont.BOTTOM);
						arrowedName = downArrow + " " + arrowedName;
						intersection.y -= textSpacing;
						break;
					case Utilities.LEFT_EDGE:
						pg.textAlign(PFont.LEFT, PFont.CENTER);
						arrowedName = leftArrow + " " + arrowedName;
						intersection.x += textSpacing;
						break;
					case Utilities.NO_EDGE:
						double angle = Utilities.getAngle(PVector.sub(
								new PVector(municipalityScreenPos.x, municipalityScreenPos.y),
								new PVector(partnerScreenPos.x, partnerScreenPos.y)));
						if (angle > 0 && angle <= 180) {
							pg.textAlign(PFont.CENTER, PFont.BOTTOM);
							intersection.y -= textSpacing;
						} else {
							pg.textAlign(PFont.CENTER, PFont.TOP);
							intersection.y += textSpacing;
						}
						break;
					default:
						break;
					}
					pg.text(arrowedName, intersection.x, intersection.y);
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
