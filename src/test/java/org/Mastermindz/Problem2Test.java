package org.Mastermindz;

import org.junit.Test;

import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Problem2Test {
    @Test
    public void testCalculateAngle () {
        double[] position = {0, 0};
        double[][] poles = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, -1}, {-1, 1}};
        double[] expected = {0, 90, 180, -90, 45, 135, -135, -45};

        for (int i = 0; i < poles.length; i++) {
            Point2D.Double pos = new Point2D.Double(position[0], position[1]);
            Point2D.Double pole = new Point2D.Double(poles[i][0], poles[i][1]);
            assertEquals(expected[i], Problem2.calculateAngle(pos, pole));
        }
    }

}
