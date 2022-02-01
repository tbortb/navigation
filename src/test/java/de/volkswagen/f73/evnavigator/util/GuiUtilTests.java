package de.volkswagen.f73.evnavigator.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
class GuiUtilTests {

    @Test
    void distanceShouldBeFormattedHumanReadable() {
        Assertions.assertEquals("800 meters", GuiUtils.distanceToString(800, false));
        Assertions.assertEquals("800 meters", GuiUtils.distanceToString(0.8, true));
        Assertions.assertEquals("1.25 km", GuiUtils.distanceToString(1.25, true));
        Assertions.assertEquals("1.25 km", GuiUtils.distanceToString(1250, false));
    }

    @Test
    void timeShouldBeFormattedHumanReadable() {
        Duration oneHourThirtyMinutes = Duration.ofMinutes(90);
        Duration tenMinutes = Duration.ofMinutes(10);
        Duration twoHours = Duration.ofMinutes(120);
        Duration oneMinute = Duration.ofMinutes(1);

        Assertions.assertEquals("1 hour, 30 minutes", GuiUtils.formatDurationToTimeString(oneHourThirtyMinutes));
        Assertions.assertEquals("10 minutes", GuiUtils.formatDurationToTimeString(tenMinutes));
        Assertions.assertEquals("2 hours", GuiUtils.formatDurationToTimeString(twoHours));
        Assertions.assertEquals("1 minute", GuiUtils.formatDurationToTimeString(oneMinute));
    }
}
