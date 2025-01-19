package dte.masteriot.mdp.roverApp;

import java.util.List;

public class RequestRealData {

    private List<TelemetryEntry> battery;
    private List<TelemetryEntry> pressure;
    private List<TelemetryEntry> humidity;
    private List<TelemetryEntry> temperature;
    private List<TelemetryEntry> PM2_5;
    private List<TelemetryEntry> PM10;


    public List<TelemetryEntry> getBattery() {
        return battery;
    }

    public void setBattery(List<TelemetryEntry> battery) {
        this.battery = battery;
    }

    public List<TelemetryEntry> getPM10() {
        return PM10;
    }

    public void setPM10(List<TelemetryEntry> PM10) {
        this.PM10 = PM10;
    }

    public List<TelemetryEntry> getTemperature() {
        return temperature;
    }

    public void setTemperature(List<TelemetryEntry> temperature) {
        this.temperature = temperature;
    }

    public List<TelemetryEntry> getHumidity() {
        return humidity;
    }

    public void setHumidity(List<TelemetryEntry> humidity) {
        this.humidity = humidity;
    }

    public List<TelemetryEntry> getPressure() {
        return pressure;
    }

    public void setPressure(List<TelemetryEntry> pressure) {
        this.pressure = pressure;
    }

    public List<TelemetryEntry> getPM2_5() {
        return PM2_5;
    }

    public void setPM2_5(List<TelemetryEntry> PM2_5) {
        this.PM2_5 = PM2_5;
    }
}
