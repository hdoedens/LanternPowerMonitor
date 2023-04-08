package com.lanternsoftware.currentmonitor;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.lanternsoftware.currentmonitor.api.ApiBreakerConfig;

public class BreakerConfigTest {

    @Test
    public void saveCalibrationTest() {
        assertTrue(ApiBreakerConfig.saveCalibration(0.05, 50));
    }
}
