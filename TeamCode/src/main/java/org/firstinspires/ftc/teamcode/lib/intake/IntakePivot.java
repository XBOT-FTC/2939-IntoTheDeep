package org.firstinspires.ftc.teamcode.lib.intake;

import com.arcrobotics.ftclib.hardware.motors.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.ElectricalContract;

public class IntakePivot {
    Servo left = null;
    Servo right = null;

    enum PivotPositions {
        DEPLOYED,
        HOMED
    }

    public IntakePivot(HardwareMap hardwareMap) {
        left = hardwareMap.get(Servo.class, ElectricalContract.leftPivotServo());
        right = hardwareMap.get(Servo.class, ElectricalContract.rightPivotServo());
        //TODO: test servo direction
        left.setDirection(Servo.Direction.REVERSE);
    }

    public void deploy() {
        right.setPosition(getPivotPosition(PivotPositions.DEPLOYED));
        left.setPosition(getPivotPosition(PivotPositions.DEPLOYED));
    }
    public void home() {
        right.setPosition(getPivotPosition(PivotPositions.HOMED));
        left.setPosition(getPivotPosition(PivotPositions.HOMED));
    }

    // TODO: Find position values
    public double getPivotPosition(IntakePivot.PivotPositions pivotPosition) {
        double position = 0; // TODO: Set this to whatever HOMED position is
        switch(pivotPosition) {
            case DEPLOYED:
                position = 0.7;
                break;
            case HOMED:
                break;
        }
        return position;
    }

}
