package ru.neverdark.hwmon.config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by ufo on 15.03.17.
 */
@XmlRootElement(name = "device")
public class DeviceConfig {
    @XmlElement (name = "vendor")
    private String mVendor;

    @XmlElement (name = "model")
    private String mModel;

    @XmlElement (name = "name")
    private String mName;

    @XmlElement (name = "ip")
    private String mIp;

    @XmlElement (name = "community")
    private String mCommunity;

    public String getVendor() {
        return mVendor;
    }

    public String getModel() {
        return mModel;
    }

    public String getIp() {
        return mIp;
    }

    public String getCommunity() {
        return mCommunity;
    }

    public String getName() {
        return mName;
    }
}
