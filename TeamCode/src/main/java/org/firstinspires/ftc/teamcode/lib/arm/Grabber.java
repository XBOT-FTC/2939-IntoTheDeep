package org.firstinspires.ftc.teamcode.lib.arm;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ElectricalContract;
import org.firstinspires.ftc.teamcode.lib.ButtonToggle;
import org.firstinspires.ftc.teamcode.lib.Constants;

public class Grabber {
    private Servo servo = null;
    public ButtonToggle buttonToggle = new ButtonToggle();

    public Grabber(HardwareMap hardwareMap) {
        servo = hardwareMap.get(Servo.class, ElectricalContract.grabber());
        servo.setDirection(Constants.getGrabberDirection());
    }

    public void openClose(Gamepad gamepad, Telemetry telemetry) {
        buttonToggle.update(gamepad.x);

        if (buttonToggle.isToggled()) {
            close();
        }
        else {
            open();
        }

        telemetry.addData("Grabber Position", servo.getPosition());
    }
    // TODO: Find position values
    public void open() {
        servo.setPosition(Constants.getOpenPosition());
    }
    public void close() {
        servo.setPosition(Constants.getClosedPosition());
    }

}