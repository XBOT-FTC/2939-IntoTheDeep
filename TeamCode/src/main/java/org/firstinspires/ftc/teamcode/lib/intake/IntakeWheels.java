package org.firstinspires.ftc.teamcode.lib.intake;

import com.arcrobotics.ftclib.hardware.motors.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.ElectricalContract;
import org.firstinspires.ftc.teamcode.lib.Constants;

public class IntakeWheels {
    private final CRServo leftCRServo;
    private final CRServo rightCRServo;


    public IntakeWheels(HardwareMap hardwareMap) {
        leftCRServo = hardwareMap.get(CRServo.class, ElectricalContract.leftIntakeServo());
        rightCRServo = hardwareMap.get(CRServo.class, ElectricalContract.rightIntakeServo());
        leftCRServo.setInverted(Constants.getLeftWheelInversion());
        rightCRServo.setInverted(!Constants.getLeftWheelInversion());
    }

    public void intake() {
        leftCRServo.set(Constants.getIntakePower());
        rightCRServo.set(Constants.getIntakePower());
    }
    public void eject() {
        leftCRServo.set(Constants.getEjectPower());
        rightCRServo.set(Constants.getEjectPower());
    }
    public void stop() {
        leftCRServo.set(0);
        rightCRServo.set(0);
    }
}
