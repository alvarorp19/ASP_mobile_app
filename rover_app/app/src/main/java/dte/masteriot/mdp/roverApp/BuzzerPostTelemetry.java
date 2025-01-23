package dte.masteriot.mdp.roverApp;

public class BuzzerPostTelemetry {

    boolean buzzer;

    public BuzzerPostTelemetry(boolean buzzer) {
        this.buzzer = buzzer;
    }

    public boolean getBuzzer() {
        return this.buzzer;
    }

    public void setBuzzer(Boolean buzzer) {
        this.buzzer = buzzer;
    }
}
