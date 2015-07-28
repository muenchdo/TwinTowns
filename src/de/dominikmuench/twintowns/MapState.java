package de.dominikmuench.twintowns;

import controlP5.ControlP5;
import de.dominikmuench.twintowns.markers.PartnershipMarker;
import de.fhpotsdam.unfolding.UnfoldingMap;

public class MapState {

	private static MapState instance;

	private ControlP5 cp5;

	private UnfoldingMap map;

	private PartnershipMarker selectedMarker;
	private PartnershipMarker clickedMarker;

	private MapState() {
	}

	public static MapState getInstance() {
		if (MapState.instance == null) {
			MapState.instance = new MapState();
		}
		return MapState.instance;
	}

	public ControlP5 getCp5() {
		return cp5;
	}

	public void setCp5(ControlP5 cp5) {
		this.cp5 = cp5;
	}

	public UnfoldingMap getMap() {
		return map;
	}

	public void setMap(UnfoldingMap map) {
		this.map = map;
	}

	public PartnershipMarker getClickedMarker() {
		return clickedMarker;
	}

	public void setClickedMarker(PartnershipMarker clickedMarker) {
		this.clickedMarker = clickedMarker;
	}

	public PartnershipMarker getSelectedMarker() {
		return selectedMarker;
	}

	public void setSelectedMarker(PartnershipMarker selectedMarker) {
		this.selectedMarker = selectedMarker;
	}
}
