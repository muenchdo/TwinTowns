package de.dominikmuench.twintowns;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.data.TableRow;
import controlP5.ControlP5;
import de.dominikmuench.twintowns.markers.MunicipalityMarker;
import de.dominikmuench.twintowns.markers.PartnershipMarker;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.events.MapEvent;
import de.fhpotsdam.unfolding.events.MapEventListener;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.EsriProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

@SuppressWarnings("serial")
public class TwinTownsApp extends PApplet implements MapEventListener {

	private TwinTownTable table;
	private UnfoldingMap map;
	private EventDispatcher mouseEventDispatcher;
	private ControlP5 cp5; 

	/* (non-Javadoc)
	 * @see processing.core.PApplet#setup()
	 */
	@Override
	public void setup() {
		// General
		size(displayWidth, displayHeight);
		smooth();
		
		// Map
		map = new UnfoldingMap(this, new EsriProvider.WorldGrayCanvas());
		map.setTweening(true);
		map.zoomAndPanTo(6, new Location(51.164181f, 10.454150f));
		map.setZoomRange(3, 9);
		MapUtils.createDefaultEventDispatcher(this, map);
		mouseEventDispatcher = MapUtils.createMouseEventDispatcher(this, map);
		
		mouseEventDispatcher.register(this, "click");
		
		// UI
		cp5 = new ControlP5(this);
		cp5.addButton("colorA")
	     .setValue(0)
	     .setPosition(100,100)
	     .setSize(200,19);
		
		// Data & Markers
		table = new TwinTownTable(this);

		List<PartnershipMarker> markers = new ArrayList<PartnershipMarker>();
		PartnershipMarker lastMarker = null;
		for (TableRow row : table.rows()) {	
			////////// My MacBook is too slow!
//			if (!(table.getPartnerContinent(row).contains("Europe") && table.getState(row).contains("Bayern"))) {
//				continue;
//			}
			if (table.getMunicipality(row).contains("Kreis")) {
				continue;
			}
			//////////

			Location fromLocation = new Location(table.getLatitude(row), table.getLongitude(row));
			MunicipalityMarker fromMarker = new MunicipalityMarker(fromLocation);
			
			Location toLocation = new Location(table.getPartnerLatitude(row), table.getPartnerLongitude(row));
			MunicipalityMarker toMarker = new MunicipalityMarker(toLocation);
			
			String municipality = table.getMunicipality(row);
			if (lastMarker == null || !lastMarker.getMunicipality().equals(municipality)) {
				PartnershipMarker connMarker = new PartnershipMarker(table.getMunicipality(row), fromMarker, toMarker);
				markers.add(connMarker);
				lastMarker = connMarker;
			} else {
				lastMarker.addToMarker(toMarker);
			}
		}
		for (PartnershipMarker marker : markers) {
			map.addMarker(marker);
		}
	}

	/* (non-Javadoc)
	 * @see processing.core.PApplet#draw()
	 */
	@Override
	public void draw() {
		map.draw();
	}

	@Override
	public boolean sketchFullScreen() {
		return true;
	}

	@Override
	public void mouseMoved() {
		for (Marker marker : map.getMarkers()) {
			marker.setSelected(false);
		}
		List<Marker> markers = map.getHitMarkers(mouseX, mouseY);
		String firstMunicipality;
		if (!markers.isEmpty()) {
			firstMunicipality = ((PartnershipMarker) markers.get(0)).getMunicipality();  
			for (Marker marker : markers) {
				String municipality = ((PartnershipMarker) marker).getMunicipality();
				if (municipality.equals(firstMunicipality)) {
					marker.setSelected(true);
				}
			}
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
		PApplet.main(new String[] {"de.dominikmuench.twintowns.TwinTownsApp"});
	}

}
