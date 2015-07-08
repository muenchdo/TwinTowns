package de.dominikmuench.twintowns;

import controlP5.ControlP5;

public class MapState {

	private static MapState instance;

	private ControlP5 cp5;

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
}
