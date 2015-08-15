package de.dominikmuench.twintowns;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PFont;
import processing.data.TableRow;
import controlP5.CheckBox;
import controlP5.ControlFont;
import controlP5.ControlP5;
import controlP5.Range;
import de.dominikmuench.twintowns.markers.PartnershipMarker;
import de.dominikmuench.twintowns.model.GermanMunicipality;
import de.dominikmuench.twintowns.model.Municipality;
import de.dominikmuench.twintowns.utility.Style;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.MapEvent;
import de.fhpotsdam.unfolding.events.MapEventListener;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.EsriProvider;

@SuppressWarnings("serial")
public class TwinTownsApp extends PApplet implements MapEventListener {

	private TwinTownTable table;
	private UnfoldingMap map;
	private Rectangle controlArea;
	private ControlP5 cp5;
	private Range numOfPartnersRange;
	private CheckBox averageDirectionCheckBox;
	private CheckBox formCheckBox;

	/*
	 * (non-Javadoc)
	 * 
	 * @see processing.core.PApplet#setup()
	 */
	@Override
	public void setup() {
		// General
//		size(800, 600, OPENGL);
		size(displayWidth, displayHeight, JAVA2D);
		smooth();

		// Map
		map = new UnfoldingMap(this, new EsriProvider.WorldGrayCanvas());
		MapState.getInstance().setMap(map);
		map.setTweening(true);
		map.zoomAndPanTo(6, new Location(51.164181f, 10.454150f));
		map.setZoomRange(3, 9);
		
		// Font
		PFont openSans12 = createFont("OpenSans", 12, true);
		PFont openSans18 = createFont("OpenSans", 18, true);
		MapState.getInstance().openSans12 = openSans12;
		MapState.getInstance().openSans18 = openSans18;
		ControlFont font = new ControlFont(openSans12, 12);

		// UI
		controlArea = new Rectangle(0, 0, 210, 200);
		cursor(CROSS);
		cp5 = new ControlP5(this);
		MapState.getInstance().setCp5(cp5);
		cp5.addTextfield("municipalityFilter")
//				.setText("München")
				.setPosition(10, 10)
				.setSize(200, 20)
				.setCaptionLabel("Gemeinde")
				.setFont(font)
				.setColorCaptionLabel(0);
		
		cp5.addTextfield("stateFilter")
//				.setText("Bayern")
				.setPosition(10, 50)
				.setSize(200, 20)
				.setCaptionLabel("Bundesland")
				.setFont(font)
				.setColorCaptionLabel(0);
		
		formCheckBox = cp5.addCheckBox("form")
			.setPosition(10, 95)
			.setColorLabel(0)
			.setSize(10, 10)
			.setItemsPerRow(3)
			.setSpacingColumn(70)
			.addItem("Partnerschaft", 0)
			.addItem("Freundschaft", 1)
			.addItem("Kontakt", 2);
		formCheckBox.activateAll();
		
		cp5.addRange("yearRange")
				.setBroadcast(false)
				.setPosition(10, 120)
				.setSize(200, 10)
				.setHandleSize(10)
				.setRange(1913, 2015)
				.setDecimalPrecision(0)
				.setRangeValues(1913, 2015)
				.setBroadcast(true)
				.setCaptionLabel("seit")
				.setColorCaptionLabel(0);
		
		cp5.addCheckBox("numOfPartners")
				.setPosition(10, 150)
				.setColorLabel(0)
                .setSize(20, 20)
                .setItemsPerRow(1)
                .setSpacingRow(20)
                .addItem("Anzahl Partnerschaften", 0);
		
		numOfPartnersRange = cp5.addRange("numOfPartnersRange")
				.setBroadcast(false) 
				.setPosition(10, 180)
				.setSize(200, 10)
				.setHandleSize(10)
				.setRange(1, 73)
				.setDecimalPrecision(0)
				.setRangeValues(1, 73)
				.setBroadcast(true)	
				.setCaptionLabel("Filter")
				.setColorCaptionLabel(0)
				.hide();
		
		averageDirectionCheckBox = cp5.addCheckBox("averageDirection")
				.setPosition(10, 180)
				.setColorLabel(0)
                .setSize(20, 20)
                .setItemsPerRow(1)
                .setSpacingRow(20)
                .addItem("Durchschnittliche Richtung", 0);

		new RestrictedEventDispatcher(this, map, controlArea);

		// Data & Markers
		table = new TwinTownTable(this);

		List<PartnershipMarker> markers = new ArrayList<>();
		PartnershipMarker lastMarker = null;
		for (TableRow row : table.rows()) {
			if (table.getMunicipality(row).contains("Kreis") || table.getMunicipality(row).contains("Bezirk")) {
				continue;
			}

			GermanMunicipality germanMunicipality = new GermanMunicipality(
					table.getMunicipality(row), new Location(
							table.getLatitude(row), table.getLongitude(row)),
					table.getState(row));

			Municipality partnerMunicipality = new Municipality(
					table.getPartnerMunicipality(row), new Location(
							table.getPartnerLatitude(row),
							table.getPartnerLongitude(row)),
					table.getPartnerCountry(row),
					table.getPartnerContinent(row),
					table.getForm(row),
					table.getYear(row));

			if (lastMarker == null
					|| !lastMarker.getGermanMunicipality().getName()
							.equals(table.getMunicipality(row))) {
				PartnershipMarker connMarker = new PartnershipMarker(
						germanMunicipality, partnerMunicipality);
				markers.add(connMarker);
				lastMarker = connMarker;
			} else {
				lastMarker.addPartnerMunicipality(partnerMunicipality);
			}
		}
		for (PartnershipMarker marker : markers) {
			map.addMarker(marker);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see processing.core.PApplet#draw()
	 */
	@Override
	public void draw() {
		map.draw();
		// Legend
		if (cp5.get(CheckBox.class, "numOfPartners").getArrayValue()[0] == 1.0) {
			pushStyle();
			noStroke();
			fill(Style.YELLOW_LOW);
			float radius = (float) (Math.log(map.getZoom()) / Math.log(2) * Math.sqrt(1 / Math.PI));
			ellipse(width - 70, height - 100, radius, radius);
			radius = (float) (Math.log(map.getZoom()) / Math.log(2) * Math.sqrt(5 / Math.PI));
			ellipse(width - 70, height - 67, radius, radius);
			radius = (float) (Math.log(map.getZoom()) / Math.log(2) * Math.sqrt(50 / Math.PI));
			ellipse(width - 70, height - 30, radius, radius);
			textAlign(CENTER, CENTER);
			fill(0);
			text("1", width - 30, height - 100);
			text("5", width - 30, height - 67);
			text("50", width - 30, height - 30);
			popStyle();
		}
	}

	@Override
	public boolean sketchFullScreen() {
		return true;
	}

	@Override
	public void mouseMoved() {

		for (Marker marker : map.getMarkers()) {
			if (!marker.equals(MapState.getInstance().getClickedMarker())) {
				marker.setSelected(false);
				MapState.getInstance().setSelectedMarker(null);
			}
		}

		Marker hitMarker = map.getFirstHitMarker(mouseX, mouseY);
		if (hitMarker != null) {
			map.getMarkers().remove(hitMarker);
			map.getMarkers().add(hitMarker);
			hitMarker.setSelected(true);
			MapState.getInstance().setSelectedMarker((PartnershipMarker) hitMarker);
		}
	}

	@Override
	public void mouseClicked() {
		
		// Ignore clicks inside control area
		if (controlArea.contains(mouseX, mouseY)) {
			return;
		}
		
		for (Marker marker : map.getMarkers()) {
			marker.setSelected(false);
		}
		PartnershipMarker hitMarker = MapState.getInstance().getSelectedMarker(); 
		if (hitMarker != null) {
			hitMarker.setSelected(true);
			MapState.getInstance().setClickedMarker(hitMarker);
		} else if (map.getFirstHitMarker(mouseX, mouseY) == null) {
			MapState.getInstance().setClickedMarker(null);
		}
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onManipulation(MapEvent event) {
		// TODO Auto-generated method stub
		System.err.println(event.toString());
	}
	
	public void numOfPartners(float[] a) {
		if (a[0] == 1.0) {
			numOfPartnersRange.show();
			averageDirectionCheckBox.setPosition(10, averageDirectionCheckBox.getPosition().y + 25);
			controlArea.y += 25;
		} else {
			numOfPartnersRange.hide();
			averageDirectionCheckBox.setPosition(10, averageDirectionCheckBox.getPosition().y - 25);
			controlArea.y -= 25;
		}
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "de.dominikmuench.twintowns.TwinTownsApp" });
	}

}
