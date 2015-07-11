package de.dominikmuench.twintowns.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import processing.core.PVector;
import de.dominikmuench.twintowns.MapState;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public class Utilities {
	
	public static final String KEY_INTERSECTION = "intersection";
	public static final String KEY_EDGE = "edge";
	public static final String TOP_EDGE = "top";
	public static final String RIGHT_EDGE = "right";
	public static final String BOTTOM_EDGE = "bottom";
	public static final String LEFT_EDGE = "left";
	public static final String NO_EDGE = "none";

	/**
	 * Finds the intersection of a given line with the edges of the map.
	 * 
	 * @param from The starting point of the line.
	 * @param to The end point of the line.
	 * @return The intersection of the given line with the edges of the map or the endpoint of the line if no intersection exists.
	 */
	public static Map<String, Object> findMapEdgeIntersection(PVector from, PVector to) {
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
		Map<String, Object> intersectionInfo = new HashMap<>();
		int edgeIndex = 0;
		for (PVector[] edge : edges) {
			PVector intersection = intersect(from, to, edge[0], edge[1]);
			if (intersection == null) {
				edgeIndex++;
				continue;
			} else {
				String intersectionEdge;
				switch (edgeIndex) {
				case 0:
					intersectionEdge = TOP_EDGE;
					break;
				case 1:
					intersectionEdge = RIGHT_EDGE;
					break;
				case 2:
					intersectionEdge = BOTTOM_EDGE;
					break;
				case 3:
					intersectionEdge = LEFT_EDGE;
					break;
				default:
					intersectionEdge = NO_EDGE;
					break;
				}
				intersectionInfo.put(KEY_EDGE, intersectionEdge);
				intersectionInfo.put(KEY_INTERSECTION, intersection);
				return intersectionInfo;
			}
		}
		intersectionInfo.put(KEY_EDGE, NO_EDGE);
		intersectionInfo.put(KEY_INTERSECTION, to);
		return intersectionInfo;
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
	
	public static double getAngle(PVector v) {
		return Math.toDegrees(Math.atan2(v.y, v.x));
	}
}
