package ru.neverdark.hwmon.hardware;

import java.util.Locale;

public abstract class NetworkHardware {
    private String mIP;
    private String mCommnunity;
    private String mName;
    private int mSnmpVersion;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getSnmpVersion() {
        return mSnmpVersion;
    }

    public void setSnmpVersion(int snmpVersion) {
        this.mSnmpVersion = snmpVersion;
    }

    public String getIp() {
        return mIP;
    }

    public void setIp(String ip) {
        this.mIP = ip;
    }

    public String getCommnunity() {
        return mCommnunity;
    }

    public void setCommnunity(String commnunity) {
        this.mCommnunity = commnunity;
    }

    public NetworkHardware(String ip, String commnunity, String name, int snmpVersion) {
        this.mIP = ip;
        this.mCommnunity = commnunity;
        this.mName = name;
        this.mSnmpVersion = snmpVersion;
    }

    public abstract void collectData();
    public abstract void dump();
    public abstract DeviceType getDeviceType();

    @Override
    public String toString() {
        return String.format(Locale.US, "%s: %s", getName(), getIp());
    }
}
