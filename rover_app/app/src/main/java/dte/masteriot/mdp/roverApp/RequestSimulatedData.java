package dte.masteriot.mdp.roverApp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class RequestSimulatedData {


    private List<TelemetryEntry> rainRate; // Array de telemetría para "rain"
    private List<TelemetryEntry> solarRadiation;   // Array de telemetría para "UV"

    // Getters y setters
    public List<TelemetryEntry> getrainRate() {
        return rainRate;
    }

    public void setrainRate(List<TelemetryEntry> rain) {
        this.rainRate = rain;
    }

    public List<TelemetryEntry> getsolarRadiation() {
        return solarRadiation;
    }

    public void setsolarRadiation(List<TelemetryEntry> UV) {
        this.solarRadiation = UV;
    }
}
