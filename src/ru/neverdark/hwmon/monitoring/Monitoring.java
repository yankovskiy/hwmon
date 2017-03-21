package ru.neverdark.hwmon.monitoring;

import ru.neverdark.hwmon.config.Config;
import ru.neverdark.hwmon.config.DeviceConfig;
import ru.neverdark.hwmon.hardware.DeviceType;
import ru.neverdark.hwmon.hardware.HardwareFactory;
import ru.neverdark.hwmon.hardware.NetworkHardware;

/**
 * Created by ufo on 15.03.17.
 */
public abstract class Monitoring {
    private final Config mConfig;

    public Monitoring(Config config) {
        mConfig = config;
    }

    public Config getConfig() {
        return mConfig;
    }

    public void run() {
        for (DeviceConfig deviceConfig : mConfig.getDeviceList()) {
            NetworkHardware hardware = null;
            try {
                hardware = HardwareFactory.getInstance(deviceConfig);
                hardware.collectData();

                if (hardware.getDeviceType() == DeviceType.DISK_ARRAY) {
                    handleDiskArray(hardware);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    protected abstract void handleDiskArray(NetworkHardware hardware);
    protected abstract void sendData(NetworkHardware hardware, CommonStatus status);
}
