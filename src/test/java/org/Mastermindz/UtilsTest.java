package org.Mastermindz;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {
    @Test
    public void testCalculateAngle () {
        double[] position = {0, 0};
        double[][] poles = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, -1}, {-1, 1}};
        double[] expected = {0, 90, 180, -90, 45, 135, -135, -45};

        for (int i = 0; i < poles.length; i++) {
            assertEquals(expected[i], Utils.calculateAngle(position, poles[i]));
        }
    }
}