package de.dominikmuench.twintowns.utility;

import java.awt.Color;

public final class Style {

	private static final int lowOpacity = 100;
	private static final int fullOpacity = 255;

	/**
	 * Blue with full opacity.
	 */
	public static final int BLUE_FULL = new Color(0, 200, 200, fullOpacity)
			.getRGB();

	/**
	 * Blue with low opacity.
	 */
	public static final int BLUE_LOW = new Color(0, 200, 200, lowOpacity)
			.getRGB();

	/**
	 * Yellow with full opacity.
	 */
	public static final int YELLOW_FULL = new Color(200, 200, 0, fullOpacity)
			.getRGB();

	/**
	 * Yellow with low opacity.
	 */
	public static final int YELLOW_LOW = new Color(200, 200, 0, lowOpacity)
			.getRGB();
	
	/**
	 * Base marker size
	 */
	public static final float BASE_MARKER_SIZE = 0.3f;

	/**
	 * Marker size
	 */
	public static final int MARKER_SIZE = 10;

}
