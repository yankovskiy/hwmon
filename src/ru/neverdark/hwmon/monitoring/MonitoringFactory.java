package ru.neverdark.hwmon.monitoring;

import ru.neverdark.hwmon.config.Config;
import ru.neverdark.hwmon.monitoring.xymon.XymonMonitoring;

/**
 * Created by ufo on 15.03.17.
 */
public class MonitoringFactory {
    public static Monitoring getInstance(Config config) throws Exception {
        if (config.getMonitoringConfig().getType().toLowerCase().equals("xymon")) {
            return new XymonMonitoring(config);
        }

        throw new Exception("Unsupported monitoring type " + config.getMonitoringConfig().getType());
    }
}
