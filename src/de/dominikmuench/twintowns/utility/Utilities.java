package de.dominikmuench.twintowns.utility;

import java.util.ArrayList;
import java.util.List;

import processing.core.PVector;
import de.dominikmuench.twintowns.MapState;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public class Utilities {

	/**
	 * Finds the intersection of a given line with the edges of the map.
	 * 
	 * @param from The starting point of the line.
	 * @param to The end point of the line.
	 * @return The intersection of the given line with the edges of the map or the endpoint of the line if no intersection exists.
	 */
	public static PVector findMapEdgeIntersection(PVector from, PVector to) {
		UnfoldingMap map = MapState.getInstance().getMap();
		ScreenPosition topLeftCorner = map.getScreenPosition(map.getTopLeftBorder());
		ScreenPosition bottomRightCorner = map.getScreenPosition(map.getBottomRightBorder());
		PVector[] topEdge = new PVector[]{topLeftCorner, new PVector(bottomRightCorner.x, 0)};
		PVector[] rightEdge = new PVector[]{new PVector(bottomRightCorner.x, 0), bottomRightCorner};
		PVector[] bottomEdge = new PVector[]{new PVector(0, bottomRightCorner.y), bottomRightCorner};
		PVector[] leftEdge = new PVector[]{topLeftCorner, new PVector(0, bottomRightCorner.y)};
		List<PVector[]> edges = new ArrayList<PVector[]>();
		edges.add(topEdge);
		edges.add(rightEdge);
		edges.add(bottomEdge);
		edges.add(leftEdge);
		for (PVector[] edge : edges) {
			PVector intersection = intersect(from, to, edge[0], edge[1]);
			if (intersection == null) {
				continue;
			} else {
				return intersection;
			}
		}
		return to;
	}

	// a1 is line1 start, a2 is line1 end, b1 is line2 start, b2 is line2 end
	/**
	 * Intersects two lines.
	 * 
	 * @param a1 The start of the first line.
	 * @param a2 The end of the first line.
	 * @param b1 The start of the second line.
	 * @param b2 The end of the second line.
	 * @return The intersection of the two lines or null if they don't intersect.
	 */
	private static PVector intersect(PVector a1, PVector a2, PVector b1, PVector b2) {
		PVector intersection = new PVector();

		PVector b = PVector.sub(a2, a1);
		PVector d = PVector.sub(b2, b1);
		float bDotDPerpendicular = b.x * d.y - b.y * d.x;

		// if b dot d == 0, it means the lines are parallel so have infinite intersection points
		if (bDotDPerpendicular == 0) {
			return null;
		}

		PVector c = PVector.sub(b1, a1);
		float t = (c.x * d.y - c.y * d.x) / bDotDPerpendicular;
		if (t < 0 || t > 1) {
			return null;
		}

		float u = (c.x * b.y - c.y * b.x) / bDotDPerpendicular;
		if (u < 0 || u > 1) {
			return null;
		}

		intersection = PVector.add(a1, PVector.mult(b, t));

		return intersection;

	}

}
