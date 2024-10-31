package org.firstinspires.ftc.teamcode.lib.arm;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.ElectricalContract;
import org.firstinspires.ftc.teamcode.lib.Constants;

public class ArmWrist {
    private final Servo servo;

    public ArmWrist(HardwareMap hardwareMap) {
        servo = hardwareMap.get(Servo.class, ElectricalContract.wrist());
        servo.setDirection(Constants.getWristDirection());
    }

    public void transfer() {
        servo.setPosition(Constants.getWristTransferPosition());
    }
    public void score() {
        servo.setPosition(Constants.getWristScorePosition());
    }
    public void intakeSpecimen() {
        servo.setPosition(Constants.getWristIntakeSpecimenPosition());
    }
    public void scoreHighSpecimen() {
        servo.setPosition(Constants.getWristHighSpecimenPosition());
    }
    public void scoreLowSpecimen() {
        servo.setPosition(Constants.getWristLowSpecimenPosition());
    }

}