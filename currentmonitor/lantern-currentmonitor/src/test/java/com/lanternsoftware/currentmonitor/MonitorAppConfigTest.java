package com.lanternsoftware.currentmonitor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

import com.lanternsoftware.datamodel.currentmonitor.Breaker;
import com.lanternsoftware.datamodel.currentmonitor.BreakerConfig;
import com.lanternsoftware.datamodel.currentmonitor.BreakerHub;
import com.lanternsoftware.util.ResourceLoader;
import com.lanternsoftware.util.dao.DaoSerializer;

public class MonitorAppConfigTest {
    private static final String WORKING_DIR = "/opt/currentmonitor/";

    MonitorConfig config = DaoSerializer.parse(ResourceLoader.loadFileAsString(WORKING_DIR + "breaker_config.json"),
            MonitorConfig.class);
    BreakerConfig breakerConfig = DaoSerializer.parse(
            ResourceLoader.loadFileAsString(WORKING_DIR + "breaker_config.json"),
            BreakerConfig.class);

    @Test
    public void loadBreakerConfig() {
        List<Breaker> breakers = breakerConfig.getBreakers();
        assertEquals(7, breakers.size());
        assertEquals(4, breakers.get(0).getSpace());
        assertEquals(7, breakers.get(0).getPort());
        assertEquals("Woonkamer", breakers.get(0).getName());
        assertEquals(30, breakers.get(0).getSizeAmps());
        assertEquals(1, breakers.get(0).getPhaseId());
        assertEquals(1, breakers.get(0).getCalibrationFactor(), 0.01);
        assertEquals(1.6, breakers.get(0).getLowPassFilter(), 0.01);
    }

    @Test
    public void breakerDetailsTest() {
        List<Breaker> breakers = breakerConfig.getBreakers();
        assertEquals(7, breakers.size());
        BreakerHub hub = breakerConfig.getHub();
        assertEquals(0, hub.getPhaseForBreaker(breakers.get(0)).getPhaseOffsetNs());
        assertEquals(13333333, hub.getPhaseForBreaker(breakers.get(5)).getPhaseOffsetNs());
        assertEquals(0, hub.getPhaseForBreaker(breakers.get(4)).getPhaseOffsetNs());
    }

    @Test
    public void breakerConfigHubTest() {
        assertEquals(50, breakerConfig.getHub().getFrequency());
        assertFalse(breakerConfig.getHub().isDebug());
    }
}
