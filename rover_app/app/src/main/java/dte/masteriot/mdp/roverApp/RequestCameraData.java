package dte.masteriot.mdp.roverApp;

import java.util.List;

public class RequestCameraData {

    private List<TelemetryEntry> image;

    public List<TelemetryEntry> getImage() {
        return image;
    }

    public void setImage(List<TelemetryEntry> image) {
        this.image = image;
    }
}
