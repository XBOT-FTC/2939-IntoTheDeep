package org.firstinspires.ftc.teamcode.lib;


public class ButtonToggle {
    private boolean toggled = false;
    private boolean buttonPressed = false;

    // press() and letGo() are for uses where you want to separately let the toggle know when you've pressed and let go
    // update() is for one line that does both.
    public void press() {
        if (!buttonPressed) {
            toggled = !toggled;
        }
        buttonPressed = true;
    }

    public void letGo() {
        buttonPressed = false;
    }

    public void update(boolean press) {
        if (press && !buttonPressed) {
            toggled = !toggled;
        }
        buttonPressed = press;
    }

    public boolean isToggled() {
        return toggled;
    }
    public void setFalseToggle() {
        toggled = false;
        buttonPressed = false;
    }
}