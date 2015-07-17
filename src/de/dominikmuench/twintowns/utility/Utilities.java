package de.dominikmuench.twintowns.utility;

import java.util.ArrayList;
import java.util.List;

import processing.core.PVector;
import de.dominikmuench.twintowns.MapState;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public class Utilities {

	public static enum Edge {
		TOP, RIGHT, BOTTOM, LEFT, NONE;

		public static Edge fromInteger(int x) {
			switch (x) {
			case 0:
				return TOP;
			case 1:
				return RIGHT;
			case 2:
				return BOTTOM;
			case 3:
				return LEFT;
			case 4:
				return NONE;
			}
			return null;
		}
	}

	public static class Intersection {

		public PVector position;
		public Edge edge;

		public Intersection(PVector position, Edge edge) {
			this.position = position;
			this.edge = edge;
		}
	}

	/**
	 * Checks if a given location is visible inside the current map view.
	 * 
	 * @param location
	 * @return
	 */
	public static boolean isOnMap(PVector screenPosition) {
		UnfoldingMap map = MapState.getInstance().getMap();
		ScreenPosition topLeftCorner = map.getScreenPosition(map
				.getTopLeftBorder());
		ScreenPosition bottomRightCorner = map.getScreenPosition(map
				.getBottomRightBorder());
		return screenPosition.x > topLeftCorner.x
				&& screenPosition.x < bottomRightCorner.x
				&& screenPosition.y > topLeftCorner.y
				&& screenPosition.y < bottomRightCorner.y;
	}

	/**
	 * Finds the intersection of a given line with the edges of the map.
	 * 
	 * @param from
	 *            The starting point of the line.
	 * @param to
	 *            The end point of the line.
	 * @return A list of Intersections (can be empty).
	 */
	public static List<Intersection> findMapEdgeIntersections(PVector from,
			PVector to) {
		UnfoldingMap map = MapState.getInstance().getMap();
		ScreenPosition topLeftCorner = map.getScreenPosition(map
				.getTopLeftBorder());
		ScreenPosition bottomRightCorner = map.getScreenPosition(map
				.getBottomRightBorder());
		PVector[] topEdge = new PVector[] { topLeftCorner,
				new PVector(bottomRightCorner.x, 0) };
		PVector[] rightEdge = new PVector[] {
				new PVector(bottomRightCorner.x, 0), bottomRightCorner };
		PVector[] bottomEdge = new PVector[] {
				new PVector(0, bottomRightCorner.y), bottomRightCorner };
		PVector[] leftEdge = new PVector[] { topLeftCorner,
				new PVector(0, bottomRightCorner.y) };
		List<PVector[]> edges = new ArrayList<PVector[]>();
		edges.add(topEdge);
		edges.add(rightEdge);
		edges.add(bottomEdge);
		edges.add(leftEdge);
		List<Intersection> intersections = new ArrayList<>();
		int edgeIndex = 0;
		for (PVector[] edge : edges) {
			PVector intersection = intersect(from, to, edge[0], edge[1]);
			if (intersection != null) {
				intersections.add(new Intersection(intersection, Edge
						.fromInteger(edgeIndex)));
			}
			edgeIndex++;
		}
		return intersections;
	}

	/**
	 * Finds the closest intersection from a vector and a list of intersections.
	 * 
	 * @param from
	 * @param intersections
	 * @return
	 */
	public static Intersection getClosestIntersection(PVector from,
			List<Intersection> intersections) {
		Intersection closestIntersection = new Intersection(new PVector(Float.MAX_VALUE, Float.MAX_VALUE), Edge.NONE);
		float maxDist = Float.MAX_VALUE;
		for (Intersection intersection : intersections) {
			float dist = Math.abs(PVector.dist(from,
					intersection.position));
			if (dist <= maxDist) {
				maxDist = dist;
				closestIntersection = intersection;
			}
		}
		return closestIntersection;
	}

	/**
	 * Intersects two lines.
	 * 
	 * @param a1
	 *            The start of the first line.
	 * @param a2
	 *            The end of the first line.
	 * @param b1
	 *            The start of the second line.
	 * @param b2
	 *            The end of the second line.
	 * @return The intersection of the two lines or null if they don't
	 *         intersect.
	 */
	private static PVector intersect(PVector a1, PVector a2, PVector b1,
			PVector b2) {
		PVector intersection = new PVector();

		PVector b = PVector.sub(a2, a1);
		PVector d = PVector.sub(b2, b1);
		float bDotDPerpendicular = b.x * d.y - b.y * d.x;

		// if b dot d == 0, it means the lines are parallel so have infinite
		// intersection points
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
