package ru.neverdark.hwmon.hardware;

import ru.neverdark.hwmon.SnmpManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ufo on 06.03.17.
 */
public abstract class DiskArray extends NetworkHardware {
    private final List<Disk> mDiskList;
    private final List<Sensor> mTempSensorList;
    private final List<Sensor> mPSUSensorList;
    private final List<Sensor> mFANSensorList;
    private final List<Sensor> mBatteryList;

    protected DiskArray(String ip, String commnunity, String name, int snmpVersion) {
        super(ip, commnunity, name, snmpVersion);
        mDiskList = new ArrayList<>();
        mTempSensorList = new ArrayList<>();
        mPSUSensorList = new ArrayList<>();
        mFANSensorList = new ArrayList<>();
        mBatteryList = new ArrayList<>();
    }

    protected void addDisk(Disk disk) {
        mDiskList.add(disk);
    }
    protected void addSensor(Sensor sensor) {
        if (sensor.getType() == SensorType.TEMPERATURE) {
            mTempSensorList.add(sensor);
        } else if (sensor.getType() == SensorType.PSU) {
            mPSUSensorList.add(sensor);
        } else if (sensor.getType() == SensorType.FAN) {
            mFANSensorList.add(sensor);
        } else if (sensor.getType() == SensorType.BATTERY) {
            mBatteryList.add(sensor);
        }
    }

    public List<Disk> getDiskList() {
        return mDiskList;
    }

    public List<Sensor> getSensorList(SensorType type) {
        if (type == SensorType.TEMPERATURE) {
            return mTempSensorList;
        } else if (type == SensorType.PSU) {
            return mPSUSensorList;
        } else if (type == SensorType.FAN) {
            return mFANSensorList;
        } else if (type == SensorType.BATTERY) {
            return mBatteryList;
        } else {
            return null;
        }
    }

    @Override
    public void dump() {
        System.out.printf("%s\n", this.toString());

        if (mDiskList.size() > 0) {
            System.out.printf("\tDisks:\n");
            for (Disk disk : mDiskList) {
                System.out.printf("\t\t%s\n", disk.toString());
            }
        }

        if (mTempSensorList.size() > 0) {
            System.out.printf("\tTemperature Sensors:\n");
            for (Sensor sensor : mTempSensorList) {
                System.out.printf("\t\t%s\n", sensor.toString());
            }
        }

        if (mPSUSensorList.size() > 0) {
            System.out.printf("\tPSU Sensors:\n");
            for (Sensor sensor : mPSUSensorList) {
                System.out.printf("\t\t%s\n", sensor.toString());
            }
        }

        if (mFANSensorList.size() > 0) {
            System.out.printf("\tFAN Sensors:\n");
            for (Sensor sensor : mFANSensorList) {
                System.out.printf("\t\t%s\n", sensor.toString());
            }
        }

        if (mBatteryList.size() > 0) {
            System.out.printf("\tBattery Sensors:\n");
            for (Sensor sensor : mBatteryList) {
                System.out.printf("\t\t%s\n", sensor.toString());
            }
        }
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.DISK_ARRAY;
    }

    protected abstract void collectDiskData(SnmpManager manager) throws IOException;
    protected abstract void collectSensorData(SnmpManager manager) throws IOException;
}
