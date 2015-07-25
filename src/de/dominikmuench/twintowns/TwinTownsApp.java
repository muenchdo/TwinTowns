package de.dominikmuench.twintowns;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.data.TableRow;
import controlP5.ControlP5;
import de.dominikmuench.twintowns.markers.PartnershipMarker;
import de.dominikmuench.twintowns.model.GermanMunicipality;
import de.dominikmuench.twintowns.model.Municipality;
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
	private ControlP5 cp5;

	/*
	 * (non-Javadoc)
	 * 
	 * @see processing.core.PApplet#setup()
	 */
	@Override
	public void setup() {
		// General
		size(800, 600, P2D);
		// size(displayWidth, displayHeight, P2D);
		smooth();

		// Map
		map = new UnfoldingMap(this, new EsriProvider.WorldGrayCanvas());
		MapState.getInstance().setMap(map);
		map.setTweening(true);
		map.zoomAndPanTo(6, new Location(51.164181f, 10.454150f));
		map.setZoomRange(3, 9);
		
		// Font
		PFont pFont = createFont("OpenSans", 12, true);
		ControlFont font = new ControlFont(pFont, 12);

		// UI
		cp5 = new ControlP5(this);
		MapState.getInstance().setCp5(cp5);
		cp5.addTextfield("municipalityFilter")
				.setText("München")
				.setPosition(10, 10)
				.setSize(200, 20)
				.setCaptionLabel("City")
				.setFont(font)
				.setColorCaptionLabel(0);
		
		cp5.addTextfield("stateFilter")
				.setText("Bavaria")
				.setPosition(10, 50)
				.setSize(200, 20)
				.setCaptionLabel("State")
				.setFont(font)
				.setColorCaptionLabel(0);
		
		cp5.addCheckBox("numOfPartners")
				.setPosition(10, 90)
				.setColorLabel(0)
                .setSize(20, 20)
                .setItemsPerRow(1)
                .setSpacingRow(20)
                .addItem("Number of partnerships", 0);
		
		cp5.addRange("numOfPartnersRange")
				.setVisible(false);
		
		cp5.addCheckBox("averageDirection")
				.setPosition(10, 120)
				.setColorLabel(0)
                .setSize(20, 20)
                .setItemsPerRow(1)
                .setSpacingRow(20)
                .addItem("Average direction", 0);

		new RestrictedEventDispatcher(this, map, new Rectangle(0, 0, 210, 140));

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
					table.getPartnerContinent(row));

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
	}

//	@Override
//	public boolean sketchFullScreen() {
//		return true;
//	}

	@Override
	public void mouseMoved() {

		for (Marker marker : map.getMarkers()) {
			if (!marker.equals(MapState.getInstance().getSelectedMarker())) {
				marker.setSelected(false);
			}
		}

		Marker hitMarker = map.getFirstHitMarker(mouseX, mouseY);
		if (hitMarker != null) {
			map.getMarkers().remove(hitMarker);
			map.getMarkers().add(hitMarker);
			hitMarker.setSelected(true);
		}
	}

	@Override
	public void mouseClicked() {
		Marker hitMarker = map.getFirstHitMarker(mouseX, mouseY);
		for (Marker marker : map.getMarkers()) {
			marker.setSelected(false);
		}
		if (hitMarker != null) {
			hitMarker.setSelected(true);
			MapState.getInstance().setSelectedMarker(
					(PartnershipMarker) hitMarker);
		} else {
			MapState.getInstance().setSelectedMarker(null);
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

	public static void main(String[] args) {
		PApplet.main(new String[] { "de.dominikmuench.twintowns.TwinTownsApp" });
	}

}
