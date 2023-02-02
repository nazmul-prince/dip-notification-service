package com.dip.unifiedviewer.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimeUtil {

    private static final String ZONE_OFFSET_ID_BD = "+06:00";
    private static final ZoneOffset zoneOffsetBd = ZoneOffset.of(ZONE_OFFSET_ID_BD);

    public static String getEpochTimeFromLocalDateTime(LocalDateTime localDateTime) {

        return String.valueOf(localDateTime.toEpochSecond(zoneOffsetBd));
    }

    public static LocalDateTime getLocalDateTimeFromEpochSecond(long epochSecond) {
        return LocalDateTime.ofEpochSecond(epochSecond, 0, zoneOffsetBd);
    }
}
