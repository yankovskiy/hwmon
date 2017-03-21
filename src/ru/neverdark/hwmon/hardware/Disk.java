package ru.neverdark.hwmon.hardware;

import java.util.Locale;

/**
 * Created by ufo on 06.03.17.
 */
public class Disk {
    private final String mSerial;
    private final boolean mIsSpare;
    private final int mSlot;

    public Disk(int slotNumber, String name, String serial, int size, State state, boolean isSpare) {
        this.mSlot = slotNumber;
        this.mName = name;
        this.mState = state;
        this.mSize = size;
        this.mSerial = serial;
        this.mIsSpare = isSpare;
    }

    public int getSlot() {
        return mSlot;
    }

    public boolean isSpareDisk() {
        return mIsSpare;
    }

    public String getSerial() {
        return mSerial;
    }

    public String getName() {
        return mName;
    }

    public State getState() {
        return mState;
    }

    public int getSize() {
        return mSize;
    }

    private final String mName;
    private final State mState;
    private final int mSize;

    @Override
    public String toString() {
        return String.format(Locale.US, "Slot: %d. Model: %s. Serial: %s. Size: %dGB. State: %s. HotSpare: %b", mSlot, mName.trim(), mSerial.trim(), mSize, mState.toString(), mIsSpare);
    }
}
