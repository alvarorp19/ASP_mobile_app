package dte.masteriot.mdp.roverApp;

public class JoystickPostTelemetry {

    String joystick_axis_x, Joystick_axis_y;

    public JoystickPostTelemetry(String joystick_axis_x, String getJoystick_axis_y) {
        this.joystick_axis_x = joystick_axis_x;
        this.Joystick_axis_y = getJoystick_axis_y;
    }

    public String getJoystick_axis_x() {
        return joystick_axis_x;
    }

    public void setJoystick_axis_x(String joystick_axis_x) {
        this.joystick_axis_x = joystick_axis_x;
    }

    public String getGetJoystick_axis_y() {
        return Joystick_axis_y;
    }

    public void setGetJoystick_axis_y(String getJoystick_axis_y) {
        this.Joystick_axis_y = getJoystick_axis_y;
    }
}
