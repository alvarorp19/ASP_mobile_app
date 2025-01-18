package dte.masteriot.mdp.roverApp;

public class TelemetryEntry {

    private long ts;        // Timestamp
    private String value;   // Valor

    // Getters y setters
    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
