package org.firstinspires.ftc.teamcode.lib.intake;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.ElectricalContract;
import org.firstinspires.ftc.teamcode.lib.Constants;

public class IntakePivot {
    private final Servo left;
    private final Servo right;

    public IntakePivot(HardwareMap hardwareMap) {
        left = hardwareMap.get(Servo.class, ElectricalContract.leftPivotServo());
        right = hardwareMap.get(Servo.class, ElectricalContract.rightPivotServo());
        left.setDirection(Constants.leftPivotDirection);
        right.setDirection(Constants.rightPivotDirection);
    }

    public void deploy() {
        right.setPosition(Constants.deployedPivotPosition);
        left.setPosition(Constants.deployedPivotPosition);
    }
    public void home() {
        right.setPosition(Constants.homedPivotPosition);
        left.setPosition(Constants.homedPivotPosition);
    }
}
