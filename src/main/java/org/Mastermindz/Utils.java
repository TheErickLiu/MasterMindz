package org.Mastermindz;

import java.util.ArrayList;
import java.awt.geom.Point2D;

public class Utils {

    /**
     * Finds the index of the pole closest to the position given a list of poles.
     * @param poles A list of poles, each represented as a Point2D.Double object.
     * @param position The position to compare, represented as a Point2D.Double object.
     * @return The index of the closest pole in the list.
     */
    public static int closestPoleIndex(ArrayList<Point2D.Double> poles, Point2D.Double position) {
        double minDistance = Double.MAX_VALUE;
        int minIndex = -1;

        for (int i = 0; i < poles.size(); i++) {
            double dist = position.distance(poles.get(i));
            if (dist < minDistance) {
                minDistance = dist;
                minIndex = i;
            }
        }

        return minIndex;
    }

    /**
     * Calculates the angle that the position needs to turn to face the pole from the positive y-axis (north).
     * The output angle is in degrees, within the range (-180°, 180°).
     * A positive angle indicates a clockwise rotation, and a negative angle indicates a counterclockwise rotation.
     * @param position The position point represented as a Point2D.Double object.
     * @param pole The pole point represented as a Point2D.Double object.
     * @return The angle in degrees.
     */
    public static double calculateAngle(Point2D.Double position, Point2D.Double pole) {
        double xDiff = pole.getX() - position.getX();
        double yDiff = pole.getY() - position.getY();

        double angle = Math.toDegrees(Math.atan2(yDiff, xDiff));
        angle = 90 - angle;
        if (angle > 180) {
            angle -= 360;
        } else if (angle < -180) {
            angle += 360;
        }

        return angle;
    }
}
