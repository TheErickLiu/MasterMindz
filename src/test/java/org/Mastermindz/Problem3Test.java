package org.Mastermindz;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Problem3Test {

    @Test
    public void testCheckIntersection() {
        int[][] ps = {{3, 9}, {2, 5}, {1, 4}, {4, 3}};
        int[][] p1s = {{2, 9}, {3, 2}, {3, 5}, {6, 6}};
        int[][] p2s = {{4, 9}, {3, 6}, {4, 4}, {3, 3}};
        boolean[] expected = {true, true, true, false};

        for (int i = 0; i < ps.length; i++) {
            assertEquals(expected[i], Problem3.checkIntersection(ps[i][0], ps[i][1], p1s[i], p2s[i]));
        }
    }

}
