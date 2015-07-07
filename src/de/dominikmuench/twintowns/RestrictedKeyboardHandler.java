package de.dominikmuench.twintowns;

import processing.core.PApplet;
import processing.event.KeyEvent;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.interactions.KeyboardHandler;

/**
 * KeyboardHandler which only accepts key events if the mouse is outside of an exclusion rectangle
 * @author Benjamin Strobel
 */
public class RestrictedKeyboardHandler extends KeyboardHandler {

	private PApplet applet;
	private Rectangle exclusionRect;
	

	/**
	 * Create a KeyboardHadler that only handles key events if the mouse is outside of the exclusion rectangle.
	 * @param applet the Processing applet
	 * @param map the Unfolding map of which the events should be handled
	 * @param exclusionRect the rectangle that defines the area where mouse clicks and keyboard events are ignored
	 */
	public RestrictedKeyboardHandler(PApplet applet, UnfoldingMap map, Rectangle exclusionRect) {
		super(applet, map);
		
		this.applet = applet;
		this.exclusionRect = exclusionRect;
	}

	@Override
	public void keyEvent(KeyEvent event) {
		if(!exclusionRect.contains(applet.mouseX, applet.mouseY)) {
			super.keyEvent(event);			
		}
	}
}
