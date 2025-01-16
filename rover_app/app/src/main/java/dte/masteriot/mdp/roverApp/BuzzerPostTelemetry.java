package dte.masteriot.mdp.roverApp;

public class BuzzerPostTelemetry {

    String Buzzer;

    public BuzzerPostTelemetry(String buzzer) {
        Buzzer = buzzer;
    }

    public String getBuzzer() {
        return Buzzer;
    }

    public void setBuzzer(String buzzer) {
        Buzzer = buzzer;
    }
}
