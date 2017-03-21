package ru.neverdark.hwmon.monitoring;

/**
 * Created by ufo on 15.03.17.
 */
public class CommonStatus {
    private String mColumnName;
    private String mStatus;
    private String mData;

    public CommonStatus(String columnName, String status, String data) {
        this.mColumnName = columnName;
        this.mStatus = status;
        this.mData = data;
    }

    public String getColumnName() {
        return mColumnName;
    }

    public String getStatus() {
        return mStatus;
    }

    public String getData() {
        return mData;
    }
}
