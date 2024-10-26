package org.firstinspires.ftc.teamcode.lib;


public class ButtonToggle {
    private boolean toggled = false;
    private boolean previousState = false;

    public void update(boolean currentState) {
        if (currentState && !previousState) {
            toggled = !toggled;
        }
        previousState = currentState;
    }

    public boolean isToggled() {
        return toggled;
    }
    public void setFalseToggle() {
        toggled = false;
        previousState = false;
    }
}