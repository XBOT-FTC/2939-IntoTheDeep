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
        left.setDirection(Constants.leftArmRotationDirection);
        right.setDirection(Constants.rightArmRotationDirection);
    }

    public void basketPosition() {
        right.setPosition(Constants.basketArmPosition);
        left.setPosition(Constants.basketArmPosition);
    }
    public void specimenIntakePosition() {
        right.setPosition(Constants.specimenIntakeArmPosition);
        left.setPosition(Constants.specimenIntakeArmPosition);
    }
    public void specimenHighPosition() {
        right.setPosition(Constants.specimenHighArmPosition);
        left.setPosition(Constants.specimenHighArmPosition);
    }
    public void specimenLowPosition() {
        right.setPosition(Constants.specimenLowArmPosition);
        left.setPosition(Constants.specimenLowArmPosition);
    }
    public void transferPosition() {
        right.setPosition(Constants.transferArmPosition);
        left.setPosition(Constants.transferArmPosition);
    }
    public void autoEndPosition() {
        right.setPosition(Constants.autoEndArmPosition);
        left.setPosition(Constants.autoEndArmPosition);
    }

}
