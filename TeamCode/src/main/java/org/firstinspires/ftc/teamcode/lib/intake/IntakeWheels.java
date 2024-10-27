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
        rightCRServo = hardwareMap.get(CRServo.class, ElectricalContract.rightIntakeServo());
        leftCRServo.setDirection(Constants.getLeftWheelDirection());
        rightCRServo.setDirection(Constants.getRightWheelDirection());
    }

    public void intake() {
        leftCRServo.setPower(Constants.getIntakePower());
        rightCRServo.setPower(Constants.getIntakePower());
    }
    public void eject() {
        leftCRServo.setPower(Constants.getEjectPower());
        rightCRServo.setPower(Constants.getEjectPower());
    }
    public void stop() {
        leftCRServo.setPower(0);
        rightCRServo.setPower(0);
    }
}
