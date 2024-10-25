package org.firstinspires.ftc.teamcode.lib.arm;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ElectricalContract;
import org.firstinspires.ftc.teamcode.lib.ButtonToggle;
import org.firstinspires.ftc.teamcode.lib.Constants;

public class Wrist {
    private Servo servo = null;
    public ButtonToggle buttonToggle = new ButtonToggle();

    public Wrist(HardwareMap hardwareMap) {
        servo = hardwareMap.get(Servo.class, ElectricalContract.wrist());
        servo.setDirection(Constants.getWristDirection());
    }

    public void transferScore(Gamepad gamepad, Telemetry telemetry) {
        buttonToggle.update(gamepad.x);

        if (buttonToggle.isToggled()) {
            transfer();
        }
        else {
            score();
        }

        telemetry.addData("Wrist Position", servo.getPosition());
    }
    // TODO: Find position values
    public void transfer() {
        servo.setPosition(Constants.getWristTransferPosition());
    }
    public void score() {
        servo.setPosition(Constants.getWristScorePosition());
    }

}