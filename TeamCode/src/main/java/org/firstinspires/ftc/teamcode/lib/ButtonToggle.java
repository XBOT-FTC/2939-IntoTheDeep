package org.firstinspires.ftc.teamcode.lib;


public class ButtonToggle {
    private boolean toggled = false;
    private boolean buttonPressed = false;

    public void press() {
        if (!buttonPressed) {
            toggled = !toggled;
        }
        buttonPressed = true;
    }

    public void letGo() {
        buttonPressed = false;
    }

    public boolean isToggled() {
        return toggled;
    }
    public void setFalseToggle() {
        toggled = false;
        buttonPressed = false;
    }
}