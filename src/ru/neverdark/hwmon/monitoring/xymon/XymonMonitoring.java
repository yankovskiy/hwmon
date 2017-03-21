package ru.neverdark.hwmon.monitoring.xymon;

import ru.neverdark.hwmon.Utils;
import ru.neverdark.hwmon.config.Config;
import ru.neverdark.hwmon.hardware.NetworkHardware;
import ru.neverdark.hwmon.monitoring.CommonStatus;
import ru.neverdark.hwmon.monitoring.Monitoring;

/**
 * Created by ufo on 15.03.17.
 */
public class XymonMonitoring extends Monitoring {
    public XymonMonitoring(Config config) {
        super(config);
    }

    @Override
    protected void handleDiskArray(NetworkHardware hardware) {
        XymonDiskArray diskArray = new XymonDiskArray(hardware);

        CommonStatus status = diskArray.prepareDiskStatus();
        sendData(hardware, status);

        status = diskArray.prepareTempStatus();
        sendData(hardware, status);

        status = diskArray.prepareBatteryStatus();
        sendData(hardware, status);

        status = diskArray.preparePsuStatus();
        sendData(hardware, status);

        status = diskArray.prepareFanStatus();
        sendData(hardware, status);
    }

    @Override
    protected void sendData(NetworkHardware hardware, CommonStatus status) {
        if (status != null) {
            String command = String.format("$BB $BBDISP \"status %s.%s %s `date` %s <br><br><br> %s\"",
                    hardware.getName(), status.getColumnName(), status.getStatus(), status.getColumnName() + " status",
                    status.getData());
            Utils.executeShellCommand(command);
        }
    }
}
