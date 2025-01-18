package dte.masteriot.mdp.roverApp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class RequestSimulatedData {


    private List<TelemetryEntry> rain; // Array de telemetría para "rain"
    private List<TelemetryEntry> UV;   // Array de telemetría para "UV"

    // Getters y setters
    public List<TelemetryEntry> getRain() {
        return rain;
    }

    public void setRain(List<TelemetryEntry> rain) {
        this.rain = rain;
    }

    public List<TelemetryEntry> getUV() {
        return UV;
    }

    public void setUV(List<TelemetryEntry> UV) {
        this.UV = UV;
    }
}
