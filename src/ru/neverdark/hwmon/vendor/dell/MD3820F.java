package ru.neverdark.hwmon.vendor.dell;

import ru.neverdark.hwmon.SnmpManager;
import ru.neverdark.hwmon.Utils;
import ru.neverdark.hwmon.hardware.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ufo on 05.04.17.
 */
public class MD3820F extends DiskArray {

    private List<String> mList;

    protected MD3820F(String ip, String commnunity, String name, int snmpVersion) {
        super(ip, commnunity, name, snmpVersion);
    }

    public MD3820F(String ip, String name) {
        this(ip, null, name, 0);
    }

    @Override
    public void collectData() throws IOException {
        String cli = System.getProperty("user.dir") + "/dell";
        cli += " -e " + getIp() + " -c 'show storagearray profile;' > /tmp/MD3820F.hwmon";

        Utils.executeShellCommand(cli);
        File file = new File("/tmp/MD3820F.hwmon");
        mList = Files.readAllLines(file.toPath());
        file.delete();

        collectDiskData(null);
        collectSensorData(null);
    }

    @Override
    protected void collectDiskData(SnmpManager manager) throws IOException {
        boolean isDiskBlockFound = false;
        for (String str: mList) {
            if (isDiskBlockFound) {
                if (str.trim().length() == 0) {
                    break;
                }
                String[] pStr = str.trim().split("\\s+");
                String enc = pStr[0].replace(",", "");
                String slot = enc + "-" + pStr[1];
                String name = pStr[10];
                float fSize = Float.valueOf(pStr[3]);
                int size = (int) fSize;
                State state = pStr[2].equals("Optimal") ? State.HEALTH : State.FAIL;

                Disk disk = new Disk(slot, name, "None", size, state, false);
                addDisk(disk);
            } else if (isStartDiskBlock(str)) {
                isDiskBlockFound = true;
            }
        }

        List<String> spareLst = new ArrayList<>();
        for (String str: mList) {
            if (isSpareStr(str)) {
                String[] pStr = str.trim().split("\\s+");
                String enc = pStr[5].replace(",", "");
                String slot = enc + "-" + pStr[7];
                spareLst.add(slot);
            }
        }

        for (Disk disk: getDiskList()) {
            if (isInSpareList(spareLst, disk.getSlot())) {
                disk.setIsSpare(true);
            }
        }
    }

    private boolean isInSpareList(List<String> spareLst, String slot) {
        for (String spare: spareLst) {
            if (spare.equals(slot)) return true;
        }

        return false;
    }

    @Override
    protected void collectSensorData(SnmpManager manager) throws IOException {
        int countFan = 1;

        for (String str: mList) {
            if (isBattryStr(str)) {
                handleBatteryStr(str);
            } else if (isFanStr(str)) {
                handleFanStr(countFan, str);
                countFan++;
            } else if (isPSUStr(str)) {
                handlePSUStr(str);
            } else if (isTemperatureStr(str)) {
                handleTempStr(str);
            }
        }
    }

    private void handleTempStr(String str) {
        String[] pStr = str.trim().split(":");
        State state = pStr[1].trim().equals("Optimal")? State.HEALTH: State.FAIL;
        Sensor psu = new Sensor(SensorType.TEMPERATURE, pStr[0], "Unknown", state);
        addSensor(psu);
    }

    private boolean isTemperatureStr(String str) {
        return str.matches("\\s+Temperature sensor status.+");
    }

    private void handlePSUStr(String str) {
        String[] pStr = str.trim().split(":");
        State state = pStr[1].trim().equals("Optimal")? State.HEALTH: State.FAIL;
        Sensor psu = new Sensor(SensorType.PSU, pStr[0], state);
        addSensor(psu);
    }

    private boolean isPSUStr(String str) {
        return str.matches("\\s+Power supply status.+");
    }

    private void handleBatteryStr(String str) {
        String[] pStr = str.trim().split(":");
        State state = pStr[1].trim().equals("Optimal")? State.HEALTH: State.FAIL;
        Sensor battery = new Sensor(SensorType.BATTERY, "Battery", state);
        addSensor(battery);
    }

    private void handleFanStr(int countFan, String str) {
        String[] pStr = str.trim().split(":");
        State state = pStr[1].trim().equals("Optimal")? State.HEALTH: State.FAIL;
        String name = pStr[0].trim();
        switch (countFan) {
            case 3:
            case 4:
            case 9:
            case 10:
                name = "Left " + name;
                break;
            case 5:
            case 6:
            case 11:
            case 12:
                name = "Right " + name;
                break;
        }
        Sensor fan = new Sensor(SensorType.FAN, name, "Unknown", state);
        addSensor(fan);
    }

    private boolean isFanStr(String str) {
        return str.matches("(?i).+Fan.+status.+");
    }

    private boolean isBattryStr(String str) {
        return str.matches("\\s+Battery status.+");
    }

    private boolean isStartDiskBlock(String str) {
        return str.matches("\\s+ENCLOSURE,\\s+SLOT\\s+STATUS.+");
    }

    private boolean isSpareStr(String str) {
        return str.matches("\\s+Standby physical disk.+");
    }
}
