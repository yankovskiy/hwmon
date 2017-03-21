package ru.neverdark.hwmon.config;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by ufo on 15.03.17.
 */
public class MonitoringConfig {
    @XmlElement (name = "type")
    private String mType;

    @XmlElement (name = "ip")
    private String mIp;

    @XmlElement (name = "port")
    private String mPort;

    public String getType() {
        return mType;
    }

    public String getIp() {
        return mIp;
    }

    public String getPort() {
        return mPort;
    }
}
