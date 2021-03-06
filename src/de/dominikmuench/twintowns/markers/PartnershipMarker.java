package de.dominikmuench.twintowns.markers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;
import controlP5.CheckBox;
import controlP5.ControlP5;
import controlP5.Range;
import controlP5.Textfield;
import de.dominikmuench.twintowns.MapState;
import de.dominikmuench.twintowns.model.GermanMunicipality;
import de.dominikmuench.twintowns.model.Municipality;
import de.dominikmuench.twintowns.utility.Style;
import de.dominikmuench.twintowns.utility.Utilities;
import de.dominikmuench.twintowns.utility.Utilities.Intersection;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.utils.MapPosition;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public class PartnershipMarker extends AbstractShapeMarker {

	private GermanMunicipality germanMunicipality;
	private List<Municipality> partnerMunicipalities;
	
	private Map<String, Boolean> partnerCountries;
	int numOfPartnerCountries;
	private float averageAngle;

	private float partnerRadius;
	private int highlightColorTransparent;
	private static final int textSpacing = 5;
	private static final String rightArrow = "→";
	private static final String leftArrow = "←";
	private static final String upArrow = "↑";
	private static final String downArrow = "↓";

	public PartnershipMarker(GermanMunicipality germanMunicipality,
			Municipality partnerMunicipality) {
		this(germanMunicipality, new Municipality[] { partnerMunicipality });
	}

	public PartnershipMarker(GermanMunicipality germanMunicipality,
			Municipality[] partnerMunicipalities) {
		this.germanMunicipality = germanMunicipality;
		this.partnerMunicipalities = new ArrayList<Municipality>();
		
		this.partnerCountries = new HashMap<>();

		addLocations(germanMunicipality.getLocation());
		for (Municipality partnerMunicipality : partnerMunicipalities) {
			addLocations(partnerMunicipality.getLocation());
			this.partnerMunicipalities.add(partnerMunicipality);
			if (!partnerCountries.containsKey(partnerMunicipality.getCountry())) {
				numOfPartnerCountries++;
				this.partnerCountries.put(partnerMunicipality.getCountry(), true);
			}
		}
		this.averageAngle = calculateAverageDirection(MapState.getInstance().getMap());

		this.color = new Color(200, 200, 0, 100).getRGB();
		this.highlightColor = new Color(0, 200, 200, 255).getRGB();
		this.highlightColorTransparent = new Color(0, 200, 200, 100).getRGB();
		this.partnerRadius = Style.BASE_MARKER_SIZE;
	}

	public GermanMunicipality getGermanMunicipality() {
		return germanMunicipality;
	}

	public void addPartnerMunicipality(Municipality partnerMunicipality) {
		addLocations(partnerMunicipality.getLocation());
		this.partnerMunicipalities.add(partnerMunicipality);
		if (!partnerCountries.containsKey(partnerMunicipality.getCountry())) {
			numOfPartnerCountries++;
			this.partnerCountries.put(partnerMunicipality.getCountry(), true);
		}
		this.averageAngle = calculateAverageDirection(MapState.getInstance().getMap());
	}

	public boolean fulfillsFilter() {
		ControlP5 cp5 = MapState.getInstance().getCp5();
		boolean filteredByMunicipality = germanMunicipality.getName().contains(
				cp5.get(Textfield.class, "municipalityFilter").getText());
		boolean filteredByState = germanMunicipality.getState().contains(
				cp5.get(Textfield.class, "stateFilter").getText());
		return filteredByMunicipality && filteredByState;
	}
	
	public boolean fulfillsForm(Municipality partnerMunicipality) {
		ControlP5 cp5 = MapState.getInstance().getCp5();
		boolean showPartnership = cp5.get(CheckBox.class, "form").getArrayValue(0) == 1.0f;
		boolean showFriendship = cp5.get(CheckBox.class, "form").getArrayValue(1) == 1.0f;
		boolean showKontakt = cp5.get(CheckBox.class, "form").getArrayValue(2) == 1.0f;
		boolean isPartnership = partnerMunicipality.getForm().equals("p");
		boolean isFriendship = partnerMunicipality.getForm().equals("f");
		boolean isKontakt = partnerMunicipality.getForm().equals("k");
		return (showPartnership && isPartnership) || (showFriendship && isFriendship) || (showKontakt && isKontakt);
	}
	
	public boolean fulfillsYear(Municipality partnerMunicipality) {
		ControlP5 cp5 = MapState.getInstance().getCp5();
		int minYear = (int) cp5.get(Range.class, "yearRange").getArrayValue(0);
		int maxYear = (int) cp5.get(Range.class, "yearRange").getArrayValue(1);
		return partnerMunicipality.getYear() >= minYear && partnerMunicipality.getYear() <= maxYear;
	}
	
	public boolean showNumOfPartners() {
		ControlP5 cp5 = MapState.getInstance().getCp5();
		return cp5.get(CheckBox.class, "numOfPartners").getArrayValue(0) == 1.0;
	}
	
	public float radius(UnfoldingMap map) {
		if (showNumOfPartners()) {
			return (float) (Math.log(map.getZoom()) / Math.log(2) * Math.sqrt(partnerMunicipalities.size() / Math.PI));
		} else {
			return (float) (Math.log(map.getZoom() * Style.BASE_MARKER_SIZE) / Math.log(2));
		}
	}

	@Override
	protected void draw(PGraphics pg, List<MapPosition> mapPositions,
			HashMap<String, Object> properties, UnfoldingMap map) {

		boolean isClicked = MapState.getInstance().getClickedMarker() != null
				&& MapState.getInstance().getClickedMarker().equals(this);

		if (isClicked) {
			this.highlightColor = new Color(0, 200, 200, 255).getRGB();
			this.highlightColorTransparent = new Color(0, 200, 200, 100)
					.getRGB();
		} else {
			this.highlightColor = new Color(127, 127, 127, 255).getRGB();
			this.highlightColorTransparent = new Color(127, 127, 127, 100)
					.getRGB();
		}
		
		pg.textFont(MapState.getInstance().openSans12, 12);
		
		ControlP5 cp5 = MapState.getInstance().getCp5();
		boolean showAverageDirection = cp5.get(CheckBox.class, "averageDirection").getArrayValue(0) == 1.0;
		int minNumOfPartners = (int) cp5.get(Range.class, "numOfPartnersRange").getArrayValue(0);
		int maxNumOfPartners = (int) cp5.get(Range.class, "numOfPartnersRange").getArrayValue(1);
		
		if (showNumOfPartners() && !(this.partnerMunicipalities.size() >= minNumOfPartners && this.partnerMunicipalities.size() <= maxNumOfPartners)) {
			return;
		}

		// German municipality
		ScreenPosition municipalityScreenPos = map
				.getScreenPosition(germanMunicipality.getLocation());
		if (fulfillsFilter()) {
			pg.pushStyle();
			pg.noStroke();
			if (isSelected()) {
				pg.fill(highlightColor);
				pg.textSize(12);
				pg.textAlign(PFont.CENTER, PFont.BOTTOM);
				pg.text(germanMunicipality.getName(), municipalityScreenPos.x,
						municipalityScreenPos.y - radius(map) / 2);
			} else {
				pg.fill(color);
			}
			
			pg.ellipse(municipalityScreenPos.x, municipalityScreenPos.y, radius(map), radius(map));
			
			
			if (showAverageDirection) {	
				int length = 2;
				pg.stroke(new Color(127, 127, 127, 100).getRGB()); // TODO
				PVector directionEnd = new PVector(municipalityScreenPos.x
								+ (float) Math.cos(Math.toRadians(averageAngle))
								* length * (float) (Math.log(map.getZoom()) / Math.log(2)),
						municipalityScreenPos.y
								+ (float) Math.sin(Math.toRadians(averageAngle))
								* length * (float) (Math.log(map.getZoom()) / Math.log(2)));
				pg.line(municipalityScreenPos.x,
						municipalityScreenPos.y,
						directionEnd.x,
						directionEnd.y);
			}
			pg.popStyle();

			// Partner municipalities
			for (Municipality partnerMunicipality : partnerMunicipalities) {
				
				if (!fulfillsForm(partnerMunicipality)) {
					continue;
				}
				
				if (!fulfillsYear(partnerMunicipality)) {
					continue;
				}
				
				ScreenPosition partnerScreenPos = map
						.getScreenPosition(partnerMunicipality.getLocation());

				// Connection
				if (isSelected()) {
					pg.pushStyle();
					pg.noFill();
					pg.strokeWeight(strokeWeight);
					pg.stroke(highlightColorTransparent);
					pg.line(municipalityScreenPos.x, municipalityScreenPos.y,
							partnerScreenPos.x, partnerScreenPos.y);
					pg.popStyle();
				}

				// Partner municipality
				pg.pushStyle();
				pg.strokeWeight(strokeWeight);
				pg.noStroke();
				if (isSelected()) {
					partnerRadius = (float) (Math.log(map.getZoom() * Style.BASE_MARKER_SIZE) / Math.log(2));
					pg.fill(highlightColor);
					pg.ellipse(partnerScreenPos.x, partnerScreenPos.y, partnerRadius,
							partnerRadius);

					if (Utilities.isOnMap(partnerScreenPos)) {
						double angle = Utilities
								.getAngle(PVector
										.sub(new PVector(
												municipalityScreenPos.x,
												municipalityScreenPos.y),
												new PVector(partnerScreenPos.x,
														partnerScreenPos.y)));
						if (angle > 0 && angle <= 180) {
							pg.textAlign(PFont.CENTER, PFont.BOTTOM);
							partnerScreenPos.y -= textSpacing;
						} else {
							pg.textAlign(PFont.CENTER, PFont.TOP);
							partnerScreenPos.y += textSpacing;
						}
						pg.text(partnerMunicipality.getName(),
								partnerScreenPos.x, partnerScreenPos.y);
					} else {
						drawEdgeLabel(pg, municipalityScreenPos,
								partnerMunicipality, partnerScreenPos);
					}
				}
				pg.popStyle();
			}

			// Draw info
			if (isClicked) {
				pg.pushStyle();
				pg.fill(0);
				pg.textFont(MapState.getInstance().openSans18, 18);
				pg.textAlign(PFont.RIGHT, PFont.TOP);
				pg.text(germanMunicipality.getName() + ", "
						+ germanMunicipality.getState(), map.getWidth()
						- 10, 10);
				int numOfPartners = partnerMunicipalities.size();
				pg.textFont(MapState.getInstance().openSans12, 12);
				String partnerText = numOfPartners == 1 ? numOfPartners
						+ " Partnerstadt" : numOfPartners
						+ " Partnerstädte";
				String countriesText = numOfPartnerCountries == 1 ? numOfPartners + " Land" : numOfPartnerCountries + " Ländern";
				pg.text(partnerText + " in " + countriesText, map.getWidth() - 10,
						10 * 2 + 20);
				pg.popStyle();
			}
		}
	}

	private void drawEdgeLabel(PGraphics pg,
			ScreenPosition municipalityScreenPos,
			Municipality partnerMunicipality, ScreenPosition partnerScreenPos) {
		List<Intersection> intersections = Utilities.findMapEdgeIntersections(
				municipalityScreenPos, partnerScreenPos);
		Intersection intersection = Utilities.getClosestIntersection(
				partnerScreenPos, intersections);
		String arrowedName = partnerMunicipality.getName();
		switch (intersection.edge) {
		case TOP:
			pg.textAlign(PFont.CENTER, PFont.TOP);
			arrowedName = upArrow + " " + arrowedName;
			intersection.position.y += textSpacing;
			break;
		case RIGHT:
			pg.textAlign(PFont.RIGHT, PFont.CENTER);
			arrowedName = arrowedName + " " + rightArrow;
			intersection.position.x -= textSpacing;
			break;
		case BOTTOM:
			pg.textAlign(PFont.CENTER, PFont.BOTTOM);
			arrowedName = downArrow + " " + arrowedName;
			intersection.position.y -= textSpacing;
			break;
		case LEFT:
			pg.textAlign(PFont.LEFT, PFont.CENTER);
			arrowedName = leftArrow + " " + arrowedName;
			intersection.position.x += textSpacing;
			break;
		case NONE:
			break;
		default:
			break;
		}
		pg.text(arrowedName, intersection.position.x, intersection.position.y);
	}

	/**
	 * Calculates the "average" direction of the partner municipalities.
	 * 
	 * @param intersections
	 * @return
	 */
	private float calculateAverageDirection(UnfoldingMap map) {
		float sumOfSin = 0;
		float sumOfCos = 0;
		ScreenPosition municipalityScreenPos = map
				.getScreenPosition(germanMunicipality.getLocation());
		for (Municipality partnerMunicipality : partnerMunicipalities) {
			ScreenPosition partnerScreenPos = map
					.getScreenPosition(partnerMunicipality.getLocation());
			double angle = Utilities.getAngle(PVector.sub(new PVector(
					partnerScreenPos.x, partnerScreenPos.y), new PVector(
					municipalityScreenPos.x, municipalityScreenPos.y)));
			sumOfSin += Math.sin(Math.toRadians(angle));
			sumOfCos += Math.cos(Math.toRadians(angle));
		}
		int n = partnerMunicipalities.size();
		return (float) (Math.toDegrees(Math.atan2(sumOfSin / n, sumOfCos / n)) + 360) % 360;
	}

	@Override
	public void draw(PGraphics pg, List<MapPosition> mapPositions) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isInside(UnfoldingMap map, float checkX, float checkY) {
		ScreenPosition position = map.getScreenPosition(germanMunicipality.getLocation());
		PVector pos = new PVector(position.x, position.y);
		return pos.dist(new PVector(checkX, checkY)) <= radius(map);
	}

}
