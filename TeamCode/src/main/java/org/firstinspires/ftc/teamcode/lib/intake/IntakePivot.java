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
        left.setDirection(Constants.getLeftPivotDirection());
        right.setDirection(Constants.getRightPivotDirection());
    }

    // FIXME: deployed and homed positions might be different for left and right servos
    public void deploy() {
        right.setPosition(Constants.getDeployedPivotPosition());
        left.setPosition(Constants.getDeployedPivotPosition());
    }
    public void home() {
        right.setPosition(Constants.getHomedPivotPosition());
        left.setPosition(Constants.getHomedPivotPosition());
    }
}
