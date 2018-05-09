package com.github.dylmeadows.common.lang;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class MathsTest {

    private static final int[] INTEGER_MIN_DATA = new int[]{9, 2, -74, 18, -3, -66, 23, -95, -47, -81, -69,
            -58, 14, 45, -9, 92, -27, -56, 98, 76, 7, 48, 12, 44, 87};
    private static final int INTEGER_MIN_RESULT = -95;

    private static final int[] INTEGER_MAX_DATA = new int[]{87, 67, 78, 50, 37, 75, 37, 33, 30, 17, 12, 30,
            32, 20, 47, 17, 65, 53, 7, 13, 42, 13, 85, 39, 76};
    private static final int INTEGER_MAX_RESULT = 87;

    private static final double[] DOUBLE_MIN_DATA = new double[]{9, 2, -74, 18, -3, -66, 23, -95, -47, -81, -69,
            -58, 14, 45, -9, 92, -27, -56, 98, 76, 7, 48, 12, 44, 87};
    private static final double DOUBLE_MIN_RESULT = -95;

    private static final double[] DOUBLE_MAX_DATA = new double[]{87, 67, 78, 50, 37, 75, 37, 33, 30, 17, 12, 30,
            32, 20, 47, 17, 65, 53, 7, 13, 42, 13, 85, 39, 76};
    private static final double DOUBLE_MAX_RESULT = 87;

    @Test
    public void testIntegerMin() {
        int min = Maths.min(INTEGER_MIN_DATA);
        assertThat(min, is(INTEGER_MIN_RESULT));
    }

    @Test
    public void testIntegerMax() {
        int max = Maths.max(INTEGER_MAX_DATA);
        assertThat(max, is(INTEGER_MAX_RESULT));
    }

    @Test
    public void testDoubleMin() {
        double min = Maths.min(DOUBLE_MIN_DATA);
        assertThat(min, is(DOUBLE_MIN_RESULT));
    }

    @Test
    public void testDoubleMax() {
        double max = Maths.max(DOUBLE_MAX_DATA);
        assertThat(max, is(DOUBLE_MAX_RESULT));
    }
}
