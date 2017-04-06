package ru.neverdark.hwmon.vendor.infortrend;

import ru.neverdark.hwmon.SnmpManager;
import ru.neverdark.hwmon.hardware.*;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by ufo on 07.03.17.
 */
public class DS3048RE extends DiskArray {

    protected DS3048RE(String ip, String community, String name, int snmpVersion) {
        super(ip, community, name, snmpVersion);
    }

    public DS3048RE(String ip, String community, String name) {
        this(ip, community, name, SnmpManager.VERSION1);
    }

    @Override
    public void collectData() {
        try {
            SnmpManager manager = new SnmpManager(getIp(), getCommnunity(), getSnmpVersion());
            manager.start();

            collectDiskData(manager);
            collectSensorData(manager);

            manager.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final static int MAXIMUM_DISK_COUNT = 240;

    @Override
    protected void collectDiskData(SnmpManager manager) throws IOException {
        for (int i = 1; i < MAXIMUM_DISK_COUNT; i++) {
            Disk disk = getDisk(manager, i);
            if (disk == null) break;
            addDisk(disk);
        }
    }

    private Disk getDisk(SnmpManager manager, int diskIndex) throws IOException {
        String oid = String.format(Locale.US, ".1.3.6.1.4.1.1714.1.1.6.1.4.%d", diskIndex);
        int scsiId = manager.getAsInt(oid);
        if (scsiId >= MAXIMUM_DISK_COUNT) {
            return null;
        }

        oid = String.format(Locale.US, ".1.3.6.1.4.1.1714.1.1.6.1.7.%d", diskIndex);
        long size = Long.valueOf(manager.getAsString(oid)) / 2 / 1024 / 1024;

        oid = String.format(Locale.US, ".1.3.6.1.4.1.1714.1.1.6.1.15.%d", diskIndex);
        String name = manager.getAsString(oid).trim();

        oid = String.format(Locale.US, ".1.3.6.1.4.1.1714.1.1.6.1.17.%d", diskIndex);
        String serial = manager.getAsString(oid).trim();

        oid = String.format(Locale.US, ".1.3.6.1.4.1.1714.1.1.6.1.12.%d", diskIndex);
        int state = manager.getAsInt(oid);
        if (state >= 0 && state <= 7) {
            state = State.HEALTH.ordinal();
        } else {
            state = State.FAIL.ordinal();
        }

        oid = String.format(Locale.US, ".1.3.6.1.4.1.1714.1.1.6.1.11.%d", diskIndex);
        boolean isSpareDisk = manager.getAsInt(oid) == 9;

        oid = String.format(Locale.US, ".1.3.6.1.4.1.1714.1.1.6.1.13.%d", diskIndex);
        String slot = manager.getAsString(oid);

        return new Disk(slot, name, serial, (int) size, State.values()[state], isSpareDisk);
    }

    @Override
    protected void collectSensorData(SnmpManager manager) throws IOException {
        addSensor(getTemperature(manager, ".1.3.6.1.4.1.1714.1.1.9.1.%d.1"));
        addSensor(getTemperature(manager, ".1.3.6.1.4.1.1714.1.1.9.1.%d.2"));
        addSensor(getTemperature(manager, ".1.3.6.1.4.1.1714.1.1.9.1.%d.3"));
        addSensor(getTemperature(manager, ".1.3.6.1.4.1.1714.1.1.9.1.%d.4"));

        addSensor(getTemperature(manager, ".1.3.6.1.4.1.1714.1.1.9.1.%d.10"));
        addSensor(getTemperature(manager, ".1.3.6.1.4.1.1714.1.1.9.1.%d.11"));
        addSensor(getTemperature(manager, ".1.3.6.1.4.1.1714.1.1.9.1.%d.12"));
        addSensor(getTemperature(manager, ".1.3.6.1.4.1.1714.1.1.9.1.%d.13"));

        addSensor(getTemperature(manager, ".1.3.6.1.4.1.1714.1.1.9.1.%d.40"));

        addSensor(getPSU(manager, ".1.3.6.1.4.1.1714.1.1.9.1.%d.19"));
        addSensor(getPSU(manager, ".1.3.6.1.4.1.1714.1.1.9.1.%d.20"));
        addSensor(getPSU(manager, ".1.3.6.1.4.1.1714.1.1.9.1.%d.21"));

        for (int i = 22; i < 40; i++) {
            addSensor(getFAN(manager, ".1.3.6.1.4.1.1714.1.1.9.1.%d." + String.valueOf(i)));
        }

        addSensor(getBattery(manager, ".1.3.6.1.4.1.1714.1.1.9.1.%d.7"));
        addSensor(getBattery(manager, ".1.3.6.1.4.1.1714.1.1.9.1.%d.16"));
    }

    private Sensor getTemperature(SnmpManager manager, String pattern) throws IOException {
        String nameOid = String.format(Locale.US, pattern, 8);
        String valueOid = String.format(Locale.US, pattern, 9);
        String unitOid = String.format(Locale.US, pattern, 10);
        String name = manager.getAsString(nameOid);
        int value = manager.getAsInt(valueOid);
        int unit = manager.getAsInt(unitOid);

        int temp = (value * unit / 1000) - 273;

        State state;
        if (temp >= 70 || temp < 15) {
            state = State.FAIL;
        } else if (temp >= 60) {
            state = State.WARNING;
        } else {
            state = State.HEALTH;
        }

        return new Sensor(SensorType.TEMPERATURE, name, String.valueOf(temp), state);
    }

    private Sensor getPSU(SnmpManager manager, String pattern) throws IOException {
        String nameOid = String.format(Locale.US, pattern, 8);
        String valueOid = String.format(Locale.US, pattern, 9);
        String name = manager.getAsString(nameOid);
        int value = manager.getAsInt(valueOid);

        if (value == 0) {
            value = State.HEALTH.ordinal();
        } else {
            value = State.FAIL.ordinal();
        }

        return new Sensor(SensorType.PSU, name, State.values()[value]);
    }

    private Sensor getBattery(SnmpManager manager, String pattern) throws IOException {
        String nameOid = String.format(Locale.US, pattern, 8);
        String valueOid = String.format(Locale.US, pattern, 9);
        String name = manager.getAsString(nameOid);
        int value = manager.getAsInt(valueOid);

        if (value == 0) {
            value = State.HEALTH.ordinal();
        } else {
            value = State.FAIL.ordinal();
        }

        return new Sensor(SensorType.BATTERY, name, State.values()[value]);
    }

    private Sensor getFAN(SnmpManager manager, String pattern) throws IOException {
        String nameOid = String.format(Locale.US, pattern, 8);
        String valueOid = String.format(Locale.US, pattern, 9);
        String name = manager.getAsString(nameOid);
        int index = manager.getAsInt(valueOid);
        State state = State.HEALTH;
        String speed = null;
        if (index > 7) {
            state = State.FAIL;
        } else {
            final String[] speeds = {"Normal", "Lowest speed", "Second lowest speed",
            "Third lowest speed", "Intermediate speed", "Third highest speed", "Second highest speed",
            "Highest speed"};
            speed = speeds[index];
        }

        return new Sensor(SensorType.FAN, name, speed, state);
    }



}
