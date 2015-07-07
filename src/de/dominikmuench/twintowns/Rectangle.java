package de.dominikmuench.twintowns;

/**
 * Simple rectangle class
 * @author Benjamin
 */
public class Rectangle {
	/**
	 * the x coordinate
	 */
	public int x;

	/**
	 * the y coordinate
	 */
	public int y; 
	
	/**
	 * the width of the rectangle
	 */
	public int width;
	
	/**
	 * the height of the rectangle
	 */
	public int height;
	
	/**
	 * Simple rectangle 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param width the width of the rectangle
	 * @param height the height of the rectangle
	 */
	public Rectangle(int x, int y, int width, int height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Determines if the rectangle contains the coordinate specified by the parameters
	 * @param px the x coordinate of the point to be checked
	 * @param py the y coordinate of the point to be checked
	 * @return true if the point at the x,y coordinate is inside the rectangle
	 */
	public boolean contains(int px, int py) {
		return !(px < x || px > x + width || py < y || py > y + height);
	}
}
