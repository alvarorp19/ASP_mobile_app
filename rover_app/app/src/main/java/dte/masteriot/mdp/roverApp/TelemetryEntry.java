package dte.masteriot.mdp.roverApp;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    @NonNull
    static public String unixTimeToDateTime(long unixTimestamp){

        Date date = new Date(unixTimestamp);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDate = sdf.format(date);

        return formattedDate;
    }
}
