package ru.neverdark.hwmon;

import ru.neverdark.hwmon.config.Config;
import ru.neverdark.hwmon.config.DeviceConfig;
import ru.neverdark.hwmon.hardware.HardwareFactory;
import ru.neverdark.hwmon.hardware.NetworkHardware;
import ru.neverdark.hwmon.monitoring.Monitoring;
import ru.neverdark.hwmon.monitoring.MonitoringFactory;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {
        try {
            if (args.length == 0 || args.length > 2) {
                showUsage();
            } else if (args.length == 2) {
                if (args[1].equals("-dump")) {
                    dump(args[0]);
                }
            } else if (args.length == 1) {
                run(args[0]);
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void dump(String configFile) throws JAXBException, FileNotFoundException {
        Config config = Config.getInstance(configFile);

        for (DeviceConfig deviceConfig : config.getDeviceList()) {
            NetworkHardware hardware = null;
            try {
                hardware = HardwareFactory.getInstance(deviceConfig);
                hardware.collectData();
                hardware.dump();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }

    private static void run(String configFile) throws JAXBException, FileNotFoundException {
        Config config = Config.getInstance(configFile);
        try {
            Monitoring monitoring = MonitoringFactory.getInstance(config);
            monitoring.run();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static void showUsage() {
        System.out.println("java -jar hwmon.jar config.xml\n\tдля запуска из системы мониторинга");
        System.out.println("java -jar hwmon.jar config.xml -dump\n\tдля тестового вывода всех устройств мониторинга");
    }
}
