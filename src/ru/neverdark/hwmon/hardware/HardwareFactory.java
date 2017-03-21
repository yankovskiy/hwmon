package ru.neverdark.hwmon.hardware;

import ru.neverdark.hwmon.config.DeviceConfig;
import ru.neverdark.hwmon.vendor.infortrend.DS3024RB;
import ru.neverdark.hwmon.vendor.infortrend.DS3048RE;

/**
 * Created by ufo on 15.03.17.
 */
public class HardwareFactory {
    public static NetworkHardware getInstance(DeviceConfig deviceConfig) throws Exception {
        if (deviceConfig.getVendor().toLowerCase().equals("infrotrend")) {
            return handleInfrotrend(deviceConfig);
        }

        throw new Exception("Unsupported vendor " + deviceConfig.getVendor());
    }

    private static NetworkHardware handleInfrotrend(DeviceConfig deviceConfig) throws Exception {
        if (deviceConfig.getModel().toLowerCase().equals("ds3048re")) {
            return new DS3048RE(deviceConfig.getIp(), deviceConfig.getCommunity(), deviceConfig.getName());
        } else if (deviceConfig.getModel().toLowerCase().equals("ds3024rb")) {
            return new DS3024RB(deviceConfig.getIp(), deviceConfig.getCommunity(), deviceConfig.getName());
        }

        throw new Exception("Unsupported device " + deviceConfig.getModel() + " from vendor " + deviceConfig.getVendor());
    }
}
