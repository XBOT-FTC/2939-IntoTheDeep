package org.firstinspires.ftc.teamcode.lib.intake;

import com.arcrobotics.ftclib.hardware.motors.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.ElectricalContract;
import org.firstinspires.ftc.teamcode.lib.Constants;

public class IntakePivot {
    Servo left = null;
    Servo right = null;

    public IntakePivot(HardwareMap hardwareMap) {
        left = hardwareMap.get(Servo.class, ElectricalContract.leftPivotServo());
        right = hardwareMap.get(Servo.class, ElectricalContract.rightPivotServo());
        left.setDirection(Constants.getLeftPivotDirection());
        right.setDirection(Constants.getRightPivotDirection());
    }

    public void deploy() {
        right.setPosition(Constants.getDeployedPivotPosition());
        left.setPosition(Constants.getDeployedPivotPosition());
    }
    public void home() {
        right.setPosition(Constants.getHomedPivotPosition());
        left.setPosition(Constants.getHomedPivotPosition());
    }
}
