package dte.masteriot.mdp.roverApp;

public class JoystickPostTelemetry {

    String xAxis, yAxis;

    public JoystickPostTelemetry(String joystick_axis_x, String getJoystick_axis_y) {
        this.xAxis = joystick_axis_x;
        this.yAxis = getJoystick_axis_y;
    }

    public String getJoystick_axis_x() {
        return xAxis;
    }

    public void setJoystick_axis_x(String joystick_axis_x) {
        this.xAxis = joystick_axis_x;
    }

    public String getGetJoystick_axis_y() {
        return yAxis;
    }

    public void setGetJoystick_axis_y(String getJoystick_axis_y) {
        this.yAxis = getJoystick_axis_y;
    }
}
