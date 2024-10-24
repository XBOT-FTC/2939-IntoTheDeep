package org.firstinspires.ftc.teamcode.lib.intake;

import com.arcrobotics.ftclib.hardware.motors.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.ElectricalContract;

public class IntakeWheels {
    CRServo leftCRServo = null;
    CRServo rightCRServo = null;

    public IntakeWheels(HardwareMap hardwareMap) {
        leftCRServo = hardwareMap.get(CRServo.class, ElectricalContract.leftIntakeServo());
        rightCRServo = hardwareMap.get(CRServo.class, ElectricalContract.rightIntakeServo());
        //TODO: test servo direction
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

    public void stop() {
        leftCRServo.set(0);
        rightCRServo.set(0);
    }
}
