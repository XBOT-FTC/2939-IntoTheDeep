package org.firstinspires.ftc.teamcode.lib.arm;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.ElectricalContract;
import org.firstinspires.ftc.teamcode.lib.Constants;

public class ArmRotation {
    Servo left = null;
    Servo right = null;

    public ArmRotation(HardwareMap hardwareMap) {
        left = hardwareMap.get(Servo.class, ElectricalContract.leftRotationServo());
        right = hardwareMap.get(Servo.class, ElectricalContract.rightRotationServo());
        //TODO: test servo direction
        left.setDirection(Constants.getLeftArmRotateDirection());
        right.setDirection(Constants.getRightArmRotateDirection());
    }

    // TODO: Find position values
    public void highBasketPosition() {
        right.setPosition(Constants.getHighBasketArmPosition());
        left.setPosition(Constants.getHighBasketArmPosition());
    }
    public void specimenPosition() {
        right.setPosition(Constants.getSpecimenArmPosition());
        left.setPosition(Constants.getSpecimenArmPosition());
    }
    public void transferPosition() {
        right.setPosition(Constants.getTransferArmPosition());
        left.setPosition(Constants.getTransferArmPosition());
    }

}
