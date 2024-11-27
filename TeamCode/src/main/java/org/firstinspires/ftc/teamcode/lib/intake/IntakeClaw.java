package org.firstinspires.ftc.teamcode.lib.intake;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.ElectricalContract;
import org.firstinspires.ftc.teamcode.lib.Constants;

public class IntakeClaw {

    private final Servo servo;

    public IntakeClaw(HardwareMap hardwareMap) {
        servo = hardwareMap.get(Servo.class, ElectricalContract.intakeClawServo());
        servo.setDirection(Constants.intakeClawDirection);
    }

    public void open() {
        servo.setPosition(Constants.intakeClawOpenPosition);
    }
    public void close() {
        servo.setPosition(Constants.intakeClawClosedPosition);
    }
    public void zero() {
        servo.setPosition(0);
    }
    public void one() {
        servo.setPosition(1);
    }

}
