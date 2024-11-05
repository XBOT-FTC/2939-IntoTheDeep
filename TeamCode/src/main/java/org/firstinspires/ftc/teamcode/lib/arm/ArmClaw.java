package org.firstinspires.ftc.teamcode.lib.arm;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.ElectricalContract;
import org.firstinspires.ftc.teamcode.lib.Constants;

public class ArmClaw {
    private final Servo servo;

    public ArmClaw(HardwareMap hardwareMap) {
        servo = hardwareMap.get(Servo.class, ElectricalContract.grabber());
        servo.setDirection(Constants.grabberDirection);
    }

    public void open() {
        servo.setPosition(Constants.openPosition);
    }
    public void close() {
        servo.setPosition(Constants.closedPosition);
    }

}