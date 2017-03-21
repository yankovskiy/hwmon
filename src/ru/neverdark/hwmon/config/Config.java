package ru.neverdark.hwmon.config;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

/**
 * Created by ufo on 15.03.17.
 */
@XmlRootElement (name = "config")
public class Config {
    private static Config mConfig;

    @XmlElement (name = "monitoring")
    private MonitoringConfig mMonitoring;

    @XmlElementWrapper (name = "devices")
    @XmlElement (name = "device")
    private List<DeviceConfig> mDeviceList;

    public static Config getInstance(String configFile) throws JAXBException, FileNotFoundException {
        if (mConfig == null) {
            JAXBContext context = JAXBContext.newInstance(Config.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            mConfig = (Config) unmarshaller.unmarshal(new FileReader(configFile));
        }

        return mConfig;
    }

    public MonitoringConfig getMonitoringConfig() {
        return mMonitoring;
    }

    public List<DeviceConfig>  getDeviceList() {
        return mDeviceList;
    }
}
