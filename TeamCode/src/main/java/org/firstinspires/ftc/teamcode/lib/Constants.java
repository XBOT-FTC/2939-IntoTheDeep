package org.firstinspires.ftc.teamcode.lib;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class Constants {

    // LIST: ARM
    // SUBLIST: GRABBER
    public static Servo.Direction getGrabberDirection() {
        return Servo.Direction.REVERSE; // TODO: Test direction
    }
    public static double getOpenPosition() {
        return 0.25; // TODO: Tune
    }
    public static double getClosedPosition() {
        return 0.5; // TODO: Tune
    }

    // SUBLIST: ROTATION
    public static Servo.Direction getLeftArmRotationDirection() {
        return Servo.Direction.REVERSE;
    }
    public static Servo.Direction getRightArmRotationDirection() {
        return Servo.Direction.FORWARD;
    }
    public static double getBasketArmPosition() {
        return 1; // TODO: Tune
    }
    public static double getSpecimenArmPosition() {
        return 0; // TODO: Tune
    }
    public static double getTransferArmPosition() {
        return 0.2; // TODO: Tune
    }

    // SUBLIST: SLIDES
    public static int getMaxArmSlideExtension() {
        return 0; // TODO: Find extension
    }
    public static DcMotorSimple.Direction getLeftArmSlideDirection() {
        return DcMotorSimple.Direction.REVERSE; // TODO: Find direction
    }
    public static int getHighBasketSlideExtension() {
        return 0; // TODO: Tune
    }
    public static int getLowBasketSlideExtension() {
        return 0; // TODO: Tune
    }
    public static int getSpecimenSlideExtension() {
        return 0; // TODO: Tune
    }
    public static int getHangSlideExtension() {
        return 0; // TODO: Tune
    }
    public static int getHomedSlideExtension() {
        return 0; // TODO: Tune
    }
    public static int getTransferSlideExtension() {
        return 0; // TODO: Tune
    }
    public static int getScoringExtensionThreshold() {
        return 40; // TODO: Find extension
    }

    // SUBLIST: WRIST
    public static Servo.Direction getWristDirection() {
        return Servo.Direction.REVERSE; // TODO: Test direction
    }
    public static double getWristTransferPosition() {
        return 0.4; // TODO: Tune
    }
    public static double getWristScorePosition() {
        return 0.6; // TODO: Tune
    }

    // LIST: INTAKE
    // SUBLIST: WHEELS
    public static DcMotorSimple.Direction getLeftWheelDirection() {
        return DcMotorSimple.Direction.REVERSE; // TODO: Test direction
    }
    public static DcMotorSimple.Direction getRightWheelDirection() {
        return DcMotorSimple.Direction.FORWARD; // TODO: Test direction
    }
    public static double getIntakePower() {
        return 0.6; // TODO: Tune
    }
    public static double getEjectPower() {
        return -0.6; // TODO: Tune
    }

    // SUBLIST: PIVOT
    public static Servo.Direction getLeftPivotDirection() {
        return Servo.Direction.REVERSE; // TODO: Test direction
    }
    public static Servo.Direction getRightPivotDirection() {
        return Servo.Direction.FORWARD; // TODO: Test direction
    }
    public static double getHomedPivotPosition() {
        return 0.4; // TODO: This should be the homed position if we zero, but double check FIXME: this might be different for left and right servos
    }
    public static double getDeployedPivotPosition() {
        return 0.6; // TODO: Tune FIXME: this might be different for left and right servos
    }

    // SUBLIST: SLIDES
    public static int getMaxIntakeSlideExtension() {
        return 0; // TODO: Find extension
    }
    public static double getIntakeSlidePower() {
        return 0; // TODO: Tune
    }
    public static DcMotorSimple.Direction getLeftIntakeMotorDirection() {
        return DcMotorSimple.Direction.FORWARD; // TODO: Find direction
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
