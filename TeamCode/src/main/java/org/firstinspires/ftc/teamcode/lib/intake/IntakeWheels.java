package org.firstinspires.ftc.teamcode.lib.intake;

import com.arcrobotics.ftclib.hardware.motors.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.ElectricalContract;

public class IntakeWheels {
    CRServo leftCRServo = null;
    CRServo rightCRServo = null;
    //TODO: add rotation servos to this class
    public IntakeWheels(HardwareMap hardwareMap) {
        leftCRServo = hardwareMap.get(CRServo.class, ElectricalContract.leftIntakeServo());
        rightCRServo = hardwareMap.get(CRServo.class, ElectricalContract.rightIntakeServo());
        //TODO: invert servo
        leftCRServo.setInverted(true);
    }

    public void intake() {
        leftCRServo.set(1);
        rightCRServo.set(1);
    }
    public void eject() {
        leftCRServo.set(-1);
        rightCRServo.set(-1);
    }
}
