package ru.neverdark.hwmon.hardware;

import java.util.Locale;

/**
 * Created by ufo on 13.03.17.
 */
public class Sensor {
    private final SensorType mType;
    private State mState = State.UNAVAIL;
    private final String mName;
    private String mStrValue = null;

    public State getState() {
        return mState;
    }

    public SensorType getType() {
        return mType;
    }

    public String getName() {
        return mName;
    }

    public String getValue() {
        return mStrValue;
    }

    public Sensor(SensorType type, String name, String value, State state) {
        this.mName = name;
        this.mStrValue = value;
        this.mType = type;
        this.mState = state;
    }

    public Sensor(SensorType type, String name, State state) {
        this.mName = name;
        this.mType = type;
        this.mState = state;
    }

    @Override
    public String toString() {
        if (mType == SensorType.TEMPERATURE) {
            return String.format(Locale.US, "Name: %s. Value: %s. State: %s", mName, mStrValue, mState.toString());
        } else if (mType == SensorType.PSU || mType == SensorType.BATTERY) {
            return String.format(Locale.US, "Name: %s. State: %s", mName, mState.toString());
        } else if (mType == SensorType.FAN) {
            return String.format(Locale.US, "Name: %s. Speed: %s. State: %s", mName, mStrValue, mState.toString());
        }else {
            return null;
        }
    }
}
