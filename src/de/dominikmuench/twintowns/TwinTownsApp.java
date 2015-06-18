package de.dominikmuench.twintowns;

import processing.core.PApplet;
import processing.data.TableRow;
import de.dominikmuench.twintowns.markers.MunicipalityMarker;
import de.dominikmuench.twintowns.markers.PartnershipMarker;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.StamenMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

@SuppressWarnings("serial")
public class TwinTownsApp extends PApplet {

	private TwinTownTable table;
	private UnfoldingMap map;

	/* (non-Javadoc)
	 * @see processing.core.PApplet#setup()
	 */
	public void setup() {
		size(1280, 800);
		smooth();
		map = new UnfoldingMap(this, new StamenMapProvider.TonerLite());
		map.setTweening(true);
		map.zoomAndPanTo(6, new Location(51.164181f, 10.454150f));
		MapUtils.createDefaultEventDispatcher(this, map);

		table = new TwinTownTable(this);
		for (TableRow row : table.rows()) {	
			////////// My MacBook is too slow!
			if (!(table.getPartnerContinent(row).contains("Europe") && table.getState(row).contains("Bayern"))) {
				continue;
			}
			//////////

			Location fromLocation = new Location(table.getLatitude(row), table.getLongitude(row));
			MunicipalityMarker fromMarker = new MunicipalityMarker(fromLocation);
			Location toLocation = new Location(table.getPartnerLatitude(row), table.getPartnerLongitude(row));
			MunicipalityMarker toMarker = new MunicipalityMarker(toLocation);
			PartnershipMarker connMarker = new PartnershipMarker(fromMarker, toMarker);
			map.addMarkers(connMarker, fromMarker, toMarker);
		}
	}

	public void draw() {
		map.draw();
	}

	public void mouseMoved() {
		for (Marker marker : map.getMarkers()) {
			marker.setSelected(false);
		}
		Marker marker = map.getFirstHitMarker(mouseX, mouseY);
		if (marker != null)
			marker.setSelected(true);
	}

	public void mouseClicked() {
		System.out.println(map.getLocation(mouseX, mouseY));
	}

	public static void main(String[] args) {
		PApplet.main(new String[] {"de.dominikmuench.twintowns.TwinTownsApp"});
	}

}
