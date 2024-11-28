package org.firstinspires.ftc.teamcode.lib.intake;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.ElectricalContract;
import org.firstinspires.ftc.teamcode.lib.Constants;

public class IntakeClawSwivel {
    private final Servo servo;

    public IntakeClawSwivel(HardwareMap hardwareMap) {
        servo = hardwareMap.get(Servo.class, ElectricalContract.intakeClawSwivelServo());
        servo.setDirection(Constants.intakeClawSwivelDirection);
    }

    public void transfer() {
        servo.setPosition(Constants.ICSTransferPosition);
    }
    public void change() {
        servo.setPosition(Constants.ICSChangePosition);
    }
    public void setPos(double position) {
        servo.setPosition(position);
    }
    public void zero() {
        servo.setPosition(0);
    }
    public void one() {
        servo.setPosition(1);
    }
}
