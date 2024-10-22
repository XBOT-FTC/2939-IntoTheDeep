package org.firstinspires.ftc.teamcode.lib.arm;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.ElectricalContract;

public class ArmRotation {
    Servo left = null;
    Servo right = null;

    public ArmRotation(HardwareMap hardwareMap) {
        left = hardwareMap.get(Servo.class, ElectricalContract.leftRotationServo());
        right = hardwareMap.get(Servo.class, ElectricalContract.rightRotationServo());
        //TODO: test servo direction
        left.setDirection(Servo.Direction.REVERSE);
    }

    // TODO: Find position values
    public void bucketPosition() {
        right.setPosition(1);
        left.setPosition(1);
    }
    public void specimenPosition() {
        right.setPosition(0.5);
        left.setPosition(0.5);
    }
    public void grabPosition() {
        right.setPosition(0);
        left.setPosition(0);
    }

}
