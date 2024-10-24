package org.firstinspires.ftc.teamcode.lib;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.sun.tools.javac.comp.Todo;

public class Constants {


    // LIST: INTAKE
    // SUBLIST: WHEELS
    public static boolean getLeftWheelInversion() {
        return false; // TODO: Test inversion
    }
    public static double getIntakePower() {
        return 1; // TODO: Tune
    }
    public static double getEjectPower() {
        return -1; // TODO: Tune
    }

    // SUBLIST: PIVOT
    public static Servo.Direction getLeftPivotDirection() {
        return Servo.Direction.REVERSE; // TODO: Test direction
    }
    public static Servo.Direction getRightPivotDirection() {
        return Servo.Direction.FORWARD; // TODO: Test direction
    }
    public static double getHomedPivotPosition() {
        return 0; // TODO: This should be the corrent homed position, but double check
    }
    public static double getDeployedPivotPosition() {
        return 0.15; // TODO: Tune
    }

    // SUBLIST: SLIDES
    public static int getMaxIntakeSlideExtension() {
        return 0; // TODO: Find extension
    }
    public static double getIntakeSlidePower() {
        return 0; // TODO: Tune
    }
    public static DcMotorSimple.Direction getLeftIntakeMotorDirection() {
        return DcMotorSimple.Direction.REVERSE; // TODO: Find direction
    }
    public static int getReadySlideExtension() {
        return 0; // TODO: Find extension
    }
    public static int getIntakeSlideExtension() {
        return 0; // TODO: Find Extension
    }
    public static int getHomedKillPowerThreshold() {
        return 10; // TODO: Find extension
    }
    public static int getReadyExtensionThreshold() {
        return 40; // TODO: Find extension
    }

    // LIST: DRIVE
    public static double getPrecisionModeLimit() {
        return 0.5; // TODO: find these values for MecanumDrive
    }
    public static DcMotorSimple.Direction getLeftDriveMotorDirection() {
        return DcMotorSimple.Direction.REVERSE; // TODO: Find direction
    }
}
