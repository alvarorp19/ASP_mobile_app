package dte.masteriot.mdp.roverApp;

public class alarmDataEntry {

    private long lastUpdateTs;        // Timestamp
    private String key;   // Valor
    private boolean value;

    public long getLastUpdateTs() {
        return lastUpdateTs;
    }

    public void setLastUpdateTs(long lastUpdateTs) {
        this.lastUpdateTs = lastUpdateTs;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
