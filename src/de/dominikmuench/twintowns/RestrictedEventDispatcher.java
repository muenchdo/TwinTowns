package de.dominikmuench.twintowns;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.interactions.KeyboardHandler;
import de.fhpotsdam.unfolding.interactions.MouseHandler;

/**
 *	A default EventDispatcher which only accepts mouse clicks outside of an exclusion rectangle 
 * @author Benjamin Strobel
 */
public class RestrictedEventDispatcher extends EventDispatcher {

	/**
	 * Restricted EventDispatcher that ignores mouse clicks inside the exclusion rectangle 
	 * @param applet the Processing applet
	 * @param map the Unfolding map of which the events should be handled
	 * @param exclusionRect the rectangle that defines the area where mouse clicks and keyboard events are ignored
	 */
	public RestrictedEventDispatcher(PApplet applet, UnfoldingMap map, Rectangle exclusionRect) {
		MouseHandler mouseHandler = createRestrictedMouseHandler(applet, map, exclusionRect);
		addBroadcaster(mouseHandler);
		
		KeyboardHandler keyboardHandler = new RestrictedKeyboardHandler(applet, map, exclusionRect);
		addBroadcaster(keyboardHandler);

		register(map, "pan");
		register(map, "zoom");
		map.setActive(true);	
	}
	
	/**
	 * Create a MouseHadler that only handles mouse clicks outside if the exclusion rectangle.
	 * @param map the Unfolding map of which the events should be handled
	 * @param exclusionRect the rectangle that defines the area where mouse clicks and keyboard events are ignored
	 * @return the restricted MouseHandler
	 */
	private MouseHandler createRestrictedMouseHandler(PApplet applet, UnfoldingMap map, Rectangle exclusionRect) {
		return new MouseHandler(applet, map) {
			@Override
			public void mouseClicked() {
				// only consider clicks outside the exclusion rectangle
				if(!mouseIsInsideExclusionRect()) {
					super.mouseClicked();
				}				
			}
			
			@Override
			public void mouseDragged() {
				// only consider drags outside the exclusion rectangle
				if(!mouseIsInsideExclusionRect()) {
					super.mouseDragged();
				}
			}
			
			/**
			 * @return true if the current mouse position is inside the exclusion rectangle
			 */			
			public boolean mouseIsInsideExclusionRect() {
				return exclusionRect.contains(applet.mouseX, applet.mouseY);
			}
		};
	}
}