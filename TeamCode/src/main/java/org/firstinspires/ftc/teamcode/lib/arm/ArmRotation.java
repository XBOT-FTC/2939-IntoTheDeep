package org.firstinspires.ftc.teamcode.lib.arm;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ElectricalContract;
import org.firstinspires.ftc.teamcode.lib.Constants;

public class ArmRotation {
    Servo left;
    Servo right;

    public ArmRotation(HardwareMap hardwareMap) {
        left = hardwareMap.get(Servo.class, ElectricalContract.leftRotationServo());
        right = hardwareMap.get(Servo.class, ElectricalContract.rightRotationServo());
        left.setDirection(Constants.getLeftArmRotationDirection());
        right.setDirection(Constants.getRightArmRotationDirection());
    }

    public void basketPosition() {
        right.setPosition(Constants.getBasketArmPosition());
        left.setPosition(Constants.getBasketArmPosition());
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
