package ru.neverdark.hwmon.hardware;

import ru.neverdark.hwmon.config.DeviceConfig;
import ru.neverdark.hwmon.vendor.dell.MD3820F;
import ru.neverdark.hwmon.vendor.infortrend.DS3024RB;
import ru.neverdark.hwmon.vendor.infortrend.DS3048RE;

/**
 * Created by ufo on 15.03.17.
 */
public class HardwareFactory {
    public static NetworkHardware getInstance(DeviceConfig deviceConfig) throws Exception {
        if (deviceConfig.getVendor().toLowerCase().equals("infortrend")) {
            return handleInfortrend(deviceConfig);
        }

        if (deviceConfig.getVendor().toLowerCase().equals("dell")) {
            return handleDell(deviceConfig);
        }

        throw new Exception("Unsupported vendor " + deviceConfig.getVendor());
    }

    private static NetworkHardware handleDell(DeviceConfig deviceConfig) throws Exception {
        if (deviceConfig.getModel().toLowerCase().equals("md3820f")) {
            return new MD3820F(deviceConfig.getIp(), deviceConfig.getName());
        }

        throw new Exception("Unsupported device " + deviceConfig.getModel() + " from vendor " + deviceConfig.getVendor());
    }

    private static NetworkHardware handleInfortrend(DeviceConfig deviceConfig) throws Exception {
        if (deviceConfig.getModel().toLowerCase().equals("ds3048re")) {
            return new DS3048RE(deviceConfig.getIp(), deviceConfig.getCommunity(), deviceConfig.getName());
        } else if (deviceConfig.getModel().toLowerCase().equals("ds3024rb")) {
            return new DS3024RB(deviceConfig.getIp(), deviceConfig.getCommunity(), deviceConfig.getName());
        }

        throw new Exception("Unsupported device " + deviceConfig.getModel() + " from vendor " + deviceConfig.getVendor());
    }
}
