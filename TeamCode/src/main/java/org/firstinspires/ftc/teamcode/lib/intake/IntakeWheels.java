package org.firstinspires.ftc.teamcode.lib.intake;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.ElectricalContract;
import org.firstinspires.ftc.teamcode.lib.Constants;

public class IntakeWheels {
    private final com.qualcomm.robotcore.hardware.CRServo leftCRServo;
    private final com.qualcomm.robotcore.hardware.CRServo rightCRServo;


    public IntakeWheels(HardwareMap hardwareMap) {
        leftCRServo = hardwareMap.get(com.qualcomm.robotcore.hardware.CRServo.class, ElectricalContract.leftIntakeServo());
        rightCRServo = hardwareMap.get(com.qualcomm.robotcore.hardware.CRServo.class, ElectricalContract.rightIntakeServo());
        leftCRServo.setDirection(Constants.leftWheelDirection);
        rightCRServo.setDirection(Constants.rightWheelDirection);
    }

    public void intake() {
        leftCRServo.setPower(Constants.intakePower);
        rightCRServo.setPower(Constants.intakePower);
    }
    public void eject() {
        leftCRServo.setPower(Constants.ejectPower);
        rightCRServo.setPower(Constants.ejectPower);
    }
    public void stop() {
        leftCRServo.setPower(0);
        rightCRServo.setPower(0);
    }
}
