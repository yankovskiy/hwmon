package ru.neverdark.hwmon.monitoring.xymon;

import ru.neverdark.hwmon.hardware.*;
import ru.neverdark.hwmon.monitoring.CommonStatus;

import java.util.List;
import java.util.Locale;

/**
 * Created by ufo on 15.03.17.
 */
public class XymonDiskArray {
    private final DiskArray mHardware;

    public XymonDiskArray(NetworkHardware hardware) {
        mHardware = (DiskArray) hardware;
    }

    public CommonStatus prepareDiskStatus() {
        List<Disk> disks = mHardware.getDiskList();
        if (disks.size() > 0) {
            String html = "<table class=hwmon><tr><th colspan=2>Slot</th><th>Name</th><th>Serial</th><th>Size</th><th>Is spare</th></tr>";
            String localStatus = null;
            boolean isGood = true;

            for (Disk disk : disks) {
                if (disk.getState() == State.HEALTH) {
                    localStatus = "&green";
                } else if (disk.getState() == State.FAIL) {
                    localStatus = "&red";
                } else {
                    localStatus = "&purple";
                }

                html += String.format(Locale.US, "<tr><td>%s</td><td>%d</td><td>%s</td><td>%s</td><td>%dGb</td><td>%b</td></tr>",
                        localStatus, disk.getSlot(), disk.getName(), disk.getSerial(), disk.getSize(), disk.isSpareDisk());

                isGood &= localStatus.equals("&green");
            }
            html += "</table>";

            return new CommonStatus("disks", isGood ? "green" : "red", html);
        }

        return null;
    }

    public CommonStatus prepareTempStatus() {
        List<Sensor> sensorList = mHardware.getSensorList(SensorType.TEMPERATURE);
        if (sensorList.size() > 0) {
            String html = "<table class=hwmon><tr><th colspan=2>Name</th><th>Temperature</th></tr>";
            for (Sensor sensor: sensorList) {
                html += String.format(Locale.US, "<tr><td>&green</td><td>%s</td><td>%d</td></tr>", sensor.getName(), sensor.getIntValue());
            }
            html += "</table>";

            return new CommonStatus("temp", "green", html);
        }

        return null;
    }

    public CommonStatus prepareBatteryStatus() {
        List<Sensor> sensorList = mHardware.getSensorList(SensorType.BATTERY);
        if (sensorList.size() > 0) {
            String html = "<table class=hwmon><tr><th colspan=2>Name</th></tr>";
            String localStatus = null;
            boolean isGood = true;

            for (Sensor sensor: sensorList) {
                if (sensor.getState() == State.HEALTH) {
                    localStatus = "&green";
                } else if (sensor.getState() == State.FAIL) {
                    localStatus = "&red";
                } else {
                    localStatus = "&purple";
                }
                html += String.format(Locale.US, "<tr><td>%s</td><td>%s</td></tr>", localStatus, sensor.getName());
                isGood &= localStatus.equals("&green");
            }
            html += "</table>";

            return new CommonStatus("battery", isGood ? "green" : "red", html);
        }

        return null;
    }

    public CommonStatus preparePsuStatus() {
        List<Sensor> sensorList = mHardware.getSensorList(SensorType.PSU);
        if (sensorList.size() > 0) {
            String html = "<table class=hwmon><tr><th colspan=2>Name</th></tr>";
            String localStatus = null;
            boolean isGood = true;

            for (Sensor sensor: sensorList) {
                if (sensor.getState() == State.HEALTH) {
                    localStatus = "&green";
                } else if (sensor.getState() == State.FAIL) {
                    localStatus = "&red";
                } else {
                    localStatus = "&purple";
                }
                html += String.format(Locale.US, "<tr><td>%s</td><td>%s</td></tr>", localStatus, sensor.getName());
                isGood &= localStatus.equals("&green");
            }
            html += "</table>";

            return new CommonStatus("psu", isGood ? "green" : "red", html);
        }

        return null;
    }

    public CommonStatus prepareFanStatus() {
        List<Sensor> sensorList = mHardware.getSensorList(SensorType.FAN);
        if (sensorList.size() > 0) {
            String html = "<table class=hwmon><tr><th colspan=2>Name</th><th>Speed</th></tr>";
            for (Sensor sensor: sensorList) {
                html += String.format(Locale.US, "<tr><td>&green</td><td>%s</td><td>%s</td></tr>", sensor.getName(), sensor.getStringValue());
            }
            html += "</table>";

            return new CommonStatus("fan", "green", html);
        }

        return null;
    }
}
