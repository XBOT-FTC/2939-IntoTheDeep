package org.firstinspires.ftc.teamcode.lib.intake;

import com.arcrobotics.ftclib.hardware.motors.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.ElectricalContract;

public class IntakePivot {
    Servo left = null;
    Servo right = null;

    public IntakePivot(HardwareMap hardwareMap) {
        left = hardwareMap.get(Servo.class, ElectricalContract.leftPivotServo());
        right = hardwareMap.get(Servo.class, ElectricalContract.rightPivotServo());
        //TODO: test servo direction
        left.setDirection(Servo.Direction.REVERSE);
    }

    // TODO: Find position values
    public void deploy() {
        right.setPosition(0.7);
        left.setPosition(0.7);
    }
    public void home() {
        right.setPosition(0);
        left.setPosition(0);
    }

}
