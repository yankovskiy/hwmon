package ru.neverdark.hwmon;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ufo on 17.03.17.
 */
public class Utils {
    public static List<String> executeShellCommand(String command) {
        List<String> strings = new ArrayList<>();
        String[] cmd = { "/bin/bash", "-c", command };

        Process proc;
        try {
            proc = Runtime.getRuntime().exec(cmd);

            proc.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) {
                    strings.add(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (strings.size() == 0) {
            strings = null;
        }
        return strings;
    }

}
